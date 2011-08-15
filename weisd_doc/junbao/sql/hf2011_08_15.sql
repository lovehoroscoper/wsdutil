----------------------------------------------------
-- Export file for user HF                        --
-- Created by Administrator on 2011-8-15, 8:56:53 --
----------------------------------------------------

spool hf2011_08_15.log

prompt
prompt Creating function CHECK_CHARGEMONEY_MATCH
prompt =========================================
prompt
CREATE OR REPLACE FUNCTION HF."CHECK_CHARGEMONEY_MATCH"(content     IN NUMBER,
                                                     minmoney    IN NUMBER,
                                                     maxmoney    IN NUMBER,
                                                     chargemoney IN NUMBER)
  RETURN VARCHAR2 IS

  resultno VARCHAR2(4);
  l_mod    NUMBER;
  /******************************************************************************
  * 用于检查充值金额是否与货架金额匹配
  *   PRODUCT_CONTENT    NUMBER(10,2), 步长
  *   PRODUCT_MINMONEY   NUMBER(10,2), 最小金额
  *   PRODUCT_MAXMONEY   NUMBER(10,2), 最大金额
  *   CHARGE_MONEY       NUMBER(10,2), 充值金额
  *
  *
  *   weisd
  *
  ******************************************************************************/
BEGIN

  resultno := '0011';

  if chargemoney is null or chargemoney = 0 or chargemoney < minmoney or chargemoney > maxmoney then
    resultno := '0011';
  elsif chargemoney = minmoney or chargemoney = maxmoney then
    resultno := '0000';
  else
    select mod(chargemoney - minmoney, content) into l_mod from dual;
    if l_mod = 0 then
      --能整除
      resultno := '0000';
    else
      resultno := '0011';
    end if;
  end if;
  return resultno;
END CHECK_CHARGEMONEY_MATCH;
/

prompt
prompt Creating function COV_POINFO_ORDER
prompt ==================================
prompt
CREATE OR REPLACE FUNCTION HF."COV_POINFO_ORDER" (v_poid NUMBER) RETURN NUMBER AS
  v_order NUMBER;
  v_temp_money number;
BEGIN

select hp.product_content into v_temp_money from hf_productonlineinfo hpo,hf_productinfo hp where hpo.product_id = hp.product_id and  hpo.online_id =v_poid;


SELECT tt.product_content,tt.rn into v_temp_money,v_order FROM (
select t.product_content,rownum rn from (
select h1.product_content
 from (select * from hf_productonlineinfo hpo,hf_productinfo hp where hpo.product_id = hp.product_id) h1
         ,(select * from hf_productonlineinfo hpo,hf_productinfo hp where hpo.product_id = hp.product_id and  hpo.online_id =v_poid ) h2
 where h1.order_source = h2.order_source and h1.product_ispid = h2.product_ispid and h1.product_type = h2.product_type
       and h1.product_provinceid = h2.product_provinceid and h1.product_status = h2.product_status
       group by h1.order_source, h1.product_ispid,h1.product_type,h1.product_provinceid,h1.product_status,h1.product_content
       order by h1.product_content
       ) t )tt where tt.product_content = v_temp_money;
return v_order;

EXCEPTION
  WHEN OTHERS THEN
  HF_DBWARNING(1,'cov_poinfo_order',SQLCODE||'：'||SQLERRM,'','','');
    RETURN 9999;
END;
/

prompt
prompt Creating function CZ_FORMAT
prompt ===========================
prompt
CREATE OR REPLACE FUNCTION HF."CZ_FORMAT" (in_format IN VARCHAR2,in_charge_money IN NUMBER,in_charge_mobile IN VARCHAR2) RETURN VARCHAR2 IS


temp_format_var VARCHAR2(100);
/******************************************************************************
   NAME:       limit_Time
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-3-14      synck           1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     limit_Time
      Sysdate:         2008-10-14
      Date and Time:   2008-10-14, 13:43:43, and 2008-3-14 13:43:43
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
--HFCZ#charge_password#charge_money#charge_mobile
  temp_format_var:= replace(in_format,'charge_money',to_char(in_charge_money));
  temp_format_var:= replace(temp_format_var,'charge_mobile',in_charge_mobile);
  return temp_format_var;

END cz_format;
/

prompt
prompt Creating function FUN_GETPROVID
prompt ===============================
prompt
CREATE OR REPLACE FUNCTION HF."FUN_GETPROVID" (v_provinceid NUMBER) RETURN VARCHAR2 AS
  v_name VARCHAR2(10);
BEGIN
  select t.province_name into v_name from hf_province t where t.province_id = v_provinceid;
  RETURN v_name;
EXCEPTION
  WHEN OTHERS THEN
    RETURN '9999';
END;
/

prompt
prompt Creating function GET_CHANNEL_MONEY
prompt ===================================
prompt
CREATE OR REPLACE FUNCTION HF."GET_CHANNEL_MONEY"(i_channel_id IN NUMBER)
  RETURN VARCHAR2 AS
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：获取渠道支持的面值
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-5-10
  修改内容：
  ************************************************************/
  support_money VARCHAR2(5000);
  temp_money    NUMBER;
  CURSOR support_money_list IS
    SELECT H.MONEY FROM ch_money H WHERE H.CHANNEL_ID = i_channel_id;
BEGIN
  support_money := '';
  OPEN support_money_list;
  LOOP
    FETCH support_money_list
      INTO temp_money;
    EXIT WHEN support_money_list%NOTFOUND;
    support_money := CONCAT(support_money, TO_CHAR(temp_money) || ',');
  END LOOP;
  CLOSE support_money_list;

  IF LENGTH(support_money) > 0 THEN
  
    RETURN SUBSTR(support_money, 1, LENGTH(support_money) - 1);
  
  END IF;

  return support_money;
END get_channel_money;
/

prompt
prompt Creating function GET_CHANNEL_PROVINCE
prompt ======================================
prompt
create or replace function hf.get_channel_province(v_channel_id in number)
  return VARCHAR2 as
  l_channel_provincename varchar2(200);
  temp_province_name     varchar2(200);
  cursor channel_province_list is
    select p.province_name
      from ch_channelprovince pro, sys_province p
     where pro.channel_provinceid = p.province_id
       and pro.channel_id = v_channel_id order by pro.channel_provinceid;
begin
  l_channel_provincename := '';
  open channel_province_list;
  loop
    fetch channel_province_list
      into temp_province_name;
    exit when channel_province_list%NOTFOUND;
    l_channel_provincename := concat(l_channel_provincename,
                                     temp_province_name || ',');
  end loop;
  close channel_province_list;

  if length(l_channel_provincename) > 0 then
    RETURN SUBSTR(l_channel_provincename,
                  1,
                  LENGTH(l_channel_provincename) - 1);
  end if;
  return l_channel_provincename;
end get_channel_province;
/

prompt
prompt Creating function GET_CHANNEL_SUPPORT_BUSINESS
prompt ==============================================
prompt
CREATE OR REPLACE FUNCTION HF.GET_CHANNEL_SUPPORT_BUSINESS(v_channel_id IN NUMBER)
  RETURN VARCHAR2 AS
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：获取渠道支持的业务类型
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-5-10
  修改内容：
  ************************************************************/
  l_support_business_name VARCHAR2(100);
  l_support_business_id   VARCHAR2(20);
  l_temp_business         NUMBER;
  cursor support_business_list is
    select h.service_type
      from ch_supporttbusiness h
     where h.channel_id = v_channel_id;

BEGIN
  l_support_business_name := '';
  l_support_business_id   := '';
  OPEN support_business_list;
  LOOP
    FETCH support_business_list
      INTO l_temp_business;
    EXIT WHEN support_business_list%NOTFOUND;
    l_support_business_id := CONCAT(l_support_business_id,
                                    l_temp_business || ',');
    IF l_temp_business = 0 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        '手机' || ',');
      --业务类型：0手机，1固话，2小联通，3宽带
    ELSIF l_temp_business = 1 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        '固话' || ',');
    ELSIF l_temp_business = 2 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        '小灵通' || ',');
    ELSIF l_temp_business = 3 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        '宽带' || ',');
    ELSE
      l_support_business_name := CONCAT(l_support_business_name,
                                        l_temp_business || ',');
    END IF;
  END LOOP;
  CLOSE support_business_list;

  IF LENGTH(l_support_business_name) > 0 THEN
    RETURN SUBSTR(l_support_business_id,
                  1,
                  LENGTH(l_support_business_id) - 1) || '|' || SUBSTR(l_support_business_name,
                                                                      1,
                                                                      LENGTH(l_support_business_name) - 1);
  END IF;
  RETURN l_support_business_id || '|' || l_support_business_name;
END;
/

prompt
prompt Creating function GET_HF_ORDERLEV
prompt =================================
prompt
CREATE OR REPLACE FUNCTION HF."GET_HF_ORDERLEV" (hf_ordersource   in number,req_userid varchar2) RETURN NUMBER aS
tmpVar NUMBER;
var_lev number;
/******************************************************************************
   NAME:       get_hf_orderlev
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2009-12-17          1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     get_hf_orderlev
      Sysdate:         2009-12-17
      Date and Time:   2009-12-17, 10:24:52, and 2009-12-17 10:24:52
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
   tmpVar := -1;


    select count(0) into tmpVar from hf_m_orderlevel h where h.order_source = hf_ordersource and h.data_source not like 'PU%';
   if tmpVar = 1 then
      select h.req_level into var_lev from hf_m_orderlevel h where h.order_source = hf_ordersource and h.data_source not like 'PU%';
      return var_lev;
   end if;

   select count(0) into tmpVar from hf_m_orderlevel h where h.order_source = hf_ordersource and h.data_source =req_userid;
   if   tmpVar = 1 then
        select h.req_level into var_lev from hf_m_orderlevel h where h.order_source = hf_ordersource and h.data_source =req_userid;
         return var_lev;
   else
         return 5;
   end if;

   EXCEPTION

     WHEN OTHERS THEN
        return -1;
       -- Consider logging the error and then re-raise

END get_hf_orderlev;
/

prompt
prompt Creating function GET_ONLINE_CHANNEL_ALL
prompt ========================================
prompt
CREATE OR REPLACE FUNCTION HF.GET_ONLINE_CHANNEL_ALL(v_ispId          IN NUMBER,
                                                  v_provinceId     in number,
                                                  v_cityCode       in varchar2,
                                                  v_osId           in number,
                                                  v_userId         in varchar2,
                                                  v_productContent in number,
                                                  v_productType    in number)
  RETURN VARCHAR2 AS
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：获取货架支持的所有渠道ID（包括开通和关闭的）信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-5-10
  修改内容：返回渠道ID,用","隔开
  ************************************************************/
  l_channelIds VARCHAR2(100);
BEGIN
  l_channelIds := '';
  FOR channel IN (SELECT cc.channel_id, cc.channel_name
                    FROM ch_channelinfo cc
                   WHERE channel_needcard = 0 --不带卡渠道
                     AND channel_isp = v_ispId --支持运营商
                     AND (channel_provinceid = v_provinceId or
                         channel_provinceid = '-1') --支持省份
                     AND (channel_citycode = '-1' OR
                         channel_citycode LIKE '%' || v_cityCode || '%') --支持地市
                        /* AND channel_status = 0 */ --渠道可用
                     AND (cc.channel_limit_money = -1 or
                         cc.channel_money <= cc.channel_limit_money) --交易量没有超限
                     AND channel_id NOT IN --不是禁止走的渠道
                         (SELECT DISTINCT channel_id
                            FROM ch_notmatchchannel nm
                           WHERE order_source = v_osId
                             AND (user_id = '-1' OR user_id = v_userId))
                     AND ( --支持面值
                          (channel_step <> 0 AND
                          MOD(v_productContent - channel_minmoney,
                               channel_step) = 0 AND
                          v_productContent >= cc.channel_minmoney AND
                          v_productContent <= cc.channel_maxmoney) OR
                          (channel_step = 0 AND
                          v_productContent IN
                          (SELECT DISTINCT money
                              FROM ch_money
                             WHERE channel_id = cc.channel_id)))
                     AND EXISTS
                   (SELECT *
                            FROM ch_supporttbusiness
                           WHERE channel_id = cc.channel_id
                             AND service_type = v_productType)) loop
    l_channelIds := l_channelIds || channel.channel_id || ',';
  END LOOP;
  if l_channelIds is not null then
    l_channelIds := ',' || l_channelIds;
  end if;
  return l_channelIds;
END;
/

prompt
prompt Creating function GET_ONLINE_CHANNEL_OFF
prompt ========================================
prompt
CREATE OR REPLACE FUNCTION HF.GET_ONLINE_CHANNEL_OFF(v_ispId          IN NUMBER,
                                                  v_provinceId     in number,
                                                  v_cityCode       in varchar2,
                                                  v_osId           in number,
                                                  v_userId         in varchar2,
                                                  v_productContent in number,
                                                  v_productType    in number)
  RETURN VARCHAR2 AS
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：获取货架支持的关闭的渠道ID信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-5-10
  修改内容：返回渠道ID,用","隔开
  ************************************************************/
  l_channelIds VARCHAR2(100);
BEGIN
  l_channelIds := '';
  FOR channel IN (SELECT cc.channel_id, cc.channel_name
                    FROM ch_channelinfo cc
                   WHERE channel_needcard = 0 --不带卡渠道
                     AND channel_isp = v_ispId --支持运营商
                     AND (channel_provinceid = v_provinceId or
                         channel_provinceid = '-1') --支持省份
                     AND (channel_citycode = '-1' OR
                         channel_citycode LIKE '%' || v_cityCode || '%') --支持地市
                         AND channel_status > 0  --渠道可用
                     AND (cc.channel_limit_money = -1 or
                         cc.channel_money <= cc.channel_limit_money) --交易量没有超限
                     AND channel_id NOT IN --不是禁止走的渠道
                         (SELECT DISTINCT channel_id
                            FROM ch_notmatchchannel nm
                           WHERE order_source = v_osId
                             AND (user_id = '-1' OR user_id = v_userId))
                     AND ( --支持面值
                          (channel_step <> 0 AND
                          MOD(v_productContent - channel_minmoney,
                               channel_step) = 0 AND
                          v_productContent >= cc.channel_minmoney AND
                          v_productContent <= cc.channel_maxmoney) OR
                          (channel_step = 0 AND
                          v_productContent IN
                          (SELECT DISTINCT money
                              FROM ch_money
                             WHERE channel_id = cc.channel_id)))
                     AND EXISTS
                   (SELECT *
                            FROM ch_supporttbusiness
                           WHERE channel_id = cc.channel_id
                             AND service_type = v_productType)) loop
    l_channelIds := l_channelIds || channel.channel_id || ',';
  END LOOP;
  if l_channelIds is not null then
    l_channelIds := ',' || l_channelIds;
  end if;
  return l_channelIds;
END;
/

prompt
prompt Creating function GET_ONLINE_CHANNEL_ON
prompt =======================================
prompt
CREATE OR REPLACE FUNCTION HF.GET_ONLINE_CHANNEL_ON(v_ispId          IN NUMBER,
                                                  v_provinceId     in number,
                                                  v_cityCode       in varchar2,
                                                  v_osId           in number,
                                                  v_userId         in varchar2,
                                                  v_productContent in number,
                                                  v_productType    in number)
  RETURN VARCHAR2 AS
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：获取货架支持的开通的渠道ID信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-5-10
  修改内容：返回渠道ID,用","隔开
  ************************************************************/
  l_channelIds VARCHAR2(100);
BEGIN
  l_channelIds := '';
  FOR channel IN (SELECT cc.channel_id, cc.channel_name
                    FROM ch_channelinfo cc
                   WHERE channel_needcard = 0 --不带卡渠道
                     AND channel_isp = v_ispId --支持运营商
                     AND (channel_provinceid = v_provinceId or
                         channel_provinceid = '-1') --支持省份
                     AND (channel_citycode = '-1' OR
                         channel_citycode LIKE '%' || v_cityCode || '%') --支持地市
                         AND channel_status = 0  --渠道可用
                     AND (cc.channel_limit_money = -1 or
                         cc.channel_money <= cc.channel_limit_money) --交易量没有超限
                     AND channel_id NOT IN --不是禁止走的渠道
                         (SELECT DISTINCT channel_id
                            FROM ch_notmatchchannel nm
                           WHERE order_source = v_osId
                             AND (user_id = '-1' OR user_id = v_userId))
                     AND ( --支持面值
                          (channel_step <> 0 AND
                          MOD(v_productContent - channel_minmoney,
                               channel_step) = 0 AND
                          v_productContent >= cc.channel_minmoney AND
                          v_productContent <= cc.channel_maxmoney) OR
                          (channel_step = 0 AND
                          v_productContent IN
                          (SELECT DISTINCT money
                              FROM ch_money
                             WHERE channel_id = cc.channel_id)))
                     AND EXISTS
                   (SELECT *
                            FROM ch_supporttbusiness
                           WHERE channel_id = cc.channel_id
                             AND service_type = v_productType)) loop
    l_channelIds := l_channelIds || channel.channel_id || ',';
  END LOOP;
  if l_channelIds is not null then
    l_channelIds := ',' || l_channelIds;
  end if;
  return l_channelIds;
END;
/

prompt
prompt Creating function ISNEEDCHECKISP
prompt ================================
prompt
CREATE OR REPLACE FUNCTION HF."ISNEEDCHECKISP" (hf_seraliid  in  varchar2 ) RETURN NUMBER IS
tmpVar NUMBER;
v_temp_isp        number;
v_temp_acc_provinceid      number;
v_temp_card_provinceid     number;
/******************************************************************************
   NAME:       isNeedCheckIsp
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2009-11-12          1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     isNeedCheckIsp
      Sysdate:         2009-11-12
      Date and Time:   2009-11-12, 9:49:12, and 2009-11-12 9:49:12
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
   if hf_seraliid is null then
      return  1;
   end if;
   select count(0) into tmpVar  from hf_fullnote h1,hf_orderinfo h2 where h1.hf_orderid = h2.req_orderid and h1.hf_serialid =hf_seraliid and h1.card_source in (0,1);
   if tmpVar <> 1 then
      return  1;
   end if;
    select h1.hf_ispid,h2.req_provinceid,h1.hf_cardprovinceid  into v_temp_isp,v_temp_acc_provinceid,v_temp_card_provinceid  from hf_fullnote h1,hf_orderinfo h2 where h1.hf_orderid = h2.req_orderid and h1.hf_serialid =hf_seraliid;

    select count(0) into tmpVar from hf_checkcardinfo h where h.cc_ispid = v_temp_isp and h.acc_provinceid = v_temp_acc_provinceid and h.card_provinceid = v_temp_card_provinceid and h.cc_flag = 0 ;

   if tmpVar = 0 then
      return 1;
   else
      return 0;
   end if;
   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
        return 0;
END isNeedCheckIsp;
/

prompt
prompt Creating function LIMIT_TIME
prompt ============================
prompt
CREATE OR REPLACE FUNCTION HF."LIMIT_TIME" (finsh_time IN DATE) RETURN VARCHAR2 IS


temp_time VARCHAR2(20);
/******************************************************************************
   NAME:       limit_Time
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-3-14          1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     limit_Time
      Sysdate:         2008-3-14
      Date and Time:   2008-3-14, 13:43:43, and 2008-3-14 13:43:43
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN

  select decode(substr(time,1,1),'-','-','')||
      decode(ABS(trunc(time)),0,'',ABS(trunc(time))||'天')||
    decode(LENGTH(ABS(trunc((time-trunc(time))*24))),1,'0','')||ABS(trunc((time-trunc(time))*24))||':'||
    decode(LENGTH(ABS(ABS(trunc((time-trunc(time)-trunc((time-trunc(time))*24)/24)*1440)))),1,'0','')||ABS(trunc((time-trunc(time)-trunc((time-trunc(time))*24)/24)*1440))||':'||
    decode(LENGTH(ABS(trunc((time-trunc(time)-trunc((time-trunc(time))*24)/24-trunc((time-trunc(time)-trunc((time-trunc(time))*24)/24)*1440)/1440)*86400))),1,'0','')||ABS(trunc((time-trunc(time)-trunc((time-trunc(time))*24)/24-trunc((time-trunc(time)-trunc((time-trunc(time))*24)/24)*1440)/1440)*86400)) into temp_time
 from
(select finsh_time-sysdate time from dual);




  return temp_time;
END limit_Time;
/

prompt
prompt Creating function MYCONVERT
prompt ===========================
prompt
CREATE OR REPLACE FUNCTION HF."MYCONVERT"
(
    p_str   varchar2,
    p_delim varchar2
) return t_vc as
    v_result   t_vc;
    v_delimlen number := length(p_delim);
    i          number := 1;
    j          number;
    p          integer; --pointer to string
    p2         integer; --pointer to delim
begin
    if p_str is null then
        return null;
    elsif p_delim is null then
        return t_vc(p_str);
    end if;

    v_result := t_vc();
    p := 1;
    loop
        p2 := instr(p_str, p_delim, p);
        v_result.extend;
        if p2 <> 0 then
            v_result(i) := substr(p_str, p, p2 - p);
        else
            v_result(i) := substr(p_str, p);
            exit;
        end if;

        -- check duplication value
        -- 检查分割开的数据之间是不是存在相等的值
        /*
        for j in 1..i loop
            if j<>i and v_result(j)=v_result(i) then
                raise_application_error(-20000, 'Duplicate value on '||j||' and '||i||' :'||v_result(i));
            end if;
        end loop;
       */
        p := p2 + v_delimlen;
        i := i + 1;
    end loop;

    return v_result;
end;

/**
 * 分隔字符串自定义函数处理方式
 * s_str:要被分割的字符串
 * split_str:分隔符
 * 使用方法：
 *       (1).create or replace type t_vc is table of varchar2(100);  --要不怕占内存，也可以整大点
 *       (2).select column_value from table(cast(myconvert(s_str, split_str) as t_vc))  order by 1 nulls first;
 * 如：select column_value from table(cast(myconvert('a+b+b+d+e+f', '+') as t_vc))  order by 1 nulls first;
 * 得到的是：a b b d e f
 */
/

prompt
prompt Creating procedure PO_PRODUCTINFO_EDIT
prompt ======================================
prompt
CREATE OR REPLACE PROCEDURE HF.PO_PRODUCTINFO_EDIT (
                                                    -- v_productname varchar2,--产品名字
                                                    v_ispid       NUMBER, --运营商标识 0：中国联通1：中国移动
                                                    v_delaytime   NUMBER, --到帐时间，单位为分钟
                                                    v_provinceids VARCHAR2, --商品省份ID
                                                    v_cardmoneys  VARCHAR2, --所选面值，以“，”隔开
                                                    v_citycodes   VARCHAR2, --地区代码
                                                    v_Type        NUMBER, --操作类型 0：添加 1：修改 2:删除
                                                    v_productid   NUMBER, --产品ID
                                                    v_productType NUMBER, --商品类型
                                                    v_Result      OUT VARCHAR2,
                                                    v_success     OUT VARCHAR2) AS
  /************************************************************
  产品名称：新话费充值系统
  模块名称：数据库脚本-话费商品信息的添加\修改\删除
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  创建人员：wangjiang
  创建日期：2008-11-28
  功能描述：话费商品信息的添加\修改\删除,添加时，
            省份和地市都是单选，
            则循环添加除省ID不同其它属性都相同的商品记录
  ************************************************************/
  l_provinceid     VARCHAR2(10);
  l_curr_cardmoney VARCHAR2(10); --循环时用到的面值

  l_product_name_temp  VARCHAR2(100); --商品名称
  l_product_name       VARCHAR2(200); --商品名称
  l_temp_name          VARCHAR2(50);
  l_temp_name1         VARCHAR2(50);
  l_productType        VARCHAR2(50);
  l_delaytime          VARCHAR2(20);
  l_productid          NUMBER;
  l_productid_temp     VARCHAR2(2);
  l_prod_paytype       NUMBER;
  l_citycode_name      VARCHAR2(20);
  l_citycode           VARCHAR2(10);
  l_count              NUMBER;
  l_success            NUMBER;
  l_city_exists        BOOLEAN;
  l_is_cardamountValid BOOLEAN;
  l_idx                NUMBER;
  l_delflag            NUMBER;
  CURSOR CUR_cardAmount IS --面值列表
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_cardmoneys, ',') AS t_vc))
     WHERE COLUMN_VALUE IS NOT NULL;
  CURSOR CUR_cityCode(CITY VARCHAR2) IS --地市列表
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(CITY, ',') AS t_vc))
     WHERE COLUMN_VALUE IS NOT NULL;
BEGIN
  v_Result             := '00000';
  l_success            := 0;
  l_count              := 0;
  l_is_cardamountValid := TRUE;

  SELECT DECODE(v_productType,
                NULL,
                '',
                0,
                '手机',
                1,
                '固话',
                2,
                '小灵通',
                '宽带')
    INTO l_productType
    FROM dual;

  IF v_Type = 0 THEN
    --添加
    SELECT DECODE(v_ispid, 0, '联通', 1, '移动', 2, '电信', v_ispid)
      INTO l_temp_name1
      FROM dual;
    SELECT DECODE(v_delaytime,
                  5,
                  '5分钟实时充值',
                  1440,
                  '24小时充值',
                  2880,
                  '48小时充值',
                  120,
                  '2小时到账',
                  999,
                  '隔天9点到帐',
                  v_delaytime)
      INTO l_delaytime
      FROM dual;
    SELECT province_name
      INTO l_temp_name
      FROM sys_province
     WHERE province_id = v_provinceids;
    l_product_name_temp := l_temp_name; --|| l_temp_name1;
    l_provinceid        := '';
    l_city_exists       := FALSE;
    IF (v_citycodes IS NULL) THEN
      OPEN CUR_cityCode('00');
    ELSE
      OPEN CUR_cityCode(v_citycodes);
    END IF;
    LOOP
      FETCH CUR_cityCode
        INTO l_citycode;
      EXIT WHEN CUR_cityCode%NOTFOUND;
      IF (l_citycode <> '00') THEN
        l_idx := 0;
        SELECT COUNT(*)
          INTO l_idx
          FROM sys_CITY
         WHERE city_code = l_citycode;
        IF (l_idx > 0) THEN
          SELECT province_id, city_name
            INTO l_productid_temp, l_citycode_name
            FROM sys_CITY
           WHERE city_code = l_citycode and rownum = 1;
        END IF;
        IF (l_productid_temp = v_provinceids) THEN
          l_city_exists := TRUE;
        END IF;
      ELSE
        l_city_exists := TRUE;
      END IF;
      IF l_city_exists = TRUE THEN
        OPEN CUR_cardAmount;
        LOOP
          FETCH CUR_cardAmount
            INTO l_curr_cardmoney;
          EXIT WHEN CUR_cardAmount%NOTFOUND;
          BEGIN
            SELECT TO_NUMBER(l_curr_cardmoney)
              INTO l_curr_cardmoney
              FROM dual;
            l_is_cardamountValid := TRUE;
          EXCEPTION
            WHEN OTHERS THEN
              l_is_cardamountValid := FALSE;
          END;
          IF l_is_cardamountValid = TRUE THEN
            SELECT NVL(COUNT(*), 0)
              INTO l_count
              FROM po_productinfo t
             WHERE t.product_ispid = v_ispid
               AND t.product_provinceid = v_provinceids
               AND t.product_citycode = l_citycode
               AND t.product_delaytime = v_delaytime
               AND t.product_content = l_curr_cardmoney
               AND t.product_type = v_productType;
            IF l_count = 0 THEN
              l_product_name := l_product_name_temp || l_citycode_name ||
                                l_temp_name1 || l_productType ||
                                l_curr_cardmoney || '元' || l_delaytime;
              SELECT Seq_ProductID.NEXTVAL INTO l_productid FROM dual;
              INSERT INTO po_productinfo
                (product_id,
                 product_name,
                 product_content,
                 PRODUCT_MINMONEY,
                 PRODUCT_MAXMONEY,
                 product_ispid,
                 product_delaytime,
                 product_provinceid,
                 product_citycode,
                 PRODUCT_TYPE)
              VALUES
                (l_productid,
                 l_product_name,
                 l_curr_cardmoney,
                 l_curr_cardmoney,
                 l_curr_cardmoney,
                 v_ispid,
                 v_delaytime,
                 TO_NUMBER(v_provinceids),
                 l_citycode,
                 v_productType);
              --在dealer用户下增加商品信息
              IF v_delaytime = 5 THEN
                l_prod_paytype := 1;
              ELSE
                l_prod_paytype := 0;
              END IF;
              IF LENGTH(v_provinceids) < 2 THEN
                l_provinceid := '0' || v_provinceids;
              ELSE
                l_provinceid := '' || v_provinceids;
              END IF;

              l_success := l_success + 1;
            END IF;
          END IF;
        END LOOP;
        CLOSE CUR_cardAmount;
      END IF;
    END LOOP;
    CLOSE CUR_cityCode;

  ELSIF v_Type = 1 THEN
    IF v_productid IS NULL OR v_ispid IS NULL OR v_delaytime IS NULL OR
       v_provinceids IS NULL OR v_cardmoneys IS NULL OR v_citycodes IS NULL OR
       v_Type IS NULL THEN
      v_Result := '00002';
      ROLLBACK;
      RETURN;
    END IF;
    l_citycode_name := '';
    IF v_citycodes <> '00' THEN
      SELECT COUNT(0)
        INTO l_count
        FROM sys_CITY
       WHERE city_code = v_citycodes
         AND province_id = v_provinceids;
      IF l_count <> 1 THEN
        v_Result := '00001';
        ROLLBACK;
        RETURN;
      END IF;

      SELECT city_name
        INTO l_citycode_name
        FROM sys_CITY
       WHERE city_code = v_citycodes
         AND province_id = v_provinceids;

    END IF;

    SELECT c.PROVINCE_NAME || l_citycode_name ||
           DECODE(v_ispid, 1, '移动', 0, '联通', 2, '电信', v_ispid) ||
           l_productType ||
           DECODE(SIGN(v_delaytime - 60),
                  -1,
                  v_delaytime || '分钟',
                  0,
                  1 || '小时',
                  (v_delaytime / 60) || '小时') ||
           DECODE(TO_NUMBER(v_cardmoneys),
                  0,
                  '不定额',
                  TO_NUMBER(v_cardmoneys) || '元') || '充值'
      INTO l_product_name
      FROM sys_province c
     WHERE c.PROVINCE_ID = v_provinceids;

    SELECT h.PRODUCT_CONTENT
      INTO l_idx
      FROM po_PRODUCTINFO h
     WHERE h.PRODUCT_ID = v_productid;

    UPDATE po_PRODUCTINFO h
       SET h.PRODUCT_CITYCODE   = v_citycodes,
           h.PRODUCT_CONTENT    = v_cardmoneys,
           h.PRODUCT_DELAYTIME  = v_delaytime,
           h.PRODUCT_ISPID      = v_ispid,
           h.PRODUCT_MAXMONEY   = v_cardmoneys,
           h.PRODUCT_MINMONEY   = v_cardmoneys,
           h.PRODUCT_NAME       = l_product_name,
           h.PRODUCT_PROVINCEID = v_provinceids,
           h.product_type       = v_productType
     WHERE h.PRODUCT_ID = v_productid;

    IF l_idx <> v_cardmoneys THEN
      UPDATE po_PRODUCTONLINEINFO h
         SET h.PRODUCT_PRICE = l_idx
       WHERE h.PRODUCT_ID = v_productid;
    END IF;
  ELSIF v_Type = 2 THEN
    IF v_productid IS NULL THEN
      v_Result := '00002';
      ROLLBACK;
      RETURN;
    END IF;
    select count(1) delCount into  l_delflag from po_PRODUCTONLINEINFO h where h.PRODUCT_ID in(v_productid);
    if l_delflag > 0 then
      v_Result := '00003';
      return;
    else
       DELETE po_PRODUCTINFO h WHERE h.PRODUCT_ID in(v_productid);
       v_Result := '00000';
    end if;
    --return;
  END IF;
  IF (v_Result <> '00000') THEN
    ROLLBACK;
  ELSE
    v_success := l_success;
    COMMIT;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    v_Result := '11111';
    ROLLBACK;
END po_PRODUCTINFO_EDIT;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_AUDITING
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.PO_PRODUCTONLINE_AUDITING(v_opttype           in VARCHAR2, --操作类型
                                                      v_auditing_status   in number, --审核状态
                                                      v_auditing_username in VARCHAR2, --审核人
                                                      v_manids            in VARCHAR2, --审核id集合
                                                      v_applytype         in varchar2, --申请类型
                                                      v_result            OUT VARCHAR2) AS
  /**************************************************************************************
  产品名称：支付网关+话费系统3.0
  模块名称：数据库脚本-新手机充值-->货架信息管理
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  创建人员：weisd
  创建日期：2011-07-12
  功能描述：
             货架审核操作
             v_opttype：1 修改信息审批
             应该确保v_applytype 是同一个，每批次，不同的申请类型对应不同的操作实现
  **************************************************************************************/
  l_manId                number; --manId
  l_user_id              VARCHAR2(30);
  l_product_schanneltype NUMBER(1);
  l_product_splitflag    NUMBER(1);
  l_product_waittime     NUMBER(10);
  l_product_delaytime    NUMBER(10);
  l_product_status       NUMBER(1);
  l_product_discount     NUMBER(10, 3);
  l_product_price        NUMBER(10, 2);
  l_online_id            NUMBER(10);
  l_product_id           NUMBER(10);
  l_order_source         NUMBER(10);
  l_auditing_status      NUMBER(1);
  l_count_man            number(10);

  CURSOR cur_manids IS
    SELECT COLUMN_VALUE FROM TABLE(CAST(Myconvert(v_manids, ',') AS T_VC));

  CURSOR cur_details(manId number) IS --定义明细游标
    SELECT nvl(m.user_id, d.old_user_id),
           nvl(m.schanneltype, d.old_schanneltype),
           nvl(m.splitflag, d.old_splitflag),
           nvl(m.waittime, d.old_waittime),
           nvl(m.delaytime, d.old_delaytime),
           nvl(m.status, d.old_status),
           nvl(m.discount, d.old_discount),
           to_char(nvl(p.product_content * m.discount / 100, d.old_price),
                   'FM999999990.0099'),
           d.online_id,
           d.product_id,
           d.order_source
      FROM PO_AUDITING_MAIN m, PO_AUDITING_DETAIL d, PO_PRODUCTINFO p
     WHERE m.man_id = d.man_id
       and d.product_id = p.product_id
       and m.man_id = manId;

BEGIN
  v_result := '11111';
  IF v_opttype = '1' THEN
  
    if v_auditing_status is null or v_auditing_status is null or
       v_applytype is null then
      v_result := '11111';
      return;
    end if;
  
    OPEN cur_manids;
    LOOP
      FETCH cur_manids
        INTO l_manId;
      EXIT WHEN cur_manids%NOTFOUND;
      IF (l_manId IS NOT NULL) THEN
      
        select count(1)
          into l_count_man
          from PO_AUDITING_MAIN t
         where t.man_id = l_manId;
      
        if l_count_man > 0 then
        
          select t.auditing_status
            into l_auditing_status
            from PO_AUDITING_MAIN t
           where t.man_id = l_manId;
        
          if l_auditing_status = 0 then
            -- 修改main表审核状态
            update PO_AUDITING_MAIN t
               set t.AUDITING_STATUS   = v_auditing_status,
                   t.AUDITING_USERNAME = v_auditing_username,
                   t.AUDITING_TIME     = sysdate
             where t.man_id = l_manId
               and t.apply_type = v_applytype;
          
            if v_auditing_status = 1 then
              --打开明细游标
              OPEN cur_details(l_manId);
              LOOP
                FETCH cur_details
                  INTO l_user_id, l_product_schanneltype, l_product_splitflag, l_product_waittime, l_product_delaytime, l_product_status, l_product_discount, l_product_price, l_online_id, l_product_id, l_order_source;
                EXIT WHEN cur_details%NOTFOUND;
                --更新货架表详情
                update PO_PRODUCTONLINEINFO t
                   set t.user_id              = l_user_id,
                       t.product_waittime     = l_product_waittime,
                       t.product_delaytime    = l_product_delaytime,
                       t.product_splitflag    = l_product_splitflag,
                       t.product_schanneltype = l_product_schanneltype,
                       t.product_discount     = l_product_discount,
                       t.product_price        = l_product_price,
                       t.product_status       = l_product_status
                 where t.online_id = l_online_id;
              END LOOP;
              CLOSE cur_details;
            end if;
          end if;
        
        end if;
      
      END IF;
    END LOOP;
    CLOSE cur_manids;
  
  ELSE
  
    v_result := '11111';
    ROLLBACK;
    return;
  
  END IF;
  v_result := '00000';
  COMMIT;

EXCEPTION
  WHEN OTHERS THEN
    v_result := '11111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_AUTO
prompt ========================================
prompt
create or replace procedure hf.po_productonline_auto(
           v_id varchar2,   --id 序号，以','分割
           v_reqOrderSource number,--订单来源
           v_reqUserid varchar2,   --代理商
           v_productIsptype number,--运营商
           v_productProvinceid number,
           v_productMoney varchar2, --面值
           v_productDelaytime number,
           v_productRate number,
           v_adjustType number,
           v_begindate varchar2,
           v_enddate varchar2,
           v_type number, --操作类型 0：添加 1：修改 2:删除
           v_Result out varchar2
          ) as
/*******************************************************
  产品名称：充值系统
  模块名称：数据库脚本-自动调整货架配置的添加\修改\删除
  模块版本：1.0.0.0
  编译环境：ORACLE10g
  创建人员：zhouwx
  创建日期：2011-07-1
  功能描述：话费商品信息的添加\修改\删除,添加时
            面值可以为'50,100' 等
            代理商可以为'001,002'等
            其他则为单选

*******************************************************/
  l_curr_cardmoney varchar2(10); --循环时用到的面值
  l_curr_userid varchar2(30);
  l_is_cardamountValid BOOLEAN;
  l_id number;
  l_id_count number;
  CURSOR CUR_cardAmount IS --面值列表
     SELECT COLUMN_VALUE
     FROM TABLE(CAST(Myconvert(v_productMoney, ',') AS t_vc))
     WHERE COLUMN_VALUE IS NOT NULL;
   CURSOR cur_userid is --代理商列表
     select COLUMN_VALUE 
     FROM TABLE(CAST(Myconvert(v_reqUserid, ',') AS t_vc))
     WHERE COLUMN_VALUE IS NOT NULL;
  begin
     v_Result := '00000';
     l_is_cardamountValid := TRUE;
  if v_type = 0 THEN
  begin
     if (v_reqOrderSource is null or v_productRate is null or v_adjustType is null or v_begindate is null or v_enddate is null or v_type is null) then
        v_Result := '00002';
        return;
     else   
        open cur_userid;
        loop
          fetch cur_userid
             into l_curr_userid;     
             exit when  cur_userid%NOTFOUND;
          begin
               open CUR_cardAmount;
               loop
                 FETCH CUR_cardAmount
                    INTO l_curr_cardmoney;
                      EXIT WHEN CUR_cardAmount%NOTFOUND;
                    begin
                          SELECT TO_NUMBER(l_curr_cardmoney)
                                 INTO l_curr_cardmoney FROM dual;
                          l_is_cardamountValid := TRUE;
                    EXCEPTION
                           WHEN OTHERS THEN
                           l_is_cardamountValid := FALSE;
                    end;
                    if l_is_cardamountValid = true then
                       select seq_rule_autochangeid.nextval into  l_id from dual;
                       insert into po_rule_autochange
                       (
                              id,
                              req_ordersource,
                              req_userid,
                              product_isptype,
                              product_provinceid,
                              product_money,
                              product_delaytime,
                              product_rate,
                              adjust_type,
                              beginday,
                              endday
                       )
                       values(
                             l_id,
                             v_reqOrderSource,
                             l_curr_userid,
                             v_productIsptype,
                             v_productProvinceid,
                             l_curr_cardmoney,
                             v_productDelaytime,
                             v_productRate,
                             v_adjustType,
                             v_begindate,
                             v_enddate
                       );
                       v_Result := '00000';   
                    end if;
               end loop;
               close CUR_cardAmount;
             end;
          end loop;    
        close cur_userid;
    end if;
   end;
   elsif v_type = 2 then
     begin
       if v_id > 0 then
          select count(1) into l_id_count from po_rule_autochange where id in(v_id);
          if l_id_count > 0 then
             delete from po_rule_autochange where id in(v_id);
             v_Result := '00000';
          else
             v_Result := '00003';
          end if;   
       end if;
     end;
  end if;
  
  if v_Result = '00000' then 
        commit;
        return;
  else
        rollback;
        return;   
  end if;
   Exception 
      when others then
           v_Result := '00001';
           rollback;
           return;
end po_productonline_auto;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_CITY
prompt ========================================
prompt
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_CITY" (v_ispId            VARCHAR2, --运营商
                                                    v_orderSource      VARCHAR2, --订单来源
                                                    v_provinceId       VARCHAR2, --省份
                                                    v_codeCitys        VARCHAR2, --城市 （以，号隔开）
                                                    v_userId           VARCHAR2, --用户Id
                                                    v_cardMoney        VARCHAR2, --录入面值
                                                    v_schannelType     VARCHAR2, --优先方式
                                                    v_ratio            VARCHAR2, --折扣率
                                                    v_splitFlag        VARCHAR2, --是否分次
                                                    v_waitTime         VARCHAR2, --初始等待时长
                                                    v_productDelaytime VARCHAR2, --商品到帐时长（商品表中的）
                                                    v_onlineDelaytime  VARCHAR2, --允许到帐时长
                                                    v_status           VARCHAR2, --商品状态
                                                    v_productType      VARCHAR2, --商品业务类型
                                                    v_orderIndex       VARCHAR2, --优先级
                                                    v_oper             VARCHAR2, --操作人
                                                    v_result           OUT VARCHAR2, --执行结果
                                                    v_insertNum        OUT VARCHAR2 --添加货架的行数
                                                    ) AS
  /************************************************************
  产品名称：新话费充值系统2.0
  模块名称：数据库脚本-商品货架信息的添加\修改\删除
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  创建人员：wangjiang
  创建日期：2008-11-26
  功能描述：商品货架信息的添加_地区代码循环
            v2 wiesd 2011-08-08 l_curr_codecity 添加rownum = 1
  ************************************************************/
  l_idx             NUMBER;
  l_idx1            NUMBER;
  l_curr_codecity   VARCHAR2(20);
  l_productid_temp  VARCHAR2(2);
  l_productId       NUMBER;
  l_product_content NUMBER(6, 2);
  l_product_price   NUMBER(6, 2);
  temp_res          VARCHAR(10);
  v_count           number;
  CURSOR cur_codeCitys IS
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_codeCitys, ',') AS T_VC))
     WHERE COLUMN_VALUE IS NOT NULL;

  CURSOR c_products(p_ispid NUMBER, p_provinceid NUMBER, p_cardmoney NUMBER, p_delaytime NUMBER, p_codecity VARCHAR) IS --定义商品游标
    SELECT product_id, product_content
      FROM po_PRODUCTINFO t
     WHERE product_ispid = p_ispid
       AND product_provinceid = p_provinceid
       AND product_content = p_cardmoney
       AND product_delaytime = p_delaytime
       AND t.product_citycode = p_codecity
       AND to_char(t.product_type) in ( SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_productType, ',') AS T_VC))
     WHERE COLUMN_VALUE IS NOT NULL);
BEGIN
  v_result := '11111';
  l_idx    := 0;
  l_idx1   := 0;
  OPEN cur_codeCitys;
  LOOP
    FETCH cur_codeCitys
      INTO l_curr_codecity;
    EXIT WHEN cur_codeCitys%NOTFOUND;
    IF (l_curr_codecity IS NOT NULL) THEN
      --此处缺少判断当前地区是否属于当前省的代码
      --如果不属于当前省，应跳过循环
      --if (l_curr_codecity<>'00') then
      --end if;
      IF l_curr_codecity <> '00' THEN
        SELECT province_id
          INTO l_productid_temp
          FROM sys_CITY
         WHERE city_code = l_curr_codecity and rownum = 1 ;
      END IF;
      IF (l_curr_codecity = '00' OR l_productid_temp = v_provinceId) THEN
        OPEN c_products(v_ispId,
                        TO_NUMBER(v_provinceId),
                        TO_NUMBER(v_cardMoney),
                        v_productDelaytime,
                        l_curr_codecity);
        LOOP
          FETCH c_products
            INTO l_productId, l_product_content;
          EXIT WHEN c_products%NOTFOUND;
          l_idx1 := 0;

         -- 注释by 
          SELECT COUNT(*)
            INTO l_idx1
            FROM PO_PRODUCTONLINEINFO t
           WHERE t.order_source = v_orderSource
             AND t.user_id = v_userId
             AND t.product_id = l_productId;
             SELECT COUNT(*)
            INTO v_count
            FROM po_PRODUCTONLINEINFO t
           WHERE t.order_source = v_orderSource
             AND t.user_id = v_userId
             AND t.product_id = l_productId;

          --dbms_output.put_line('l_idx1='||l_idx1||'|v_count='||v_count||'|v_orderSource='||v_orderSource||'|v_userId='||v_userId||'|l_productId='||l_productId||'|l_productId='||l_productId||'|v_cardMoney='||v_cardMoney||'|v_productDelaytime='||v_productDelaytime||'|l_curr_codecity='||l_curr_codecity);
          IF (l_idx1 <= 0) and v_count = 0 THEN
            --如果存在相同的记录，则不添加

            l_idx           := l_idx + 1;
            l_product_price := (l_product_content * v_ratio) / 100;

            INSERT INTO PO_PRODUCTONLINEINFO
              (
               ONLINE_ID,
               PRODUCT_ID,
               ORDER_SOURCE,
               USER_ID,
               PRODUCT_SCHANNELTYPE,
               PRODUCT_PRICE,
               PRODUCT_DISCOUNT,
               PRODUCT_SPLITFLAG,
               PRODUCT_WAITTIME,
               PRODUCT_DELAYTIME,
               PRODUCT_STATUS,
               ONLINE_ADUSTTYPE,
               ORDER_INDEX,
               OPER_USERID,
               CREATE_TIME
               )
            VALUES
              (
               Seq_onlineid.NEXTVAL,
               l_productId,
               v_orderSource,
               v_userId,
               v_schannelType,
               l_product_price,
               v_ratio,
               v_splitFlag,
               v_waitTime,
               v_onlineDelaytime,
               v_status,
               0,
               v_orderIndex,
               v_oper,
               sysdate
               );

          END IF;

        END LOOP; --结束循环游标
        CLOSE c_products;

      END IF;
    END IF;
  END LOOP;
  CLOSE cur_codeCitys;
  v_insertNum := l_idx;
  v_result    := '00000';

  IF (v_result <> '00000') THEN
    ROLLBACK;
  END IF;
  IF(temp_res = '11111') THEN
  ROLLBACK;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
  --DBMS_OUTPUT.PUT_LINE(SQLCODE||'：'||SQLERRM);
   v_result := '11111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_CARDMONEY
prompt =============================================
prompt
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_CARDMONEY" (v_ispId            VARCHAR2, --运营商
                                                         v_ordersource      VARCHAR2, --订单来源
                                                         v_provinceId       VARCHAR2, --省份
                                                         v_codeCitys        VARCHAR2, --城市 （以，号隔开）
                                                         v_userId           VARCHAR2, --用户Id
                                                         v_cardMoneys       VARCHAR2, --录入的多个面值（以，号隔开）
                                                         v_schannelType     VARCHAR2, --优先方式
                                                         v_ratio            VARCHAR2, --折扣率
                                                         v_splitFlag        VARCHAR2, --是否分次
                                                         v_waitTime         VARCHAR2, --初始等待时长
                                                         v_productDelaytime VARCHAR2, --商品到帐时长（商品表中的）
                                                         v_onlineDelaytime  VARCHAR2, --允许到帐时长
                                                         v_status           VARCHAR2, --商品状态
                                                         v_productType      VARCHAR2, --商品业务类型
                                                         v_orderIndex       VARCHAR2, --优先级
                                                         v_oper             VARCHAR2, --操作人
                                                         v_result           OUT VARCHAR2, --执行结果
                                                         v_insertNum        OUT VARCHAR2 --添加货架的行数
                                                         ) AS
  /************************************************************
  产品名称：新话费充值系统2.0
  模块名称：数据库脚本-商品货架信息的添加\修改\删除
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  创建人员：wangjiang
  创建日期：2008-11-26
  功能描述：商品货架信息的添加_面值循环
  ************************************************************/
  l_curr_cardMoney VARCHAR2(20); --代理商钱包ID
  l_insertNum      NUMBER;
  l_tempNum        NUMBER;
  l_orderIndex     NUMBER;
  l_productContentNum NUMBER;
  CURSOR cur_cardMoneys IS
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_cardMoneys, ',') AS T_VC))
     WHERE COLUMN_VALUE IS NOT NULL
     order by column_value;
BEGIN
  v_result    := '11111';
  l_insertNum := 0;
  l_orderIndex:= 0;
  SELECT count(*) into l_productContentNum
      FROM TABLE(CAST(Myconvert(v_cardMoneys, ',') AS T_VC))
     WHERE COLUMN_VALUE IS NOT NULL
     order by column_value;
  OPEN cur_cardMoneys;
  LOOP
    FETCH cur_cardMoneys
      INTO l_curr_cardMoney;
    EXIT WHEN cur_cardMoneys%NOTFOUND;
    IF (l_curr_cardMoney IS NOT NULL) THEN
      l_tempNum := 0;
      IF (l_productContentNum > 1)THEN
         l_orderIndex := l_orderIndex +1;
      END IF;
      IF (l_productContentNum = 1)THEN
         l_orderIndex := to_number(v_orderIndex);
      END IF;
      PO_Productonline_City(v_ispId, --运营商
                              v_ordersource, --订单来源
                              v_provinceId, --省份
                              v_codeCitys, --城市 （以，号隔开）
                              v_userId, --用户Id
                              l_curr_cardMoney, --录入面值
                              v_schannelType, --优先方式
                              v_ratio, --折扣率
                              v_splitFlag, --是否分次
                              v_waitTime, --初始等待时长
                              v_productDelaytime, --商品到帐时长（商品表中的）
                              v_onlineDelaytime, --允许到帐时长
                              v_status, --商品状态
                              v_productType, --商品业务类型
                              l_orderIndex,-- 优先级
                              v_oper,   --操作人
                              v_result, --执行结果
                              l_tempNum);
      l_insertNum := l_insertNum + l_tempNum;
    END IF;

  END LOOP;
  CLOSE cur_cardMoneys;
  v_insertNum := l_insertNum;
  IF (v_result <> '00000') THEN
    ROLLBACK;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    v_result := '11111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_DEALER
prompt ==========================================
prompt
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_DEALER" (v_ispId            VARCHAR2, --运营商
                                                      v_ordersource      VARCHAR2, --订单来源
                                                      v_provinceId       VARCHAR2, --省份
                                                      v_codeCitys        VARCHAR2, --城市 （以，号隔开）
                                                      v_userIds          VARCHAR2, --用户Id(代理商ID,以，号隔开)
                                                      v_cardMoneys       VARCHAR2, --录入的多个面值（以，号隔开）
                                                      v_schannelType     VARCHAR2, --优先方式
                                                      v_ratio            VARCHAR2, --折扣率
                                                      v_splitFlag        VARCHAR2, --是否分次
                                                      v_waitTime         VARCHAR2, --初始等待时长
                                                      v_productDelaytime VARCHAR2, --商品到帐时长（商品表中的）
                                                      v_onlineDelaytime  VARCHAR2, --允许到帐时长
                                                      v_status           VARCHAR2, --商品状态
                                                      v_productType      VARCHAR2, --商品业务类型
                                                      v_orderIndex       VARCHAR2, --优先级
                                                      v_applyUserName    VARCHAR2, --申请人 add by gengxj
                                                      v_result           OUT VARCHAR2, --执行结果
                                                      v_insertNum        OUT VARCHAR2 --添加货架的行数
                                                      ) AS
  /************************************************************
  产品名称：新话费充值系统2.0
  模块名称：数据库脚本-商品货架信息的添加\修改\删除
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  创建人员：wangjiang
  创建日期：2008-11-26
  功能描述：商品货架信息的添加_代理商循环
  ************************************************************/
  l_curr_userId  VARCHAR2(20); --循环时用到的代理商ID
  l_curr_purseId VARCHAR2(20); --代理商钱包ID
  l_idx          NUMBER;
  l_insertNum    NUMBER;
  l_tempNum      NUMBER;
  CURSOR cur_userIds IS
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_userIds, ',') AS T_VC))
     WHERE COLUMN_VALUE IS NOT NULL;
BEGIN
  v_result    := '11111';
  l_idx       := 0;
  l_insertNum := 0;
  OPEN cur_userIds;
  LOOP
    FETCH cur_userIds
      INTO l_curr_userId;
    EXIT WHEN cur_userIds%NOTFOUND;
    IF (l_curr_userId IS NOT NULL) THEN
        SELECT COUNT(*)
          INTO l_idx
          FROM SALE_DEALERINFO
         WHERE dealerid = l_curr_userId;
        IF (l_idx = 1) THEN 
          SELECT dealerid
            INTO l_curr_purseId
            FROM SALE_DEALERINFO
           WHERE dealerid = l_curr_userId;
          IF (l_curr_purseId IS NULL) THEN
            l_curr_purseId := l_curr_userId;
          END IF;
        ELSE
          l_curr_purseId := l_curr_userId;
        END IF;
      l_tempNum := 0;
      PO_PRODUCTONLINE_CARDMONEY(v_ispId, --运营商
                                   v_ordersource, --订单来源
                                   v_provinceId, --省份
                                   v_codeCitys, --城市 （以，号隔开）
                                   l_curr_purseId, --用户Id(代理商ID,以，号隔开)
                                   v_cardMoneys, --录入的多个面值（以，号隔开）
                                   v_schannelType, --优先方式
                                   v_ratio, --折扣率
                                   v_splitFlag, --是否分次
                                   v_waitTime, --初始等待时长
                                   v_productDelaytime, --商品到帐时长（商品表中的）
                                   v_onlineDelaytime, --允许到帐时长
                                   v_status, --商品状态
                                   v_productType, --商品业务类型
                                   v_orderIndex,--优先级
                                   v_applyUserName, --申请人 
                                   v_result, --执行结果
                                   l_tempNum);
    l_insertNum := l_insertNum + l_tempNum;
    END IF;

  END LOOP;
  CLOSE cur_userIds;
  v_insertNum := l_insertNum;
  IF (v_result <> '00000') THEN
    ROLLBACK;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    v_result := '11111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_EDIT
prompt ========================================
prompt
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_EDIT" (
                                                    v_op_type          VARCHAR2, --操作类型 0:添加 1:修改
                                                    v_onlineIds        VARCHAR2, --货架id(修改时用)
                                                    v_ispId            VARCHAR2, --运营商
                                                    v_orderSource      VARCHAR2, --订单来源
                                                    v_provinceId       VARCHAR2, --省份
                                                    v_codecitys        VARCHAR2, --城市 （以，号隔开）
                                                    v_userIds          VARCHAR2, --用户Id(代理商ID,以，号隔开)
                                                    v_cardMoneys       VARCHAR2, --录入的多个面值（以，号隔开）
                                                    v_schannelType     VARCHAR2, --优先方式
                                                    v_ratio            VARCHAR2, --折扣率
                                                    v_splitFlag        VARCHAR2, --是否分次
                                                    v_waitTime         VARCHAR2, --初始等待时长
                                                    v_productDelaytime VARCHAR2, --商品到帐时长（商品表中的）
                                                    v_onlineDelaytime  VARCHAR2, --允许到帐时长
                                                    v_status           VARCHAR2, --商品状态
                                                    v_productType      VARCHAR2, --商品业务类型
                                                    v_orderIndex       VARCHAR2, --优先级
                                                    v_applyUserName    VARCHAR2, --申请人   add by gengxj
                                                    v_result           OUT VARCHAR2, --执行结果
                                                    v_insertNum        OUT VARCHAR2 --添加货架的行数
                                                    ) AS

  /**********************************
  功能：货架批量修改涉及申请
  作者：
  时间：
        该存储过程现在只是涉及到【添加】
        v2 weisd 2011-08-09 l_codecitys 修改长度
  **********************************/

  l_userIds   VARCHAR2(200);
  l_codecitys VARCHAR2(4000);
  lv_pid      VARCHAR2(2);
  CURSOR cur_provinceIds IS
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_provinceId, ',') AS T_VC))
     WHERE COLUMN_VALUE IS NOT NULL;

BEGIN
  v_result    := '11111';
  v_insertNum := 0;
  IF v_op_type = '0' THEN
    IF (v_cardMoneys IS NULL) THEN
      RETURN;
    END IF;
    --用户id部分
    IF (v_userIds IS NULL) THEN
      --不填则为'ALL'
      l_userIds := 'ALL';
    ELSE
      l_userIds := UPPER(v_userIds);
    END IF;
    --地市部分
    IF (v_codecitys IS NULL) THEN
      --不填则为'00'
      l_codecitys := '00';
    ELSE
      l_codecitys := v_codecitys;
    END IF;
 OPEN cur_provinceIds ;
 LOOP
   FETCH cur_provinceIds INTO lv_pid ;
   EXIT WHEN cur_provinceIds%NOTFOUND;

    PO_PRODUCTONLINE_DEALER(v_ispId, --运营商
                              v_ordersource, --订单来源
                              lv_pid, --省份
                              l_codecitys, --城市 （以，号隔开）
                              l_userIds, --用户Id(代理商ID,以，号隔开)
                              v_cardMoneys, --录入的多个面值（以，号隔开）
                              v_schannelType, --优先方式
                              v_ratio, --折扣率
                              v_splitFlag, --是否分次
                              v_waitTime, --初始等待时长
                              v_productDelaytime, --商品到帐时长（商品表中的）
                              v_onlineDelaytime, --允许到帐时长
                              v_status, --商品状态
                              v_productType , --商品业务类型
                              v_orderIndex,--优先级
                              v_applyUserName,      --申请人 add by gengxj
                              v_result, --执行结果
                              v_insertNum --添加货架的行数
                              );
 END LOOP;
 CLOSE cur_provinceIds;
  END IF;
  IF (v_result <> '00000') THEN
    ROLLBACK;
  ELSE
    COMMIT;
  END IF;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      V_RESULT := '11111';
END;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_UPDATE
prompt ==========================================
prompt
CREATE OR REPLACE PROCEDURE HF.PO_PRODUCTONLINE_UPDATE(v_opttype      VARCHAR2, --操作类型 0 状态修改 1 修改其它公共修改  2 删除
                                                      v_onlineids    VARCHAR2, --要修改的所有货架id，以”，“隔开
                                                      v_status       VARCHAR2, --货架状态 0上架 1下架 2暂停
                                                      v_delaytime    VARCHAR2, --允许到帐时长
                                                      v_splitflag    VARCHAR2, --是否支持分次0：允许 1：禁止
                                                      v_schanneltype VARCHAR2, --渠道优先原则，1：按处理速度，2：按渠道成本，3：分流比例
                                                      v_ratio        VARCHAR2, --折扣率
                                                      v_userid       VARCHAR2, --用户ID，如果某商品需要对特定的用户发布不同的价格，则此处有值，不区分用户则填ALL
                                                      v_remark       VARCHAR2, --操作备注
                                                      v_waittime     VARCHAR2, --等待处理时长
                                                      v_orderindex   VARCHAR2, --优先级
                                                      v_lockstatus   VARCHAR2, --锁定解锁
                                                      v_opter        VARCHAR2, --操作人
                                                      v_result       OUT VARCHAR2 --,
                                                      ) AS
  /**************************************************************************************
  产品名称：支付网关+话费系统3.0
  模块名称：数据库脚本-新手机充值-->货架信息管理
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  创建人员：weisd
  创建日期：2011-07-11
  功能描述：
            合并货架几个修改操作并记录日志到审核表，
            信息记录到审核表并不一定是要审核，某些只是为了记录日志信息
            根据  v_opttype 进行区分：
             0:状态修改
             1:修改其它公共修改(不需审核)
             2:货架信息修改需要审核的，如折扣率由改变
             3:锁定和解锁
  **************************************************************************************/
  l_onlineid       VARCHAR2(20);
  l_product_status varchar2(10);
  l_manId          number;

  CURSOR cur_onlineids IS
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_onlineids, ',') AS T_VC));
BEGIN
  v_result := '00000';
  IF v_opttype = '0' THEN
    -- 插入审核记录表，但是这里不用审核信息,状态使用同一
    select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
    insert into po_auditing_main
      (man_id,
       status,
       apply_username,
       apply_time,
       auditing_username,
       auditing_time,
       remark,
       apply_type,
       auditing_status)
    values
      (l_manId,
       v_status,
       v_opter,
       sysdate,
       'system',
       sysdate,
       '货架上下架状态修改',
       0,
       1);
  
    insert into po_auditing_detail
      (pk_id, man_id, online_id, product_id, order_source, old_status)
      select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
             l_manId,
             p.online_id,
             p.product_id,
             p.order_source,
             p.product_status
        from PO_PRODUCTONLINEINFO p
       where exists (select column_value
                from table(Myconvert(v_onlineids, ','))
               where column_value = p.online_id);
  
    --状态修改(即货架的上下架)
    UPDATE PO_PRODUCTONLINEINFO t
       SET t.product_status = v_status
     WHERE t.online_id IN (SELECT COLUMN_VALUE
                             FROM TABLE(CAST(Myconvert(v_onlineids, ',') AS t_vc))
                            WHERE COLUMN_VALUE IS NOT NULL);
  
  ELSIF v_opttype = '1' THEN
  
    -- 插入审核记录表，但是这里不用审核信息,状态使用同一
    select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
    insert into po_auditing_main
      (man_id,
       user_id,
       schanneltype,
       discount,
       splitflag,
       waittime,
       delaytime,
       status,
       apply_time,
       remark,
       apply_username,
       auditing_username,
       auditing_time,
       apply_type,
       auditing_status)
    values
      (l_manId,
       v_userid,
       v_schanneltype,
       v_ratio,
       v_splitflag,
       v_waittime,
       v_delaytime,
       l_product_status,
       sysdate,
       '货架修改,不用审批', --v_remark,
       v_opter,
       'system',
       sysdate,
       1,
       1);
  
    OPEN cur_onlineids;
    LOOP
      FETCH cur_onlineids
        INTO l_onlineid;
       EXIT WHEN cur_onlineids%NOTFOUND;
      IF (l_onlineid IS NOT NULL) THEN
      
        insert into po_auditing_detail
          (pk_id,
           man_id,
           online_id,
           product_id,
           order_source,
           old_user_id,
           old_schanneltype,
           old_price,
           old_discount,
           old_splitflag,
           old_waittime,
           old_delaytime,
           old_status)
          select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
                 l_manId,
                 p.online_id,
                 p.product_id,
                 p.order_source,
                 p.user_id,
                 p.product_schanneltype,
                 p.product_price,
                 p.product_discount,
                 p.product_splitflag,
                 p.product_waittime,
                 p.product_delaytime,
                 p.product_status
            from PO_PRODUCTONLINEINFO p
           where p.online_id = l_onlineid;
      
        UPDATE PO_PRODUCTONLINEINFO t
           SET t.product_schanneltype = DECODE(v_schanneltype,
                                               NULL,
                                               t.product_schanneltype,
                                               '',
                                               t.product_schanneltype,
                                               v_schanneltype),
               --t.product_price        = l_price,
               t.PRODUCT_WAITTIME  = DECODE(v_waittime,
                                            NULL,
                                            t.PRODUCT_WAITTIME,
                                            v_waittime),
               t.product_splitflag = DECODE(v_splitflag,
                                            NULL,
                                            t.product_splitflag,
                                            '',
                                            t.product_splitflag,
                                            v_splitflag),
               t.product_delaytime = DECODE(v_delaytime,
                                            NULL,
                                            t.product_delaytime,
                                            '',
                                            t.product_delaytime,
                                            v_delaytime),
               t.USER_ID           = DECODE(v_userid,
                                            NULL,
                                            t.USER_ID,
                                            '',
                                            t.USER_ID,
                                            v_userid),
               t.order_index       = DECODE(v_orderindex,
                                            NULL,
                                            t.order_index,
                                            '',
                                            t.order_index,
                                            v_orderindex)
         WHERE t.online_id = l_onlineid;
      END IF;
      --EXIT WHEN cur_onlineids%NOTFOUND;
    END LOOP;
    CLOSE cur_onlineids;
  
  ELSIF v_opttype = '2' THEN
  
    /*  if v_flag = '1' then
      n_product_status := null;
    else
      n_product_status := v_product_status;
    end if;*/
  
    l_product_status := null; --状态不修改
  
    --记录货架修改申请日志
    select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
    insert into po_auditing_main
      (man_id,
       user_id,
       schanneltype,
       discount,
       splitflag,
       waittime,
       delaytime,
       status,
       apply_time,
       remark,
       apply_username,
       apply_type,
       auditing_status)
    values
      (l_manId,
       v_userid,
       v_schanneltype,
       v_ratio,
       v_splitflag,
       v_waittime,
       v_delaytime,
       l_product_status,
       sysdate,
       '货架修改,申请', --v_remark,
       v_opter,
       1,
       0);
  
    insert into po_auditing_detail
      (pk_id,
       man_id,
       online_id,
       product_id,
       order_source,
       old_user_id,
       old_schanneltype,
       old_price,
       old_discount,
       old_splitflag,
       old_waittime,
       old_delaytime,
       old_status)
      select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
             l_manId,
             p.online_id,
             p.product_id,
             p.order_source,
             p.user_id,
             p.product_schanneltype,
             p.product_price,
             p.product_discount,
             p.product_splitflag,
             p.product_waittime,
             p.product_delaytime,
             p.product_status
        from PO_PRODUCTONLINEINFO p, PO_PRODUCTINFO pp
       where p.product_id(+) = pp.product_id
         and p.is_locked = 0
         and p.online_id in
             (select * from table(Myconvert(v_onlineids, ',')));
  
  ELSIF v_opttype = '3' THEN
    --锁定解锁
  
    if v_lockstatus is null then
      v_result := '11111';
      ROLLBACK;
    end if;
  
    update PO_PRODUCTONLINEINFO p
       set p.is_locked = to_number(v_lockstatus)
     where exists (select 1
              from table(Myconvert(v_onlineids, ','))
             where column_value = p.online_id);
  
  END IF;
  v_result := '00000';
  COMMIT;

EXCEPTION
  WHEN OTHERS THEN
    v_result := '11111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure SP_SYS_DBERRORINFO_CREATE
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF."SP_SYS_DBERRORINFO_CREATE"
(
       i_Level          NUMBER,
       i_Owner          VARCHAR2,
       i_OraErr         VARCHAR2,
       i_Info1          VARCHAR2,
       i_Info2          VARCHAR2,
       i_Info3          VARCHAR2
)
AS
        l_Level         NUMBER;
        l_Owner         VARCHAR2(50);
        l_OraErr        VARCHAR2(255);
        l_Info1         VARCHAR2(255);
        l_Info2         VARCHAR2(255);
        l_Info3         VARCHAR2(255);

    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
        l_Level := 0;
        l_Owner :='unknow';
        l_OraErr:='unknow';
        l_Info1 :='unknow';
        l_Info2 :='unknow';
        l_Info3 :='unknow';


        if i_Level is not null then
           l_Level := i_Level;
        end if;

        if i_Owner is not null then
           l_Owner := i_Owner;
        end if;

        if i_OraErr is not null then
           l_OraErr := i_OraErr;
        end if;

        if i_Info1 is not null then
           l_Info1 := i_Info1;
        end if;

        if i_Info2 is not null then
           l_Info2 := i_Info2;
        end if;

        if i_Info3 is not null then
           l_Info3 := i_Info3;
        end if;

        insert into sys_dberrorinfo values(SYSDATE,l_Level,l_Owner,l_OraErr,l_Info1,l_Info2,l_Info3);
        commit;

EXCEPTION WHEN OTHERS THEN
        Rollback;
END;
/

prompt
prompt Creating procedure SP_HF_GENERATEHFSERIAL
prompt =========================================
prompt
CREATE OR REPLACE PROCEDURE HF."SP_HF_GENERATEHFSERIAL"
(
    v_Result       OUT           VARCHAR2, --结果码
    v_HFserialid   OUT           VARCHAR2  --订单号
)
IS
/******************************************************************************
产品名称：话费充值系统
产品版本：V1a
模块名称：数据库脚本-生成话费充值流水
模块版本：1.0.0.0
编译环境：ORACLE9i，ORACLE10g
修改人员：wjjava
修改日期：2011-04-15
修改内容：
******************************************************************************/
    l_seqid        NUMBER;
    l_sysid        NUMBER;

BEGIN
    v_Result := '11111' ;
    v_HFserialid := '-1';
    /*
    SELECT sys_id
      INTO l_sysid
      FROM hf_syspalt WHERE rownum=1;
    */
    l_sysid := '01';--目前只有一个处理平台
    SELECT seq_hfid.nextval
      INTO l_seqid
      FROM dual;

    v_HFserialid := 'HF' || LPAD(TO_CHAR(l_sysid),2,'0') || TO_CHAR(SYSDATE,'YYMMDDHH24MISS') || LPAD(TO_CHAR(l_seqid),4,'0');
    v_Result := '00000' ;

EXCEPTION WHEN OTHERS THEN
    v_Result := '11111';
    SP_SYS_DBERRORINFO_CREATE (3,'Sp_HF_GenerateHFSerial',sqlerrm,NULL,NULL,NULL);
END;
/

prompt
prompt Creating procedure SP_HF_GENERATEORDERID
prompt ========================================
prompt
CREATE OR REPLACE PROCEDURE HF."SP_HF_GENERATEORDERID"
(
    v_Result       OUT           VARCHAR2, --结果码
    v_OrderID      OUT           VARCHAR2  --订单号
)
IS
/******************************************************************************
产品名称：话费充值系统
产品版本：V1a
模块名称：数据库脚本-生成订单号
模块版本：1.0.0.0
编译环境：ORACLE9i，ORACLE10g
修改人员：wjjava
修改日期：2011-04-15
修改内容：
******************************************************************************/
    l_seqid        NUMBER;
    l_sysid        NUMBER;

BEGIN
    v_Result := '11111' ;
    v_OrderID := '-1';
    /*
    SELECT sys_id
      INTO l_sysid
      FROM hf_syspalt WHERE rownum=1;
    */
    l_sysid := '01';--目前只有一个处理平台
    SELECT seq_orderid.nextval
      INTO l_seqid
      FROM dual;

    v_OrderID := 'JB' || LPAD(TO_CHAR(l_sysid),2,'0') || TO_CHAR(SYSDATE,'YYMMDDHH24MISS') || LPAD(TO_CHAR(l_seqid),4,'0');
    v_Result := '00000' ;

EXCEPTION WHEN OTHERS THEN
    v_Result := '11111';
    sp_sys_dberrorinfo_create(3,'Sp_HF_GenerateOrderID',sqlerrm,NULL,NULL,NULL);
END;
/

prompt
prompt Creating procedure SP_HF_GETCHANNEL
prompt ===================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_HF_GETCHANNEL(v_orderid     VARCHAR2, --订单号
                                             v_result      OUT VARCHAR2, --结果返回码
                                             v_channelid   OUT VARCHAR2, --渠道id
                                             v_stationtype OUT VARCHAR2, --站点类型
                                             v_fullmoney   OUT VARCHAR2 --充值金额
                                             
                                             )
/************************************************************
  产品名称：话费充值系统
  产品版本：V3a
  模块名称：数据库脚本-取充值渠道
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-05-06
  修改内容：
  ************************************************************/
 AS
  l_ordersource NUMBER;
  l_userid      varchar2(30);
  l_isptype     NUMBER;
  l_provinceid  NUMBER;
  l_citycode    VARCHAR2(10);
  l_fullmoney   NUMBER;
  l_reqmoney    NUMBER(10, 2); --订单金额

  l_splitflag    NUMBER;
  l_curpercent   NUMBER;
  l_schanneltype NUMBER;

  l_accnum      VARCHAR2(30);
  l_chargemoney NUMBER;
  l_noneedcount NUMBER;
  l_needcount   NUMBER;

  l_trycount NUMBER;

  l_channelstep     NUMBER;
  l_channelmaxmoney NUMBER;
  l_channelminmoney NUMBER;

  l_servicetype   NUMBER;
  l_finishmoney   NUMBER;
  l_asrunioncount NUMBER;
  l_swebcount     NUMBER;
  l_ykccount      NUMBER;

  l_nocardcount NUMBER; --不带卡渠道数量

  l_ryswcount NUMBER; --如意商务渠道开通数量
  l_ucswitch  NUMBER; --联通3a充值开关

BEGIN
  v_result        := '81508';
  v_channelid     := '-1';
  v_stationtype   := '-1';
  v_fullmoney     := '-1';
  l_trycount      := 0;
  l_reqmoney      := -1;
  l_asrunioncount := 0;
  l_swebcount     := 0;
  l_ykccount      := 0;
  l_nocardcount   := 0;
  l_ryswcount     := 0;
  l_ucswitch      := 0;

  --1、取订单信息
  -----------------------------------------------------------------------------------------
  SELECT req_ordersource,
         req_userid,
         req_ispid,
         req_provinceid,
         req_citycode,
         req_accnum,
         req_money - charge_finishmoney,
         product_splitflag,
         req_money,
         product_slchannel,
         req_servicetype,
         charge_finishmoney
    INTO l_ordersource,
         l_userid,
         l_isptype,
         l_provinceid,
         l_citycode,
         l_accnum,
         l_fullmoney,
         l_splitflag,
         l_reqmoney,
         l_schanneltype,
         l_servicetype,
         l_finishmoney
    FROM od_orderinfo o
   WHERE req_orderid = v_orderid;

  --带卡渠道充值次数
  SELECT COUNT(0)
    INTO l_needcount
    FROM od_fullnote f
   WHERE f.hf_orderid = v_orderid
     AND f.hf_acchannel IN (SELECT channel_id
                              FROM ch_channelinfo p
                             WHERE p.channel_needcard = 1
                               AND p.channel_type <> 0);

  --不带卡充值渠道符合的数量，条件同下面不同方式选取的渠道方式相同条件，速度，成本，分流等sql查询条件。
  SELECT COUNT(0)
    INTO l_nocardcount
    FROM ch_channelinfo cc
   WHERE channel_needcard = 0 --不带卡渠道
     AND channel_isp = l_isptype --支持运营商
     AND (channel_provinceid = 0 or
         l_provinceid in
         (select cp.channel_provinceid
             from ch_channelprovince cp
            where cp.channel_id = cc.channel_id)) --支持省份
     AND (channel_citycode = '-1' OR
         channel_citycode LIKE '%' || l_citycode || '%') --支持地市
     AND channel_status = 0 --渠道可用
     AND (cc.channel_limit_money = -1 or
         cc.channel_money <= cc.channel_limit_money) --交易量没有超限
     AND channel_id NOT IN --不是禁止走的渠道
         (SELECT DISTINCT channel_id
            FROM ch_notmatchchannel nm
           WHERE order_source = l_ordersource
             AND (user_id = '-1' OR user_id = l_userid))
     AND ( --支持面值
          (channel_step <> 0 AND
          MOD(l_fullmoney - channel_minmoney, channel_step) = 0 AND
          l_fullmoney >= cc.channel_minmoney AND
          l_fullmoney <= cc.channel_maxmoney) OR
          (channel_step = 0 AND
          l_fullmoney IN
          (SELECT DISTINCT money
              FROM ch_money
             WHERE channel_id = cc.channel_id)) OR --可分次
          (l_SplitFlag = 0 AND l_fullmoney >= 100) OR --可补充
          (l_SplitFlag = 2 AND l_fullmoney > 50 AND l_finishmoney > 0))
     AND EXISTS (SELECT *
            FROM ch_supporttbusiness
           WHERE channel_id = cc.channel_id
             AND service_type = l_servicetype);

  --<<不需要卡渠道>>
  --=====================================================================================
  <<NoCard_Label>>
  IF l_schanneltype = 1 THEN
    --速度优先
    BEGIN
      SELECT channel_id, channel_stationtype, channel_maxmoney, MinCount
        INTO v_channelid, v_stationtype, l_chargemoney, l_noneedcount
        FROM (SELECT channel_id,
                     channel_stationtype,
                     channel_chargetime,
                     channel_maxmoney,
                     SUM(num) MinCount
                FROM (SELECT channel_id,
                             channel_stationtype,
                             channel_chargetime,
                             channel_maxmoney,
                             decode(nvl(b.hf_status, -1), -1, 0, 1) num
                        FROM (SELECT channel_id,
                                     channel_stationtype,
                                     channel_chargetime,
                                     channel_maxmoney
                                FROM ch_channelinfo cc
                               WHERE channel_needcard = 0 --不带卡渠道
                                 AND channel_isp = l_isptype --支持运营商
                                 AND (channel_provinceid = 0 or
                                     l_provinceid in
                                     (select cp.channel_provinceid
                                         from ch_channelprovince cp
                                        where cp.channel_id = cc.channel_id)) --支持省份
                                 AND (channel_citycode = '-1' OR
                                     channel_citycode LIKE
                                     '%' || l_citycode || '%') --支持地市
                                 AND channel_status = 0 --渠道可用
                                 AND (cc.channel_limit_money = -1 or
                                     cc.channel_money <=
                                     cc.channel_limit_money) --交易量没有超限
                                 AND channel_id NOT IN --不是禁止走的渠道
                                     (SELECT DISTINCT channel_id
                                        FROM ch_notmatchchannel nm
                                       WHERE order_source = l_ordersource
                                         AND (user_id = '-1' OR
                                             user_id = l_userid))
                                 AND ( --支持面值
                                      (channel_step <> 0 AND
                                      MOD(l_fullmoney - channel_minmoney,
                                           channel_step) = 0 AND
                                      l_fullmoney >= cc.channel_minmoney AND
                                      l_fullmoney <= cc.channel_maxmoney) OR
                                      (channel_step = 0 AND
                                      l_fullmoney IN
                                      (SELECT DISTINCT money
                                          FROM ch_money
                                         WHERE channel_id = cc.channel_id)) OR --可分次
                                      (l_SplitFlag = 0 AND l_fullmoney >= 100 AND
                                      l_fullmoney >= cc.channel_minmoney) OR --可补充
                                      (l_SplitFlag = 2 AND l_fullmoney > 50 AND
                                      l_finishmoney > 0 AND
                                      l_fullmoney >= cc.channel_minmoney))
                                 AND EXISTS
                               (SELECT *
                                        FROM ch_supporttbusiness
                                       WHERE channel_id = cc.channel_id
                                         AND service_type = l_servicetype)) a
                        LEFT JOIN (SELECT f.hf_acchannel, f.hf_status
                                    FROM od_fullnote f
                                   WHERE f.hf_orderid = v_orderid) b ON a.channel_id =
                                                                        b.hf_acchannel)
               GROUP by channel_id,
                        channel_stationtype,
                        channel_chargetime,
                        channel_maxmoney
               ORDER BY SUM(num) ASC, channel_chargetime ASC)
       WHERE rownum = 1;
    
      --只有一个符合条件的不带卡渠道，按照2：1关系走，其它则不变
      IF (l_nocardcount = 1 AND
         l_noneedcount - l_trycount + 1 <= 2 * l_needcount + 2) OR
         (l_noneedcount - l_trycount <= l_needcount) THEN
      
        v_result := '0';
        GOTO chargemoney_label;
      ELSE
        v_result := '81001';
        GOTO NeedCard_Label;
      END IF;
    
    EXCEPTION
      WHEN OTHERS THEN
        v_result := '1007';
    END;
  
  ELSIF l_schanneltype = 2 THEN
    --成本优先
    BEGIN
      SELECT channel_id, channel_stationtype, channel_maxmoney, MinCount
        INTO v_channelid, v_stationtype, l_chargemoney, l_noneedcount
        FROM (SELECT channel_id,
                     channel_stationtype,
                     channel_cost,
                     channel_maxmoney,
                     SUM(num) MinCount
                FROM (SELECT channel_id,
                             channel_stationtype,
                             channel_cost,
                             channel_maxmoney,
                             decode(nvl(b.hf_status, -1), -1, 0, 1) num
                        FROM (SELECT channel_id,
                                     channel_stationtype,
                                     channel_cost,
                                     channel_maxmoney
                                FROM ch_channelinfo cc
                               WHERE channel_needcard = 0 --不带卡渠道
                                 AND channel_isp = l_isptype --支持运营商
                                 AND (channel_provinceid = 0 or
                                     l_provinceid in
                                     (select cp.channel_provinceid
                                         from ch_channelprovince cp
                                        where cp.channel_id = cc.channel_id)) --支持省份
                                 AND (channel_citycode = '-1' OR
                                     channel_citycode LIKE
                                     '%' || l_citycode || '%') --支持地市
                                 AND channel_status = 0 --渠道可用
                                 AND (cc.channel_limit_money = -1 or
                                     cc.channel_money <=
                                     cc.channel_limit_money) --交易量没有超限
                                 AND channel_id NOT IN --不是禁止走的渠道
                                     (SELECT DISTINCT channel_id
                                        FROM ch_notmatchchannel nm
                                       WHERE order_source = l_ordersource
                                         AND (user_id = '-1' OR
                                             user_id = l_userid))
                                 AND ( --支持面值
                                      (channel_step <> 0 AND
                                      MOD(l_fullmoney - channel_minmoney,
                                           channel_step) = 0 AND
                                      l_fullmoney >= cc.channel_minmoney AND
                                      l_fullmoney <= cc.channel_maxmoney) OR
                                      (channel_step = 0 AND
                                      l_fullmoney IN
                                      (SELECT DISTINCT money
                                          FROM ch_money
                                         WHERE channel_id = cc.channel_id)) OR --可分次
                                      (l_SplitFlag = 0 AND l_fullmoney >= 100 AND
                                      l_fullmoney >= cc.channel_minmoney) OR --可补充
                                      (l_SplitFlag = 2 AND l_fullmoney > 50 AND
                                      l_finishmoney > 0 AND
                                      l_fullmoney >= cc.channel_minmoney))
                                 AND EXISTS
                               (SELECT *
                                        FROM ch_supporttbusiness
                                       WHERE channel_id = cc.channel_id
                                         AND service_type = l_servicetype)) a
                        LEFT JOIN (SELECT f.hf_acchannel, f.hf_status
                                    FROM od_fullnote f
                                   WHERE f.hf_orderid = v_orderid) b ON a.channel_id =
                                                                        b.hf_acchannel)
               GROUP by channel_id,
                        channel_stationtype,
                        channel_cost,
                        channel_maxmoney
               ORDER BY SUM(num) ASC, channel_cost ASC)
       WHERE rownum = 1;
    
      --只有一个符合条件的不带卡渠道，按照2：1关系走，其它则不变
      IF (l_nocardcount = 1 AND
         l_noneedcount - l_trycount + 1 <= 2 * l_needcount + 2) OR
         (l_noneedcount - l_trycount <= l_needcount) THEN
        v_result := '0';
        GOTO chargemoney_label;
      ELSE
        v_result := '81001';
        GOTO NeedCard_Label;
      END IF;
    
    EXCEPTION
      WHEN OTHERS THEN
        v_result := '1007';
    END;
  ELSE
    --分流比
    BEGIN
      SELECT channel_id, channel_stationtype, channel_maxmoney, MinCount
        INTO v_channelid, v_stationtype, l_chargemoney, l_noneedcount
        FROM (SELECT channel_id,
                     channel_stationtype,
                     channel_curpercent,
                     channel_percent,
                     channel_maxmoney,
                     SUM(num) MinCount
                FROM (SELECT channel_id,
                             channel_stationtype,
                             channel_curpercent,
                             channel_percent,
                             channel_maxmoney,
                             decode(nvl(b.hf_status, -1), -1, 0, 1) num
                        FROM (SELECT cc.channel_id,
                                     cc.channel_stationtype,
                                     cc.channel_curpercent,
                                     cc.channel_percent,
                                     cc.channel_maxmoney
                                FROM ch_channelinfo cc
                               WHERE cc.channel_needcard = 0 --不带卡渠道
                                 AND cc.channel_isp = l_isptype --支持运营商
                                 AND (channel_provinceid = 0 or
                                     l_provinceid in
                                     (select cp.channel_provinceid
                                         from ch_channelprovince cp
                                        where cp.channel_id = cc.channel_id)) --支持省份
                                 AND (cc.channel_citycode = '-1' OR
                                     channel_citycode LIKE
                                     '%' || l_citycode || '%') --支持地市
                                 AND cc.channel_status = 0 --渠道可用
                                 AND (cc.channel_limit_money = -1 or
                                     cc.channel_money <=
                                     cc.channel_limit_money) --交易量没有超限
                                 AND cc.channel_id NOT IN --不是禁止走的渠道
                                     (SELECT DISTINCT channel_id
                                        FROM ch_notmatchchannel nm
                                       WHERE order_source = l_ordersource
                                         AND (user_id = '-1' OR
                                             user_id = l_userid))
                                 AND ( --支持面值
                                      (channel_step <> 0 AND
                                      MOD(l_fullmoney - cc.channel_minmoney,
                                           cc.channel_step) = 0 AND
                                      l_fullmoney >= cc.channel_minmoney AND
                                      l_fullmoney <= cc.channel_maxmoney) OR
                                      (channel_step = 0 AND
                                      l_fullmoney IN
                                      (SELECT DISTINCT money
                                          FROM ch_money
                                         WHERE channel_id = cc.channel_id)) OR --可分次
                                      (l_SplitFlag = 0 AND l_fullmoney >= 100 AND
                                      l_fullmoney >= cc.channel_minmoney) OR --可补充
                                      (l_SplitFlag = 2 AND l_fullmoney > 50 AND
                                      l_finishmoney > 0 AND
                                      l_fullmoney >= cc.channel_minmoney))
                                 AND EXISTS
                               (SELECT *
                                        FROM ch_supporttbusiness
                                       WHERE channel_id = cc.channel_id
                                         AND service_type = l_servicetype)) a
                        LEFT JOIN (SELECT f.hf_acchannel, f.hf_status
                                    FROM od_fullnote f
                                   WHERE f.hf_orderid = v_orderid) b ON a.channel_id =
                                                                        b.hf_acchannel)
               GROUP by channel_id,
                        channel_stationtype,
                        channel_curpercent,
                        channel_percent,
                        channel_maxmoney
               ORDER BY SUM(num) ASC,
                        channel_curpercent /
                        decode(channel_percent, 0, 0.1, channel_percent) ASC)
       WHERE rownum = 1;
    
      --只有一个符合条件的不带卡渠道，按照2：1关系走，其它则不变
      IF (l_nocardcount = 1 AND
         l_noneedcount - l_trycount + 1 <= 2 * l_needcount + 2) OR
         (l_noneedcount - l_trycount <= l_needcount) THEN
        --当前分流值加1
        BEGIN
          SELECT nvl(channel_curpercent, 0)
            INTO l_curpercent
            FROM ch_channelinfo cp
           WHERE channel_id = v_channelid
             FOR UPDATE NOWAIT;
        
          UPDATE ch_channelinfo cp
             SET channel_curpercent = nvl(channel_curpercent, 0) + 1
           WHERE channel_id = v_channelid;
        EXCEPTION
          WHEN OTHERS THEN
            v_Result := '0';
        END;
        v_result := '0';
        GOTO chargemoney_label;
      ELSE
        v_result := '81001';
        GOTO NeedCard_Label;
      END IF;
    
    EXCEPTION
      WHEN OTHERS THEN
        v_result := '1007';
    END;
  END IF;

  --<<不带卡渠道充值金额>>
  --=========================================================================================
  <<chargemoney_label>>
  IF v_result <> '0' THEN
    --没渠道不带卡渠道，走带卡渠道
    GOTO NeedCard_Label;
  END IF;

  SELECT channel_step, channel_maxmoney, channel_minmoney
    INTO l_channelstep, l_channelmaxmoney, l_channelminmoney
    FROM ch_channelinfo
   WHERE channel_id = v_channelid;
  IF l_SplitFlag = 0 OR (l_SplitFlag = 2 AND l_finishmoney > 0) THEN
    IF l_channelstep = 0 THEN
      SELECT max(money)
        INTO v_fullmoney
        FROM ch_money c
       WHERE channel_id = v_channelid
         AND c.money <= l_fullmoney;
    ELSE
      IF l_fullmoney > l_chargemoney THEN
        v_fullmoney := l_chargemoney;
      ELSE
        v_fullmoney := floor((l_fullmoney - l_channelminmoney) /
                             l_channelstep) * l_channelstep +
                       l_channelminmoney;
      END IF;
    END IF;
  
  ELSE
    v_fullmoney := l_fullmoney;
  END IF;

  IF v_fullmoney < 50 AND v_fullmoney <> l_fullmoney THEN
    v_result := '1007';
    GOTO NeedCard_Label;
  END IF;

  GOTO End_Label; --取到渠道结束

  --<<带卡渠道>>
  --=======================================================================================
  <<NeedCard_Label>>
--取本次订单不同类型带卡数量
  SELECT NVL(SUM(decode(c.channel_type, 1, 1, 0)), 0),
         NVL(SUM(decode(c.channel_type, 4, 1, 3, 1, 0)), 0),
         NVL(SUM(decode(c.channel_type, 11, 1, 0)), 0)
    INTO l_asrunioncount, l_ykccount, l_swebcount
    FROM od_fullnote f, ch_channelinfo c
   WHERE f.hf_orderid = v_orderid
     AND f.hf_status IS NOT NULL
     AND f.hf_acchannel = c.channel_id
     AND c.channel_type IN (1, 3, 4, 11);

  --如果是联通则进行如意商务渠道的存在判断
  IF l_isptype = 0 THEN
    SELECT COUNT(*)
      INTO l_ryswcount
      FROM ch_channelinfo t
     WHERE t.channel_type = 4
       AND t.channel_isp = 0
       AND t.channel_needcard = 1
       AND t.channel_status = 0;
  END IF;

  --取带卡渠道
  BEGIN
    SELECT channel_id, channel_stationtype
      INTO v_channelid, v_stationtype
      FROM (SELECT ccl.channel_id, ccl.channel_stationtype, ccl.channel_type
              FROM ch_channelinfo ccl
             WHERE ccl.channel_needcard = 1 --需要卡
               AND ccl.channel_isp = l_isptype
               AND (ccl.channel_provinceid = 0 or
                   l_provinceid in
                   (select cp.channel_provinceid
                       from ch_channelprovince cp
                      where cp.channel_id = ccl.channel_id)) --支持省份
               AND (channel_citycode = '-1' OR
                   channel_citycode LIKE '%' || l_citycode || '%') --支持地市
               AND channel_status IN (0, 2) --渠道可用
               AND (ccl.channel_limit_money = -1 or
                   ccl.channel_money <= ccl.channel_limit_money) --交易量没有超限
               AND ((channel_type NOT IN (0, 9, 10) AND l_isptype <> 0) --排除007和无线语音卡充值渠道
                   OR (l_ucswitch <= 0 AND
                   channel_type NOT IN (0, 9, 10, 3) AND l_isptype = 0) OR
                   (l_ucswitch <= 0 AND channel_type = 3 AND l_isptype = 0 AND
                   l_ryswcount > 0) --如果是一卡充渠道，则判断如意商务渠道存在开通
                   OR (l_ucswitch > 0 AND channel_type IN (1, 4) AND
                   l_isptype = 0) --如果是3a则提取外呼和如意商务平台
                   )
               AND channel_id <> 145
               AND channel_id NOT IN --不是禁止走的渠道
                   (SELECT DISTINCT channel_id
                      FROM ch_notmatchchannel
                     WHERE order_source = l_ordersource
                       AND (user_id = '-1' OR user_id = l_userid))
               AND ( --支持面值
                    (channel_step <> 0 AND
                    MOD(l_fullmoney - channel_minmoney, channel_step) = 0 AND
                    l_fullmoney >= ccl.channel_minmoney AND
                    l_fullmoney <= ccl.channel_maxmoney) OR
                    (channel_step = 0 AND
                    l_fullmoney IN
                    (SELECT DISTINCT money
                        FROM ch_money
                       WHERE channel_id = ccl.channel_id)) OR --可分次
                    (l_SplitFlag <> 1 AND l_fullmoney >= channel_minmoney))
               AND EXISTS
             (SELECT *
                      FROM ch_supporttbusiness
                     WHERE channel_id = ccl.channel_id
                       AND service_type = l_servicetype)
               AND ( --移动可分次订单，小于30元时不能取全国卡带卡渠道
                    (l_isptype = 1 AND channel_type = 1 AND l_fullmoney >= 30) OR
                    (l_isptype = 1 AND channel_type <> 1) OR l_isptype <> 1 OR
                    l_SplitFlag = 2)
             ORDER BY decode(l_isptype,
                             0,
                             0,
                             decode(ccl.channel_type, 3, 1, 8, 2, 7, 4, 0)) ASC,
                      decode(l_isptype,
                             0,
                             0,
                             decode(l_servicetype,
                                    0,
                                    0,
                                    decode(ccl.channel_type,
                                           1,
                                           l_asrunioncount,
                                           3,
                                           l_ykccount,
                                           4,
                                           l_ykccount,
                                           11,
                                           l_swebcount,
                                           l_asrunioncount + l_swebcount + 100),
                                    decode(ccl.channel_type,
                                           3,
                                           l_ykccount,
                                           4,
                                           l_ykccount,
                                           11,
                                           l_swebcount,
                                           l_asrunioncount + l_swebcount + 100)),
                             decode(ccl.channel_type,
                                    1,
                                    l_asrunioncount,
                                    3,
                                    l_swebcount + l_ykccount,
                                    4,
                                    l_swebcount + l_ykccount,
                                    11,
                                    l_swebcount + l_ykccount,
                                    l_asrunioncount + l_swebcount + 100)) ASC,
                      decode(l_isptype,
                             0,
                             decode(l_servicetype,
                                    0,
                                    decode(ccl.channel_type,
                                           3,
                                           0,
                                           11,
                                           2,
                                           1,
                                           1,
                                           3),
                                    decode(ccl.channel_type, 3, 0, 11, 1, 2)),
                             1) ASC,
                      decode(ccl.channel_type, 11, 0, 1, 1, 2) ASC --优先web渠道，然后asr渠道
            )
     WHERE rownum = 1;
    v_fullmoney := l_fullmoney;
  EXCEPTION
    WHEN OTHERS THEN
      IF v_Result = '81001' THEN
        l_trycount := l_trycount + 1;
        GOTO NoCard_Label;
      ELSE
        v_result := '1007'; --不支持不带卡渠道
        RETURN;
      END IF;
  END;

  --<<找渠道结束>>
  --=======================================================================================
  <<End_Label>>
  v_result := '0';

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    v_result := '11111';
END;
/

prompt
prompt Creating procedure SP_M_CHANNEL_ONOFF
prompt =====================================
prompt
create or replace procedure hf.SP_M_CHANNEL_ONOFF(v_channel_ids  varchar2, --涉及到的渠道ID组合，以“,”隔开
                                               v_opt_type     number, --操作类型0：开通  1： 关闭
                                               v_auto_no      number, --是否自动开通  0：不需要   1：需要  2：已执行
                                               v_opt_userid   varchar2, --操作人ID
                                               v_opt_username varchar2, --操作人名称
                                               v_opt_log      varchar2, --操作日志
                                               v_on_time      varchar2, --如果v_auto_no=1，这里为开通时间
                                               v_linkOnline   number, --是否关联货架 0：不关联 1：关联         
                                               v_result       out VARCHAR2,
                                               v_desc         out varchar2)
/******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 充值渠道开关管理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  功能描述：对充值渠道的开关操作进行记录
            如果开通
            --1.将关闭渠道时下架的货架上架，但要判断此货架是否被人为下架 
            --2.判断是否设置了自动开通，如果设置了还没执行，修改成已执行
            --3.修改没有匹配到渠道、且可以通过此渠道充值的订单的状态为等待充值
            --4.修改渠道状态为开通
            --4.记录日志
           如果关闭
            --1.下架只能通过此渠道充值的货架
            --2.修改渠道状态为关闭
            --3.记录日志
  ******************************************************************************/
 AS
  l_count         number;
  l_online_count  number;
  l_channel_count number;
  l_manId         number;
begin
  v_result        := '0000';
  l_online_count  := 0;
  l_channel_count := 0;
  l_count         := 0;
  --参数验证
  if v_channel_ids is null or v_opt_type is null or v_auto_no is null or
     v_opt_userid is null or (v_opt_type <> 0 and v_opt_log is null) or
     (v_auto_no = 1 and v_on_time is null) then
    v_result := '0001';
    v_desc   := '参数错误！';
    return;
  end if;
  if v_opt_type = 0 then
    --开通通道
    --2.判断是否设置了自动开通，如果设置了还没执行，修改成已执行
    --3.修改没有匹配到渠道、且可以通过此渠道充值的订单的状态为等待充值
    --渠道状态：0打开，1关闭，2一卡充通道关闭，3暂停  ,5渠道维护自动关闭,8签退关闭
    FOR channel IN (select *
                      from ch_channelinfo t
                     where t.channel_id in
                           (select column_value
                              from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
                             where column_value is not null)
                       and t.channel_status in (1, 2, 3)) LOOP
      if v_linkOnline = 1 then
        --如果关联货架
        --1.将关闭渠道时下架的货架上架，但要判断此货架是否被人为下架 
        select count(distinct online_id)
          into l_count
          from (select a.online_id,
                       GET_ONLINE_CHANNEL_ALL(p.product_ispid,
                                              p.product_provinceid,
                                              p.product_citycode,
                                              b.order_source,
                                              b.user_id,
                                              p.product_content,
                                              p.product_type) channelIds
                  from (select t1.online_id,
                               t.apply_username,
                               t.status,
                               row_number() over(partition by t1.online_id order by t.auditing_time desc) rn
                          from po_auditing_main t, po_auditing_detail t1
                         where t.man_id = t1.man_id
                           and t.apply_type = 0 --上下架   
                           and t.auditing_status = 1 --审批通过
                        ) a,
                       po_productonlineinfo b,
                       po_productinfo p
                 where a.online_id = b.online_id
                   and a.rn = 1
                   and a.apply_username = 'channel' --最后一次是由渠道关闭而下架的
                   and a.status = 1 --最后一次为下架
                   and b.product_status = 1 --当前为下架的货架
                   and b.product_id = p.product_id)
         where ',' || channel.channel_id || ',' like channelIds;
        if l_count > 0 then
          update po_productonlineinfo p
             set p.product_status = 0
           where p.online_id in
                 (select distinct online_id
                    from (select a.online_id,
                                 GET_ONLINE_CHANNEL_ALL(p.product_ispid,
                                                        p.product_provinceid,
                                                        p.product_citycode,
                                                        b.order_source,
                                                        b.user_id,
                                                        p.product_content,
                                                        p.product_type) channelIds
                            from (select t1.online_id,
                                         t.apply_username,
                                         t.status,
                                         row_number() over(partition by t1.online_id order by t.auditing_time desc) rn
                                    from po_auditing_main   t,
                                         po_auditing_detail t1
                                   where t.man_id = t1.man_id
                                     and t.apply_type = 0 --上下架   
                                     and t.auditing_status = 1 --审批通过
                                  ) a,
                                 po_productonlineinfo b,
                                 po_productinfo p
                           where a.online_id = b.online_id
                             and a.rn = 1
                             and a.apply_username = 'channel' --最后一次是由渠道关闭而下架的
                             and a.status = 1 --最后一次为下架
                             and b.product_status = 1 --当前为下架的货架
                             and b.product_id = p.product_id)
                   where ',' || channel.channel_id || ',' like channelIds);
          --记录货架上架日志
          select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
          insert into po_auditing_main
            (man_id,
             status,
             apply_time,
             remark,
             auditing_time,
             apply_username,
             auditing_username,
             apply_type,
             auditing_status)
          values
            (l_manId,
             0,
             sysdate,
             '渠道开通关联货架上架',
             sysdate,
             'channel',
             'system',
             0,
             1);
          insert into po_auditing_detail
            (pk_id,
             man_id,
             online_id,
             product_id,
             order_source,
             old_status)
            select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
                   l_manId,
                   online_id,
                   product_id,
                   order_source,
                   1
              from (select a.online_id,
                           p.product_id,
                           b.order_source,
                           GET_ONLINE_CHANNEL_ALL(p.product_ispid,
                                                  p.product_provinceid,
                                                  p.product_citycode,
                                                  b.order_source,
                                                  b.user_id,
                                                  p.product_content,
                                                  p.product_type) channelIds
                      from (select t1.online_id,
                                   t.apply_username,
                                   t.status,
                                   row_number() over(partition by t1.online_id order by t.auditing_time desc) rn
                              from po_auditing_main t, po_auditing_detail t1
                             where t.man_id = t1.man_id
                               and t.apply_type = 0 --上下架   
                               and t.auditing_status = 1 --审批通过
                            ) a,
                           po_productonlineinfo b,
                           po_productinfo p
                     where a.online_id = b.online_id
                       and a.rn = 1
                       and a.apply_username = 'channel' --最后一次是由渠道关闭而下架的
                       and a.status = 1 --最后一次为下架
                       --and b.product_status = 1 --当前为下架的货架
                       and b.product_id = p.product_id)
             where ',' || channel.channel_id || ',' like channelIds;
          l_online_count := l_online_count + l_count;
        end if;
      end if;
      l_count := 0;
      select count(1)
        into l_count
        from ch_onoff_log l
       where l.channel_id = channel.channel_id
         and l.auto_no = 1
         and l.on_time > sysdate - 5 / 1440;
      if l_count > 0 then
        update ch_onoff_log t
           set t.auto_no   = 2,
               t.done_log  = '已人工开通，无须再执行!',
               t.done_time = sysdate
         where t.channel_id = channel.channel_id
           and t.auto_no = 1
           and t.on_time > sysdate - 5 / 1440;
      end if;
      if channel.channel_provinceid = -1 then
        update od_orderinfo h
           set h.charge_status = 2
         where h.req_ispid = channel.channel_isp
           and h.charge_status = 8
           and h.req_errorcode in ('1007');
      elsif channel.channel_provinceid <> -1 and
            channel.channel_citycode = -1 then
        update od_orderinfo h
           set h.charge_status = 2
         where h.req_ispid = channel.channel_isp
           and h.req_provinceid = channel.channel_provinceid
           and h.charge_status = 8
           and h.req_errorcode in ('1007');
      elsif channel.channel_provinceid <> -1 and
            channel.channel_citycode <> -1 then
        update od_orderinfo h
           set h.charge_status = 2
         where h.req_ispid = channel.channel_isp
           and h.req_provinceid = channel.channel_provinceid
           and h.req_citycode = channel.channel_citycode
           and h.charge_status = 8
           and h.req_errorcode in ('1007');
      end if;
    end loop;
    if v_linkOnline = 1 then
      v_desc := '关联【' || l_online_count || '】笔货架同时已上架。';
    end if;
    --4.修改渠道状态
    update ch_channelinfo t
       set t.channel_status = 0
     where t.channel_id in
           (select column_value
              from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
             where column_value is not null)
       and t.channel_status in (1, 2, 3);
    --5.插入日志
    insert into ch_onoff_log
      select SEQ_CH_ONOFF_LOG.Nextval,
             column_value,
             v_opt_type,
             sysdate,
             0,
             v_opt_userid,
             v_opt_username,
             v_opt_log,
             null,
             null,
             null
        from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
       where column_value is not null;
    v_desc := '渠道开通成功！' || v_desc;
  elsif v_opt_type = 1 then
    --关闭渠道
    l_count         := 0;
    l_channel_count := 0;
    if v_linkOnline = 1 then
      --如果关联货架
      --1.下架只能通过此渠道充值的货架
      FOR channel IN (select *
                        from ch_channelinfo t
                       where t.channel_id in
                             (select column_value
                                from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
                               where column_value is not null)
                         and t.channel_status = 0) LOOP
        select count(distinct online_id)
          into l_channel_count
          from (select o.online_id,
                       GET_ONLINE_CHANNEL_ON(p.product_ispid,
                                             p.product_provinceid,
                                             p.product_citycode,
                                             o.order_source,
                                             o.user_id,
                                             p.product_content,
                                             p.product_type) channelIds
                  from po_productonlineinfo o, po_productinfo p
                 where o.product_id = p.product_id
                   and o.product_status = 0) t
         where channelIds = ',' || channel.channel_id || ',';
        l_count := l_count + l_channel_count;
        if l_channel_count > 0 then
          --记录货架上架日志
          select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
          insert into po_auditing_main
            (man_id,
             status,
             apply_time,
             remark,
             auditing_time,
             apply_username,
             auditing_username,
             apply_type,
             auditing_status)
          values
            (l_manId,
             1,
             sysdate,
             '渠道关闭关联货架下架',
             sysdate,
             'channel',
             'system',
             0,
             1);
          insert into po_auditing_detail
            (pk_id,
             man_id,
             online_id,
             product_id,
             order_source,
             old_status)
            select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
                   l_manId,
                   online_id,
                   product_id,
                   order_source,
                   0
              from (select distinct online_id, product_id, order_source
                      from (select o.online_id,
                                   p.product_id,
                                   o.order_source,
                                   GET_ONLINE_CHANNEL_ON(p.product_ispid,
                                                         p.product_provinceid,
                                                         p.product_citycode,
                                                         o.order_source,
                                                         o.user_id,
                                                         p.product_content,
                                                         p.product_type) channelIds
                              from po_productonlineinfo o, po_productinfo p
                             where o.product_id = p.product_id
                               and o.product_status = 0) t
                     where channelIds = ',' || channel.channel_id || ',');
        
          update po_productonlineinfo o
             set o.product_status = 1
           where o.online_id in
                 (select distinct online_id
                    from (select o.online_id,
                                 GET_ONLINE_CHANNEL_ON(p.product_ispid,
                                                       p.product_provinceid,
                                                       p.product_citycode,
                                                       o.order_source,
                                                       o.user_id,
                                                       p.product_content,
                                                       p.product_type) channelIds
                            from po_productonlineinfo o, po_productinfo p
                           where o.product_id = p.product_id
                             and o.product_status = 0) t
                   where channelIds = ',' || channel.channel_id || ',');
        end if;
      end loop;
    
      v_desc := '关联【' || l_count || '】笔货架同时已下架。';
    end if;
    --2.修改渠道状态为关闭
    update ch_channelinfo t
       set t.channel_status = 1
     where t.channel_id in
           (select column_value
              from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
             where column_value is not null)
       and t.channel_status = 0;
    --3.插入日志
    insert into ch_onoff_log
      select SEQ_CH_ONOFF_LOG.Nextval,
             column_value,
             v_opt_type,
             sysdate,
             v_auto_no,
             v_opt_userid,
             v_opt_username,
             v_opt_log,
             to_date(v_on_time, 'yyyy-mm-dd hh24:mi:ss'),
             null,
             null
        from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
       where column_value is not null;
    v_desc := '渠道关闭成功！' || v_desc;
  end if;
  commit;
EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    v_desc   := '存储过程执行异常，操作失败！';
    ROLLBACK;
end;
/

prompt
prompt Creating procedure SP_JOB_CHANNEL_AUTO_ONOFF
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_JOB_CHANNEL_AUTO_ONOFF is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-渠道自动开关JOB(41)
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-13
  修改内容：
  功能描述：每2分钟执行一次，将在渠道开关管理中设置的自动开关内容到指定时间执行
  ************************************************************/
  l_channelStatus number(2);
  l_optType       number(2);
  l_doneLog       varchar2(50);
  l_isDone        number(1);
  l_result        varchar2(10);
  l_desc          varchar2(50);
BEGIN

  for channel_auto in (select *
                         from ch_onoff_log t
                        where t.auto_no = 1
                          and t.on_time <= sysdate) loop
    l_isDone := 0;
    select c.channel_status
      into l_channelStatus
      from ch_channelinfo c
     where c.channel_id = channel_auto.channel_id;
    if channel_auto.opt_type = 0 and l_channelStatus > 0 then
      --操作为开通时，自动操作为关闭
      l_doneLog := '渠道已经为关闭状态，无须再关闭！';
      l_isDone  := 1;
    elsif channel_auto.opt_type = 1 and l_channelStatus = 0 then
      --操作为关闭时，自动操作为开通
      l_doneLog := '渠道已经为开通状态，无须再开通！';
      l_isDone  := 1;
    else
      l_doneLog := '自动执行结果：';
    end if;
    select decode(channel_auto.opt_type, 0, 1, 1, 0)
      into l_optType
      from dual;
    if l_optType is null then
      l_doneLog := '参数错误，终止执行！';
      l_isDone  := 1;
    end if;
    --修改为已执行
    update ch_onoff_log l
       set l.auto_no = 2, l.done_log = l_doneLog, l.done_time = sysdate
     where l.log_id = channel_auto.log_id;
    if l_isDone = 0 then
      sp_m_channel_onoff(v_channel_ids  => channel_auto.channel_id,
                         v_opt_type     => l_optType,
                         v_auto_no      => 0,
                         v_opt_userid   => 'system',
                         v_opt_username => 'system',
                         v_opt_log      => '主键【' || channel_auto.log_id ||
                                           '】自动执行',
                         v_on_time      => null,
                         v_linkonline   => 1,
                         v_result       => l_result,
                         v_desc         => l_desc);
      update ch_onoff_log l
         set l.done_log = l_doneLog || l_desc, l.done_time = sysdate
       where l.log_id = channel_auto.log_id;
    end if;
  end loop;
  commit;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
  
END;
/

prompt
prompt Creating procedure SP_JOB_PRODUCTONLINE_AUTO
prompt ============================================
prompt
create or replace procedure hf.SP_JOB_PRODUCTONLINE_AUTO as
  /*******************************************************
    产品名称：充值系统
    模块名称：数据库脚本-自动调整货架价格
    模块版本：1.0.0.0
    编译环境：ORACLE10g
    创建人员：wjjava
    创建日期：2011-07-1
    功能描述：根据po_rule_autochange表中的配置，每天凌晨执行一次，修改符合条件的货架价格，并记录日志
  *******************************************************/

  l_day       number(2);
  l_begindate date;
  l_enddate   date;
  l_count     number;
  l_manId     number;
begin
  --逐一生成执行记录
  for online_auto in (select *
                        from po_rule_autochange t
                       where t.adjust_type = 0) loop
    if online_auto.cyc_id = 0 and online_auto.adjust_type = 0 then
      --如果周期为每月执行一次，其它周期暂时不处理
      --判断当前日期是否为1号
      select to_number(to_char(sysdate, 'dd')) into l_day from dual;
      select count(1)
        into l_count
        from po_rule_rundetial t
       where t.rule_id = online_auto.id
         and t.begindate >= trunc(sysdate, 'MONTH');
      if l_day = 1 and l_count = 0 then
        --如果为1号且没有生成执行数据，则生成执行的数据，否则不处理，继续处理下一条记录
        select trunc(sysdate, 'MONTH') + online_auto.beginday - 1,
               trunc(sysdate, 'MONTH') + online_auto.endday - 1
          into l_begindate, l_enddate
          from dual;
        insert into po_rule_rundetial
          (id, rule_id, begindate, enddate, status, createtime)
        values
          (seq_rule_rundetial.nextval,
           online_auto.id,
           l_begindate,
           l_enddate,
           0,
           sysdate);
      end if;
    end if;
  end loop;
  --遍历货架自动执行记录表，状态为未执行、时间在要调整的时间范围之内的记录，进行价格调整

  for run_auto in (select r.id,
                          a.id rule_id,
                          a.req_ordersource,
                          a.req_userid,
                          a.product_isptype,
                          a.product_provinceid,
                          a.product_money,
                          a.product_delaytime,
                          a.product_rate
                     from po_rule_rundetial r, po_rule_autochange a
                    where r.rule_id = a.id
                      and r.status = 0
                      and a.adjust_type = 0
                      and sysdate between r.begindate and r.enddate) loop
  
    select count(1)
      into l_count
      FROM po_productonlineinfo q, po_productinfo p
     WHERE q.product_id = p.product_id
       and (run_auto.req_ordersource = -1 or
           run_auto.req_ordersource = q.order_source)
       AND (run_auto.req_userid = 'ALL' OR run_auto.req_userid = q.user_id) --代理商id
       AND (run_auto.product_isptype = -1 OR
           run_auto.product_isptype = p.product_ispid) --运营商
       AND (run_auto.product_provinceid = -1 OR
           run_auto.product_provinceid = p.product_provinceid) --省份
       AND (run_auto.product_money = -1 OR
           run_auto.product_money = p.product_content) --面值
       AND (run_auto.product_delaytime = -1 OR
           run_auto.product_delaytime = p.product_delaytime);
    if l_count > 0 then
      --插入货架操作日志主表
      select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
    
      insert into po_auditing_main
        (man_id,
         user_id,
         schanneltype,
         discount,
         splitflag,
         waittime,
         delaytime,
         status,
         apply_time,
         remark,
         apply_username,
         auditing_username,
         auditing_time,
         apply_type,
         auditing_status)
      values
        (l_manId,
         '',
         '',
         run_auto.product_rate,
         '',
         '',
         '',
         '',
         sysdate,
         '货架价格自动调整', --v_remark,
         'system',
         'system',
         sysdate,
         2,
         1);
      insert into po_auditing_detail
        (pk_id,
         man_id,
         online_id,
         product_id,
         order_source,
         old_user_id,
         old_schanneltype,
         old_price,
         old_discount,
         old_splitflag,
         old_waittime,
         old_delaytime,
         old_status)
        select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
               l_manId,
               q.online_id,
               q.product_id,
               q.order_source,
               q.user_id,
               q.product_schanneltype,
               q.product_price,
               q.product_discount,
               q.product_splitflag,
               q.product_waittime,
               q.product_delaytime,
               q.product_status
          FROM po_productonlineinfo q, po_productinfo p
         WHERE q.product_id = p.product_id
           and (run_auto.req_ordersource = -1 or
               run_auto.req_ordersource = q.order_source)
           AND (run_auto.req_userid = 'ALL' OR
               run_auto.req_userid = q.user_id) --代理商id
           AND (run_auto.product_isptype = -1 OR
               run_auto.product_isptype = p.product_ispid) --运营商
           AND (run_auto.product_provinceid = -1 OR
               run_auto.product_provinceid = p.product_provinceid) --省份
           AND (run_auto.product_money = -1 OR
               run_auto.product_money = p.product_content) --面值
           AND (run_auto.product_delaytime = -1 OR
               run_auto.product_delaytime = p.product_delaytime) --到帐类型
        ;
      --修改价格
      UPDATE PO_PRODUCTONLINEINFO t
         SET t.product_price = t.product_price +
                               (run_auto.product_rate / 100) *
                               (SELECT product_content
                                  FROM po_productinfo
                                 WHERE product_id = t.product_id)
       WHERE t.online_id IN
             (select distinct q.online_id
                FROM po_productonlineinfo q, po_productinfo p
               WHERE q.product_id = p.product_id
                 and (run_auto.req_ordersource = -1 or
                     run_auto.req_ordersource = q.order_source)
                 AND (run_auto.req_userid = 'ALL' OR
                     run_auto.req_userid = q.user_id) --代理商id
                 AND (run_auto.product_isptype = -1 OR
                     run_auto.product_isptype = p.product_ispid) --运营商
                 AND (run_auto.product_provinceid = -1 OR
                     run_auto.product_provinceid = p.product_provinceid) --省份
                 AND (run_auto.product_money = -1 OR
                     run_auto.product_money = p.product_content) --面值
                 AND (run_auto.product_delaytime = -1 OR
                     run_auto.product_delaytime = p.product_delaytime));
    end if;
    --修改记录状态为已执行
    update po_rule_rundetial t
       set t.status = 1, t.man_id = l_manId
     where t.id = run_auto.id;
  
  end loop;
  --遍历货架自动执行记录表，状态为已执行、时间在要调整的时间范围之外的记录，进行价格恢复
  for rollback_auto in (select r.id,
                               r.man_id,
                               a.id rule_id,
                               a.req_ordersource,
                               a.req_userid,
                               a.product_isptype,
                               a.product_provinceid,
                               a.product_money,
                               a.product_delaytime,
                               a.product_rate
                          from po_rule_rundetial r, po_rule_autochange a
                         where r.rule_id = a.id
                           and r.status = 1
                           and a.adjust_type = 0
                           and r.man_id is not null
                           and sysdate > r.enddate) loop
    --插入货架操作日志主表
    select SEQ_PO_AUDITING_MAIN.NEXTVAL into l_manId from dual;
    insert into po_auditing_main
      (man_id,
       user_id,
       schanneltype,
       discount,
       splitflag,
       waittime,
       delaytime,
       status,
       apply_time,
       remark,
       apply_username,
       auditing_username,
       auditing_time,
       apply_type,
       auditing_status)
    values
      (l_manId,
       '',
       '',
       rollback_auto.product_rate,
       '',
       '',
       '',
       '',
       sysdate,
       '货架价格自动恢复，主键【' || rollback_auto.man_id || '】',
       'system',
       'system',
       sysdate,
       2,
       1);
    insert into po_auditing_detail
      (pk_id,
       man_id,
       online_id,
       product_id,
       order_source,
       old_user_id,
       old_schanneltype,
       old_price,
       old_discount,
       old_splitflag,
       old_waittime,
       old_delaytime,
       old_status)
      select SEQ_PO_AUDITING_DETAIL.NEXTVAL,
             l_manId,
             q.online_id,
             q.product_id,
             q.order_source,
             q.user_id,
             q.product_schanneltype,
             q.product_price,
             q.product_discount,
             q.product_splitflag,
             q.product_waittime,
             q.product_delaytime,
             q.product_status
        FROM po_productonlineinfo q, po_productinfo p, po_auditing_detail a
       WHERE q.product_id = p.product_id
         and q.online_id = a.online_id
         and a.man_id = rollback_auto.man_id;
  
    --修改价格
    UPDATE PO_PRODUCTONLINEINFO t
       SET t.product_price = (select d.old_price
                                from po_auditing_detail d
                               where d.man_id = rollback_auto.man_id
                                 and d.online_id = t.online_id)
     WHERE t.online_id IN
           (select distinct a.online_id
              FROM po_auditing_detail a
             WHERE a.man_id = rollback_auto.man_id);
    --修改记录状态为已恢复
    update po_rule_rundetial t
       set t.status = 2
     where t.id = rollback_auto.id;
  end loop;

  commit;
Exception
  when others then
    rollback;
end;
/

prompt
prompt Creating procedure SP_JOB_RESET_CHANNELMONEY
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_JOB_RESET_CHANNELMONEY is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-将渠道信息表中的“该渠道当日交易量”每天凌晨清0
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-13
  修改内容：
  功能描述：由数据库 job调用
            v1：将渠道信息表中的“该渠道当日交易量”每天凌晨清0
  
  ************************************************************/
  l_warninfo VARCHAR2(200); -- 报警信息
BEGIN

  update CH_CHANNELINFO c
     set c.channel_money = 0
   where c.channel_status = 0;

  commit;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    -- 向报警表中添加报警记录
    l_warninfo := '每天凌晨重置渠道日交易量为0报异常！！！';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
  
END;
/

prompt
prompt Creating procedure SP_M_CHANNELMANAGER
prompt ======================================
prompt
create or replace procedure hf.SP_M_CHANNELMANAGER(v_dealType               number, --处理方式 0：添加 1：修改 2：删除
                                                v_channel_id             varchar2,
                                                v_channel_name           varchar2,
                                                v_channel_code           varchar2,
                                                v_channel_type           number,
                                                v_channel_chargetime     number,
                                                v_channel_cost           number,
                                                v_channel_stationtype    number,
                                                v_channel_needcard       number,
                                                v_channel_step           number,
                                                v_channel_minmoney       number,
                                                v_channel_maxmoney       number,
                                                v_channel_isp            number,
                                                v_channel_provinceid     varchar2, --省份id,以','隔开
                                                v_channel_citycode       varchar2,
                                                v_channel_percent        number,
                                                v_channel_limit_money    number,
                                                v_channel_pri            number,
                                                v_channel_cardtype       number,
                                                v_channel_cardsource     number,
                                                v_channel_waittime       number,
                                                v_channel_isreversed     number,
                                                v_supportMoneys          varchar2,
                                                v_channelSupportBusiness varchar2,
                                                v_result                 out VARCHAR2,
                                                v_desc                   out varchar2)
/******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 充值渠道管理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  功能描述：对充值渠道的增加、删除、修改的操作
  2011-08-04修改
  ******************************************************************************/
 AS
begin
  v_result := '0000';
  if v_dealType = 0 then
    begin
      if v_channel_provinceid <> '0' and  instr(',',v_channel_provinceid) > 0 then
        insert into ch_channelinfo
          (channel_id,
           channel_name,
           channel_code,
           channel_type,
           channel_chargetime,
           channel_cost,
           channel_stationtype,
           channel_needcard,
           channel_step,
           channel_minmoney,
           channel_maxmoney,
           channel_isp,
           channel_provinceid,
           channel_citycode,
           channel_percent,
           channel_limit_money,
           channel_isreversed)
        values
          (v_channel_id,
           v_channel_name,
           v_channel_code,
           v_channel_type,
           v_channel_chargetime,
           v_channel_cost,
           v_channel_stationtype,
           v_channel_needcard,
           v_channel_step,
           v_channel_minmoney,
           v_channel_maxmoney,
           v_channel_isp,
           -1,
           v_channel_citycode,
           v_channel_percent,
           v_channel_limit_money,
           v_channel_isreversed);
      
        --插入渠道省份表
        insert into ch_channelprovince
          select v_channel_id, column_value
                    from table(cast(MYCONVERT(v_channel_provinceid, ',') as t_vc))
                   where column_value is not null;
          --插入到渠道面值表
           insert into ch_money
            select v_channel_id, column_value
                    from table(cast(MYCONVERT(v_supportMoneys, ',') as t_vc))
                   where column_value is not null;
        --插入到渠道业务类型表
        insert into ch_supporttbusiness
          select v_channel_id, column_value
            from table(cast(MYCONVERT(v_channelSupportBusiness, ',') as t_vc))
           where column_value is not null;
        v_desc := '渠道添加成功！';
   
      else 
        insert into ch_channelinfo
          (channel_id,
           channel_name,
           channel_code,
           channel_type,
           channel_chargetime,
           channel_cost,
           channel_stationtype,
           channel_needcard,
           channel_step,
           channel_minmoney,
           channel_maxmoney,
           channel_isp,
           channel_provinceid,
           channel_citycode,
           channel_percent,
           channel_limit_money,
           channel_isreversed)
        values
          (v_channel_id,
           v_channel_name,
           v_channel_code,
           v_channel_type,
           v_channel_chargetime,
           v_channel_cost,
           v_channel_stationtype,
           v_channel_needcard,
           v_channel_step,
           v_channel_minmoney,
           v_channel_maxmoney,
           v_channel_isp,
           to_number(v_channel_provinceid),
           v_channel_citycode,
           v_channel_percent,
           v_channel_limit_money,
           v_channel_isreversed);
      
        --插入渠道省份表
        insert into ch_channelprovince(channel_id,channel_provinceid)
        values(v_channel_id,to_number(v_channel_provinceid));
          
          --插入到渠道面值表
           insert into ch_money
            select v_channel_id, column_value
                    from table(cast(MYCONVERT(v_supportMoneys, ',') as t_vc))
                   where column_value is not null;
      
        --插入到渠道业务类型表
        insert into ch_supporttbusiness
          select v_channel_id, column_value
            from table(cast(MYCONVERT(v_channelSupportBusiness, ',') as t_vc))
           where column_value is not null;
        v_desc := '渠道添加成功！';
                   
      end if;
    
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        v_result := '0001';
        v_desc   := '渠道ID重复';
        ROLLBACK;
    end;
  elsif v_dealType = 1 then
    update ch_channelinfo
       set channel_name        = decode(v_channel_name,
                                        null,
                                        channel_name,
                                        v_channel_name),
           channel_code        = decode(v_channel_code,
                                        null,
                                        channel_code,
                                        v_channel_code),
           channel_type        = decode(v_channel_type,
                                        null,
                                        channel_type,
                                        v_channel_type),
           channel_chargetime  = decode(v_channel_chargetime,
                                        null,
                                        channel_chargetime,
                                        v_channel_chargetime),
           channel_cost        = decode(v_channel_cost,
                                        null,
                                        channel_cost,
                                        v_channel_cost),
           channel_stationtype = decode(v_channel_stationtype,
                                        null,
                                        channel_stationtype,
                                        v_channel_stationtype),
           channel_needcard    = decode(v_channel_needcard,
                                        null,
                                        channel_needcard,
                                        v_channel_needcard),
           channel_step        = decode(v_channel_step,
                                        null,
                                        channel_step,
                                        v_channel_step),
           channel_minmoney    = decode(v_channel_minmoney,
                                        null,
                                        channel_minmoney,
                                        v_channel_minmoney),
           channel_maxmoney    = decode(v_channel_maxmoney,
                                        null,
                                        channel_maxmoney,
                                        v_channel_maxmoney),
           channel_isp         = decode(v_channel_isp,
                                        null,
                                        channel_isp,
                                        v_channel_isp),
           channel_provinceid  = decode(v_channel_provinceid,
                                        null,
                                        channel_provinceid,
                                        v_channel_provinceid),
           channel_citycode    = decode(v_channel_citycode,
                                        null,
                                        channel_citycode,
                                        v_channel_citycode),
           channel_percent     = decode(v_channel_percent,
                                        null,
                                        channel_percent,
                                        v_channel_percent),
           channel_limit_money = decode(v_channel_limit_money,
                                        null,
                                        channel_limit_money,
                                        v_channel_limit_money),
           channel_isreversed  = decode(v_channel_isreversed,
                                        null,
                                        channel_isreversed,
                                        v_channel_isreversed)
     where channel_id = v_channel_id;
  
    delete ch_money t where t.channel_id = v_channel_id;
    --插入到渠道面值表
    insert into ch_money
      select v_channel_id, column_value
        from table(cast(MYCONVERT(v_supportMoneys, ',') as t_vc))
       where column_value is not null;
    --插入到渠道业务类型表
    delete ch_supporttbusiness t where t.channel_id = v_channel_id;
    insert into ch_supporttbusiness
      select v_channel_id, column_value
        from table(cast(MYCONVERT(v_channelSupportBusiness, ',') as t_vc))
       where column_value is not null;
    v_desc := '渠道修改成功！';
  elsif v_dealType = 2 then
    delete ch_money t where t.channel_id = v_channel_id;
    delete ch_supporttbusiness t where t.channel_id = v_channel_id;
    delete ch_channelinfo where channel_id = v_channel_id;
    v_desc := '渠道删除成功！';
  end if;
  commit;
EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    v_desc   := '存储过程执行异常，操作失败！';
    ROLLBACK;
end;
/

prompt
prompt Creating procedure SP_M_MANUALDEALORDER_LOCK
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_M_MANUALDEALORDER_LOCK(v_opt_type       IN NUMBER, --操作类型 0：锁定 1：解锁
                                                       v_isjudge_status in number, --是否判断状态 0：判断 1：不判断
                                                       v_hf_serialid    IN VARCHAR2,
                                                       v_opt_userid     IN VARCHAR2,
                                                       v_res            OUT VARCHAR2,
                                                       v_desc           OUT VARCHAR2,
                                                       v_lock_userid    OUT VARCHAR2) AS
  l_count        NUMBER;
  l_order_status NUMBER;
  l_hfcount      NUMBER;
  l_errorcode    varchar2(10);
  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 人工订单锁定
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  功能描述：锁定当前处理的订单，防止多人同时处理同一笔订单
            v2 weisd 2011-08-10 补充订单匹配不到渠道失败处理  2 订单锁定，共用解锁
               v_hf_serialid 为订单号
  ******************************************************************************/
BEGIN
  v_res := '0000';
  IF v_opt_type = 0 THEN
    IF v_hf_serialid IS NULL OR v_opt_userid IS NULL THEN
      v_res  := '0001';
      v_desc := '参数不全';
      RETURN;
    END IF;
    if v_isjudge_status = 0 then
      select h2.charge_status
        into l_order_status
        from od_fullnote h1, od_orderinfo h2
       where h1.hf_orderid = h2.req_orderid
         and h1.hf_serialid = v_hf_serialid;

      IF l_order_status <> 8 THEN
        v_res  := '0002';
        v_desc := '该充值记录已被处理';
        RETURN;
      END IF;
    end if;
    SELECT COUNT(0)
      INTO l_count
      FROM od_customcheck_failorder H
     WHERE H.HF_SERIALID = v_hf_serialid;
    IF l_count = 0 THEN
      INSERT INTO od_customcheck_failorder
      VALUES
        (v_hf_serialid, v_opt_userid, SYSDATE);
      v_lock_userid := v_opt_userid;
      commit;
      RETURN;
    ELSE
      SELECT H.OPT_USERID
        INTO v_lock_userid
        FROM od_customcheck_failorder H
       WHERE H.HF_SERIALID = v_hf_serialid;
      RETURN;
    END IF;
  ELSIF v_opt_type = 1 THEN
    IF v_hf_serialid IS NULL OR v_opt_userid IS NULL THEN
      v_res  := '0001';
      v_desc := '参数不全';
      RETURN;
    END IF;
    DELETE od_customcheck_failorder H WHERE H.HF_SERIALID = v_hf_serialid;
    v_desc := '解锁成功';
    commit;
    RETURN;
    -- 锁定订单
  ELSIF v_opt_type = 2 THEN
    IF v_hf_serialid IS NULL OR v_opt_userid IS NULL THEN
      v_res  := '0001';
      v_desc := '参数不全';
      RETURN;
    END IF;
    if v_isjudge_status = 0 then
      select count(1)
        into l_hfcount
        from od_fullnote h1
       where h1.hf_orderid = v_hf_serialid
         and h1.hf_status not in (0, 3); -- 不是最终状态的
      IF l_hfcount <> 0 THEN
        v_res  := '0003';
        v_desc := '订单记录存在未完成的充值记录';
        RETURN;
      END IF;

      select h2.charge_status, h2.req_errorcode
        into l_order_status, l_errorcode
        from od_orderinfo h2
       where h2.req_orderid = v_hf_serialid;

      IF l_order_status <> 8 THEN
        v_res  := '0004';
        v_desc := '该订单记录已被处理';
        RETURN;
      END IF;
      IF l_errorcode <> '1007' THEN
        --不是无法获取渠道
        v_res  := '0005';
        v_desc := '该订单不是获取渠道失败,联系相关人员';
        RETURN;
      END IF;
    end if;
    SELECT COUNT(0)
      INTO l_count
      FROM od_customcheck_failorder H
     WHERE H.HF_SERIALID = v_hf_serialid;
    IF l_count = 0 THEN
      INSERT INTO od_customcheck_failorder
      VALUES
        (v_hf_serialid, v_opt_userid, SYSDATE);
      v_lock_userid := v_opt_userid;
      commit;
      RETURN;
    ELSE
      SELECT H.OPT_USERID
        INTO v_lock_userid
        FROM od_customcheck_failorder H
       WHERE H.HF_SERIALID = v_hf_serialid;
      RETURN;
    END IF;
  ELSE
    v_res  := '0001';
    v_desc := '参数不全';
    RETURN;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    v_res  := '1111';
    v_desc := '执行存储过程异常';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure SP_M_MANUALDEALORDER
prompt =======================================
prompt
create or replace procedure hf.SP_M_MANUALDEALORDER(v_dealType    number, --处理方式 0：充值成功 1：充值失败 2：再次充值
                                                  v_orderid     in VARCHAR2, --订单号
                                                  v_hfserialid  in VARCHAR2, --话费流水号
                                                  v_finishmoney in NUMBER, --实现充值金额，只有当v_dealType=1时才有值
                                                  v_checkIsp    in varchar2, --渠道方核实结果
                                                  v_remark      in varchar2, --备注
                                                  v_operid      in varchar2, --操作人ID（客服ID）
                                                  v_opername    in varchar2, --操作人名称
                                                  v_result      out VARCHAR2)
/******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 人工订单处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  功能描述：对结果未知订单的处理，包括：充值成功处理、充值失败处理、继续充值处理
            08.11  weisd 补充 订单无渠道处理  v_dealType 订单无渠道
  ******************************************************************************/
 AS
  l_warninfo            VARCHAR2(200); -- 报警信息
  l_curr_fu_fullmoney   number(10, 2);
  l_curr_fu_finishmoney number(10, 2);
  l_curr_orderstatus    number(2);
  l_new_orderstatus     number(2);
  l_curr_hfstatus       number(2);
  l_new_hfstatus        number(2);
  l_curr_money          number(10, 2);
  l_curr_finishmoney    NUMBER(10, 2);
  l_curr_finishtime     date;
  l_curr_linetime       date;
  l_curr_trycount       number(2);
  l_hferrorcode         varchar2(10);
  l_res                 varchar2(10);
  l_desc                varchar2(50);
  l_lock_userid         varchar2(20);
  l_hfstatus_count      number;
begin
  v_result := '0000';
  begin

  if v_dealType = 3  then
    select o.charge_status,
           o.req_money,
           o.charge_finishmoney,
           o.charge_finishtime,
           o.charge_linetime,
           o.req_trycount,
           o.req_errorcode
      into l_curr_orderstatus,
           l_curr_money,
           l_curr_finishmoney,
           l_curr_finishtime,
           l_curr_linetime,
           l_curr_trycount,
           l_hferrorcode
      from od_orderinfo o
     where o.req_orderid = v_orderid ;
  else

    select o.charge_status,
           o.req_money,
           o.charge_finishmoney,
           o.charge_finishtime,
           o.charge_linetime,
           o.req_trycount,
           f.hf_status,
           f.full_money,
           f.finish_money,
           f.hf_errorcode
      into l_curr_orderstatus,
           l_curr_money,
           l_curr_finishmoney,
           l_curr_finishtime,
           l_curr_linetime,
           l_curr_trycount,
           l_curr_hfstatus,
           l_curr_fu_fullmoney,
           l_curr_fu_finishmoney,
           l_hferrorcode
      from od_orderinfo o, od_fullnote f
     where o.req_orderid = v_orderid
       and f.hf_serialid = v_hfserialid;

    end if;

    if v_dealType = 0 then
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_finishmoney is null or v_finishmoney < 0 or v_checkIsp is null then
        v_result := '0001'; --参数异常
        goto lockorder;
      end if;
      if l_curr_hfstatus not in (4, 9) or l_curr_orderstatus not in (7, 8) then
        v_result := '0002'; --状态异常
        goto lockorder;
      end if;
      if v_finishmoney > l_curr_fu_fullmoney then
        v_result := '0003'; --金额异常，成功金额不能大于订单本次充值金额
        goto lockorder;
      end if;
      --0：充值成功处理
      --修改话费充值记录表状态为成功
      --修改订单表，如果订单金额=成功金额，修改状态为成功，如果不等，修改状态为等待处理，等待下一次的修改
      if (v_finishmoney - l_curr_fu_fullmoney) = 0 then
        l_new_hfstatus := 0; --成功
      elsif (v_finishmoney - l_curr_fu_fullmoney) < 0 then
        l_new_hfstatus := 6; --部分成功
      elsif (v_finishmoney - l_curr_fu_fullmoney) > 0 then
        --告警，成功，这种情况不可能发生
        l_new_hfstatus := 0; --成功
        l_warninfo     := '话费充值记录中话费流水号:' || v_hfserialid ||
                          '渠道返回已充值大于实际要充金额!';
        sp_sys_dberrorinfo_create(1, 'system', '', l_warninfo, '', '');
      end if;
      update od_fullnote t
         set t.hf_status    = l_new_hfstatus,
             t.finish_money = v_finishmoney,
             t.finish_time  = sysdate
       where t.hf_serialid = v_hfserialid;
      if (v_finishmoney + l_curr_finishmoney - l_curr_money) = 0 then
        l_new_orderstatus := 0; --订单成功
      elsif (v_finishmoney + l_curr_finishmoney - l_curr_money) < 0 then
        if sysdate < l_curr_linetime and l_curr_trycount < 8 then
          --如果还没有订单要求完成时间，且充值次数没有超过8次，订单继续充值
          l_new_orderstatus := 2; --订单等待处理
        else
          l_new_orderstatus := 6; --订单部分成功
        end if;
      elsif (v_finishmoney + l_curr_finishmoney - l_curr_money) > 0 then
        --告警，成功，这种情况不可能发生
        l_new_orderstatus := 0; --成功
        l_warninfo        := '订单号:' || v_orderid || '，话费充值记录中话费流水号:' ||
                             v_hfserialid || '已充值金额大于实际要充金额!';
        sp_sys_dberrorinfo_create(1, 'system', '', l_warninfo, '', '');
      end if;
      UPDATE od_orderinfo t
         SET t.charge_status      = l_new_orderstatus,
             t.charge_finishmoney = v_finishmoney + l_curr_finishmoney,
             t.charge_finishtime  = sysdate
       WHERE t.req_orderid = v_orderid;

    elsif v_dealType = 1 then
      --1：充值失败处理
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_checkIsp is null then
        v_result := '0001'; --参数异常
        goto lockorder;
      end if;
      if l_curr_hfstatus not in (4, 9) or l_curr_orderstatus not in (7, 8) then
        v_result := '0002'; --状态异常
        goto lockorder;
      end if;
      l_new_hfstatus := 3;
      update od_fullnote t
         set t.hf_status = l_new_hfstatus, t.finish_time = sysdate
       where t.hf_serialid = v_hfserialid;
      if l_curr_finishmoney > 0 then
        l_new_orderstatus := 6; --订单部分成功
      else
        l_new_orderstatus := 4; --订单失败
      end if;
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.req_errorcode     = l_hferrorcode,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    elsif v_dealType = 2 then
      --2：再次充值处理
      --1、设置充值记录状态为失败
      --2、设置订单状态为等待处理，且延长要求完成时间5分钟
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_checkIsp is null then
        v_result := '0001'; --参数异常
        goto lockorder;
      end if;
      if l_curr_hfstatus not in (4, 9) or l_curr_orderstatus not in (7, 8) then
        v_result := '0002'; --状态异常
        goto lockorder;
      end if;
      l_new_hfstatus := 3; --
      update od_fullnote t
         set t.hf_status = l_new_hfstatus, t.finish_time = sysdate
       where t.hf_serialid = v_hfserialid;
      l_new_orderstatus := 2; --订单等待处理
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.charge_linetime   = sysdate + 5 / 1440, --当前时间—+5分钟
             t.req_errorcode     = l_hferrorcode,
             t.req_trycount      = 0,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;

    elsif v_dealType = 3 then
      --订单失败处理
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_checkIsp is null then
        v_result := '0001'; --参数异常
        goto lockorder;
      end if;
      if l_curr_orderstatus <> 8 or l_hferrorcode <> '1007' then
        v_result := '0002'; --状态异常
        goto lockorder;
      end if;

      select count(1)
        into l_hfstatus_count
        from od_fullnote h1
       where h1.hf_orderid = v_orderid
         and h1.hf_status not in (0, 3); -- 不是最终状态的
      IF l_hfstatus_count <> 0 THEN
        v_result  := '0004';--订单记录存在未完成的充值记录
        goto lockorder;
      END IF;

      l_new_orderstatus := 4;
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.req_errorcode     = l_hferrorcode,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    end if;

    --记录操作日志
    insert into cs_personconfirminfo
      (confirm_id,
       person_id,
       person_name,
       hf_serialid,
       hf_orderid,
       hf_confirmtime,
       hf_confirmresult,
       isp_check,
       record_check,
       new_hfstatus,
       old_hfstatus,
       new_orderstatus,
       old_orderstatus,
       old_finish_time,
       req_finish_money,
       remark)
    values
      (SEQ_cs_personconfirminfo.Nextval,
       v_operid,
       v_opername,
       v_hfserialid,
       v_orderid,
       sysdate,
       v_dealType,
       v_checkIsp,
       '',
       l_new_hfstatus,
       l_curr_hfstatus,
       l_new_orderstatus,
       l_curr_orderstatus,
       l_curr_finishtime,
       l_curr_finishmoney,
       v_remark);
    v_result := '0000';

    <<lockorder>>
  --解除锁定
    SP_M_MANUALDEALORDER_LOCK(1,
                               0,
                               v_hfserialid,
                               v_operid,
                               l_res,
                               l_desc,
                               l_lock_userid);
    commit;
  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;
end;
/

prompt
prompt Creating procedure SP_M_WAITINGDEALORDER
prompt ========================================
prompt
create or replace procedure hf.SP_M_WAITINGDEALORDER(v_dealType number, --处理方式 0：充值成功 1：充值失败 2：再次充值
                                                  v_orderid  in VARCHAR2, --订单号
                                                  v_operid   in varchar2, --操作人ID（客服ID）
                                                  v_opername in varchar2, --操作人名称
                                                  v_result   out VARCHAR2,
                                                  v_desc     out varchar2)
/******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 待处理订单处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  功能描述：对正在处理但已过要求完成时间的订单进行处理，
            包括：充值成功处理、充值失败处理、继续充值处理
            8.12 weisd 再次充值时候 错误码 1010
  ******************************************************************************/
 AS
  l_count               number(10);
  l_hfserialid          varchar2(20);
  l_curr_fu_fullmoney   number(10, 2);
  l_curr_fu_finishmoney number(10, 2);
  l_curr_orderstatus    number(2);
  l_new_orderstatus     number(2);
  l_curr_hfstatus       number(2);
  l_new_hfstatus        number(2);
  l_curr_money          number(10, 2);
  l_curr_finishmoney    NUMBER(10, 2);
  l_curr_finishtime     date;
  l_curr_linetime       date;
  l_curr_trycount       number(2);
  l_hferrorcode         varchar2(10);
begin
  v_result := '0000';
  begin
    --查找最后一次充值记录流水号
    select count(1)
      into l_count
      from od_fullnote t
     where t.hf_orderid = v_orderid;
    if l_count > 0 then
      select hf_serialid
        into l_hfserialid
        from (select f.hf_serialid
                from od_orderinfo o, od_fullnote f
               where o.req_orderid = f.hf_orderid
                 and o.req_orderid = v_orderid
               order by f.hf_serialid desc)
       where rownum = 1;
    
      select o.charge_status,
             o.req_money,
             o.charge_finishmoney,
             o.charge_finishtime,
             o.charge_linetime,
             o.req_trycount,
             f.hf_status,
             f.full_money,
             f.finish_money,
             f.hf_errorcode
        into l_curr_orderstatus,
             l_curr_money,
             l_curr_finishmoney,
             l_curr_finishtime,
             l_curr_linetime,
             l_curr_trycount,
             l_curr_hfstatus,
             l_curr_fu_fullmoney,
             l_curr_fu_finishmoney,
             l_hferrorcode
        from od_orderinfo o, od_fullnote f
       where o.req_orderid = v_orderid
         and f.hf_serialid = l_hfserialid;
      if l_curr_orderstatus <> 3 then
        v_result := '1001'; --该记录已被处理
        v_desc   := '该记录已被处理';
        return;
      end if;
    else
      v_result := '1000'; --没有找到充值记录
      v_desc   := '没有找到充值记录';
      return;
    end if;
    if l_hfserialid is null or v_orderid is null then
      v_result := '0001'; --参数异常
      v_desc   := '参数异常';
      return;
    end if;
    if l_curr_hfstatus <> 2 or l_curr_orderstatus <> 3 then
      v_result := '0002'; --状态异常
      v_desc   := '状态异常';
      return;
    end if;
    if v_dealType = 0 then
      --0：充值成功处理
      --修改话费充值记录表状态为成功
      --修改订单表，如果订单金额=成功金额，修改状态为成功，如果不等，修改状态为等待处理，等待下一次的修改
      l_new_hfstatus := 0; --成功
      update od_fullnote t
         set t.hf_status    = l_new_hfstatus,
             t.finish_money = t.full_money,
             t.hf_errorcode = '0000',
             t.finish_time  = sysdate
       where t.hf_serialid = l_hfserialid;
      l_new_orderstatus := 0;
      UPDATE od_orderinfo t
         SET t.charge_status      = l_new_orderstatus,
             t.charge_finishmoney = t.req_money,
             t.req_errorcode = '0000',
             t.charge_finishtime  = sysdate
       WHERE t.req_orderid = v_orderid;
    
    elsif v_dealType = 1 then
    
      if l_hferrorcode is null then 
         l_hferrorcode := '1010';
      end if;
      --1：充值失败处理    
      l_new_hfstatus := 3;
      update od_fullnote t
         set t.hf_status = l_new_hfstatus,t.hf_errorcode = l_hferrorcode, t.finish_time = sysdate
       where t.hf_serialid = l_hfserialid;
      l_new_orderstatus := 4; --订单失败
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.req_errorcode     = l_hferrorcode,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    elsif v_dealType = 2 then
      --2：再次充值处理
      --1、设置充值记录状态为失败
      --2、设置订单状态为等待处理，且延长要求完成时间5分钟
      if l_hferrorcode is null then 
         l_hferrorcode := '1010';
      end if;
      
      l_new_hfstatus := 3;
      update od_fullnote t
         set t.hf_status = l_new_hfstatus, t.hf_errorcode = l_hferrorcode, t.finish_time = sysdate
       where t.hf_serialid = l_hfserialid;
      l_new_orderstatus := 2; --订单等待处理   
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.charge_linetime   = sysdate + 5 / 1440,
             t.req_errorcode     = l_hferrorcode,
             t.req_trycount      = 0,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    end if;
    --记录操作日志
    insert into cs_personconfirminfo
      (confirm_id,
       person_id,
       person_name,
       hf_serialid,
       hf_orderid,
       hf_confirmtime,
       hf_confirmresult,
       isp_check,
       record_check,
       new_hfstatus,
       old_hfstatus,
       new_orderstatus,
       old_orderstatus,
       old_finish_time,
       req_finish_money,
       remark)
    values
      (SEQ_cs_personconfirminfo.Nextval,
       v_operid,
       v_opername,
       l_hfserialid,
       v_orderid,
       sysdate,
       v_dealType,
       '未核实',
       '',
       l_new_hfstatus,
       l_curr_hfstatus,
       l_new_orderstatus,
       l_curr_orderstatus,
       l_curr_finishtime,
       l_curr_finishmoney,
       '待处理订单工人处理');
    v_result := '0000';
    v_desc   := '处理成功';
    commit;
  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;
end;
/

prompt
prompt Creating procedure SP_OD_CANCELORDERCHARGE
prompt ==========================================
prompt
create or replace procedure hf.SP_OD_CANCELORDERCHARGE(v_opttype        in number, --操作类型
                                                    v_hfserialid     in VARCHAR2,
                                                    v_orderid        in VARCHAR2,
                                                    v_finishmoney    in NUMBER,
                                                    v_hfcancelstatus in NUMBER,
                                                    v_errorcode      in NUMBER,
                                                    v_result         out VARCHAR2)
/******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 话费充正信息
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-5-16
  添加内容：插入/更新话费充正信息
  --
  v_opttype 3、返回结果处理  0、插入更新， 2 为修改通知状态

  v1.1 weisd 补充添加 v_zhorderid 冲正流水
  v2   weisd 补充opttype 操作类型 是插入更新还是结果处理 冲正结果返回信息不一致
             对于冲正结果，冲正成功的修改充值记录表以及订单表状态
             删除  v_zhorderid
  ******************************************************************************/
 AS
  l_count         NUMBER; -- 充正信息是否存在
  l_warninfo      VARCHAR2(200); -- 报警信息
  l_orderid       varchar2(30);
  l_hfserialid    varchar2(30);
  l_finishmoney   NUMBER(10, 2);
  l_currentstatus NUMBER(10, 2); --当前冲正状态

begin
  v_result      := '1111';
  l_finishmoney := 0;

  begin

    if v_opttype = 3 then
      --结果返回处理
      select count(1)
        into l_count
        from od_cancelordercharge t
       where t.hf_serialid = v_hfserialid;

      if l_count = 1 and v_hfcancelstatus is not null then
        -- 已经冲正结果处理

        select t.hf_orderid,
               t.hf_serialid,
               t.finish_money,
               t.hf_cancelstatus
          into l_orderid, l_hfserialid, l_finishmoney, l_currentstatus
          from od_cancelordercharge t
         where t.hf_serialid = v_hfserialid
           for update;

        if l_currentstatus != 0 then
          --当前状态为 不成功
          if v_hfcancelstatus = 0 then
            --冲正成功
            update OD_FULLNOTE f
               set f.hf_status    = 3,
                   f.finish_time  = sysdate,
                   f.hf_errorcode = v_errorcode,
                   f.finish_money = f.finish_money - l_finishmoney
             where f.hf_orderid = l_orderid
               and f.hf_serialid = l_hfserialid
               and f.hf_status = 0;
            --修改订单状态 不能简单修改订单，金额呢？
            update OD_ORDERINFO o
               set o.charge_status      = 4,
                   o.charge_finishtime  = sysdate,
                   o.req_errorcode = v_errorcode,
                   o.charge_finishmoney = o.charge_finishmoney -
                                          l_finishmoney
             where o.req_orderid = l_orderid
               and o.charge_status = 0;
          end if;

          if v_hfcancelstatus = 4 and l_currentstatus != 1  then --查询未知,不能修改原来结果明确的
             null;
          else
            update od_cancelordercharge
               set hf_cancelstatus = v_hfcancelstatus,
                   req_sarttime    = sysdate,
                   req_errorcode   = decode(v_hfcancelstatus,0,null,v_errorcode)
             where hf_serialid = v_hfserialid;

          end if;

        else
          --当前为成功的
          if v_hfcancelstatus != 0 then
            -- 向报警表中添加报警记录
            l_warninfo := '冲正结果处理:' || v_hfserialid || ',当前状态:' ||
                          l_currentstatus || '元,返回状态' || v_hfcancelstatus ||
                          ',由冲正成功到失败存不匹配!'||',v_errorcode:'||v_errorcode;
            sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');

          end if; --当前成功，查询也成功，不做改变
          --比如：查询时候冲正是未知的，而查询后返回结果成功了，但是订单的状态已经是成功了，那么怎么处理


        end if;
        v_result := '0000';
      else
        --报警
        l_warninfo := '话费充值流水号:' || v_hfserialid ||
                      '冲正返回记过处理,根据冲正流水查询记录不符,状态:' ||v_hfcancelstatus||',v_errorcode:'||v_errorcode||',count:'|| l_count;
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
        v_result := '1111';
      end if;

    elsif v_opttype = 2 then
      --修改通知结果状态
      select count(1)
        into l_count
        from od_cancelordercharge t
       where t.hf_serialid = v_hfserialid;

      if l_count = 1 and v_hfcancelstatus is not null then
        if v_hfcancelstatus = 1 or v_hfcancelstatus = 2 then

          update od_cancelordercharge t
             set t.ec_notifyflag = v_hfcancelstatus
           where t.hf_serialid = v_hfserialid;

        elsif v_hfcancelstatus = 0 or v_hfcancelstatus = 3 then

          update od_cancelordercharge t
             set t.ec_notifyflag = v_hfcancelstatus,
                 t.ec_notifytime = sysdate
           where t.hf_serialid = v_hfserialid;

        end if;
        v_result := '0000';
      else

        v_result := '1111';

      end if;

    else
      -- 查询充正信息是否存在
      select count(1)
        into l_count
        from od_cancelordercharge t
       where t.hf_serialid = v_hfserialid
         and t.hf_orderid = v_orderid
         and rownum = 1;

      if l_count <= 0 then
        -- 没有记录,添加记录
        insert into od_cancelordercharge
          (hf_serialid,
           hf_orderid,
           finish_money,
           hf_cancelstatus,
           req_sarttime,
           req_trycount)
        values
          (v_hfserialid,
           v_orderid,
           v_finishmoney,
           v_hfcancelstatus,
           sysdate,
           1);
      else
        -- 已经有记录,更新记录
        update od_cancelordercharge
           set finish_money    = v_finishmoney,
               hf_cancelstatus = v_hfcancelstatus,
               req_trycount    = req_trycount + 1,
               req_errorcode   = v_errorcode
         where hf_serialid = v_hfserialid
           and hf_orderid = v_orderid;
      end if;
      v_result := '0000';
    end if;
    commit;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      v_result := '1111';
  end;

end;
/

prompt
prompt Creating procedure SP_OD_CREATEORDERINFO
prompt ========================================
prompt
create or replace procedure hf.SP_OD_CREATEORDERINFO(v_online_id    in NUMBER, -- 货架ID
                                                  v_mobilenum    in VARCHAR2, -- 手机号码
                                                  v_chargeamount in NUMBER, -- 充值金额
                                                  v_paymount     in NUMBER, -- 订单金额
                                                  v_orderid      in VARCHAR2, -- 订单号
                                                  v_ordersource  in VARCHAR2, -- 订单来源
                                                  v_agentid      in VARCHAR2, -- 代理商ID(用户ID)
                                                  v_ordertime    in VARCHAR2, -- 销售平台订单时间
                                                  v_mark         in VARCHAR2, -- 保留字段
                                                  v_receivetime  in VARCHAR2, --收单平台接收时间点
                                                  v_reqorderid   out VARCHAR2, -- 充值订单信息订单号
                                                  v_result       out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 话费充值订单信息
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-4-15
  添加内容：插入话费充值订单信息 流程类是 SP_OD_PREPAREORDERINFO
            v2 2011.06.23 该SP为直接下单,不走预下单,但是必须验证信息,下单成功状态为:2
            v3 2011.6.24 添加同一手机号，同一充值金额1分钟内不可重复提交
  ******************************************************************************/

 AS
  l_isp_id            NUMBER; -- 运营商标识
  l_province_id       NUMBER; -- 归属省份ID
  l_city_code         VARCHAR2(4); -- 归属地市区号
  l_product_id        NUMBER; -- 商品ID
  l_product_splitflag NUMBER; -- 产品分次标识
  l_product_type      NUMBER; -- 产品类型(业务类型)
  l_order_id          VARCHAR2(30); -- 话费充值订单信息中的订单号(主键)
  l_seq_result        VARCHAR2(30); -- 订单号返回码
  l_po_exist          VARCHAR2(4); -- 临时变量,以在收单信息中使用
  l_count1            NUMBER; -- 货架存在
  l_count2            NUMBER; -- 货架可用
  l_count3            NUMBER; -- 订单来源
  l_product_waittime  NUMBER(10); -- 初始等待时长，即接收请求后等待多长时间开始处理，单位为秒
  l_product_delaytime NUMBER(10); -- 允许到帐时长，单位为分钟
  l_content           NUMBER(10, 2); --商品步长
  l_minmoney          NUMBER(10, 2); --商品最小金额
  l_maxmoney          NUMBER(10, 2); --商品最大金额
  l_chk_money         VARCHAR2(4); -- 临时变量充值金额
  l_moblie_type       NUMBER; -- 判断是否是固话、
  l_moblie_trycount   number; --是否1分钟内重复充值
  l_begin_sysdate     date; -- 进入系统时候的时间,用于判断执行插入时候是否超时
  l_insert_count      number;--插入语句涉及到的数据量
  l_phone_valid       VARCHAR2(4); -- 手机号码验证结果
  l_v_product_type    number(1);--临时存储货架产品类型
  l_p_product_type      NUMBER(1);--货架的产品类型
  l_p_product_ispid      number(1); --货架运营商
	l_p_product_provinceid number(2); --货架省份
	l_p_product_citycode   varchar2(10);--货架城市

begin
  v_result     := '1111';
  v_reqorderid := '';
  l_count1     := 0;
  l_count2     := 0;
  l_po_exist   := '';
  l_chk_money  := '';
  l_phone_valid := '0000';--初始手机号码通过，防止对非手机充值干扰
  --设置时间，防止下单到入库时间过长
  if v_receivetime is null then 
     l_begin_sysdate := sysdate;
  else 
     l_begin_sysdate := to_date(v_receivetime,'yyyy-mm-dd hh24:mi:ss');
  end if;

  -- 查询订单来源是否存在或可用: 返回 1030
  select count(1)
    into l_count3
    from ch_ordersourceinfo t
   where t.os_id = v_ordersource
     and t.os_status = '0'
     and rownum = 1;

  -- 查询货架存在或可用状态: 不存在返回 1040 不可用返回1050
  select count(1), nvl(sum(decode(t.product_status, 0, 1, null, 0, 0)), 0)
    into l_count1, l_count2
    from po_productonlineinfo t
   where t.online_id = v_online_id
     and t.order_source = v_ordersource
     and rownum = 1;

  -- 查询充值金额是否与货架、商品金额匹配
  begin
    select p.product_content, p.product_minmoney, p.product_maxmoney,
           p.product_type,p.product_ispid, p.product_provinceid, p.product_citycode 
      into l_content, l_minmoney, l_maxmoney,l_p_product_type,l_p_product_ispid,l_p_product_provinceid,l_p_product_citycode
      from po_productinfo p, po_productonlineinfo t
     where p.product_id = t.product_id
       and t.online_id = v_online_id
       and t.order_source = v_ordersource
       and t.product_status = 0;

    l_chk_money := CHECK_CHARGEMONEY_MATCH(l_content,
                                           l_minmoney,
                                           l_maxmoney,
                                           v_chargeamount);

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      -- 无数据的异常
      l_chk_money := '0010';
  end;

  -- 是否是固话，暂时不支持
  select instr(v_mobilenum, '-') into l_moblie_type from dual;

  if l_moblie_type is not null and l_moblie_type > 0 then
    l_v_product_type := 1;--其他非手机充值
  else
      l_v_product_type := 0;--是手机充值
      
      IF (v_mobilenum  LIKE '%13800138000%') or (substr(v_mobilenum,1,1) = '0') THEN --非手机充值号码
          l_phone_valid := '0022';   --该号码不支持充值
      ELSIF length(v_mobilenum) <> 11 THEN 
          l_v_product_type := 1 ;--非手机充值
          l_phone_valid := '0023';   --手机号码有误
      ELSE 
          -- 查询号码信息表: 查询号码信息表 不存在返回 1060
          begin
            SELECT isp_id, province_id, city_code
              into l_isp_id, l_province_id, l_city_code
              FROM sys_accsegment
             WHERE acc_segment = substr(v_mobilenum, 0, 7)
               and rownum = 1;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              -- 无数据的异常
              l_po_exist := '0006';
          end;
      END IF;
  end if;

  -- 是否同一号码、同一面值1分钟重复充值
  select count(1)
    into l_moblie_trycount
    from od_orderinfo t
   where t.req_accnum = v_mobilenum
     and t.req_money = v_chargeamount
     and t.req_datetime >= sysdate - 1 / 1440
     and t.req_datetime <= sysdate;

  -- 插入收单记录
  -- 收单时间 货架ID 充值手机号 运营商标识 业务类型 订单号 订单来源 用户ID 是否支持分次 产品金额
  begin
    insert into od_recvwebbill
      (recv_date,
       online_id,
       req_accnum,
       req_ispid,
       req_busitype,
       pre_errorcode,
       req_orderid,
       order_source,
       urser_id,
       split_flag,
       product_content)
    values
      (sysdate,
       v_online_id,
       v_mobilenum,
       l_isp_id,
       l_product_type,
       '0',
       v_orderid,
       v_ordersource,
       v_agentid,
       l_product_splitflag,
       v_chargeamount);

    v_result := '0000';
    commit;

  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      -- 唯一索引冲突
      v_result := '0007';
      ROLLBACK;
      return;
  end;

  -- 参数的判断
  if l_moblie_trycount > 0 then
    -- 是否重复充值
    v_result := '0019';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_count3 = 0 then
    -- 订单来源不存在
    v_result := '0003';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_v_product_type != 0 then
      -- 暂时不支持固话
      v_result := '0012';
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  elsif l_phone_valid <> '0000'  then 
     --手机号码验证有误
      v_result := l_phone_valid;
      update od_recvwebbill t
         set t.cre_errorcode = v_result 
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  end if;

  if l_p_product_type <> l_v_product_type  then 
      --充值类型与货架产品类型不一致
      v_result := '0017';
      update od_recvwebbill t
         set t.cre_errorcode = v_result 
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  end if;

  if l_po_exist = '0006' then
      -- 号码信息不存在
      v_result := l_po_exist;
      update od_recvwebbill t
         set t.cre_errorcode = v_result 
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
    
  else 
       if l_p_product_ispid <> l_isp_id then 
          -- 号码与货架所属运营商不一致
          v_result := '0018';
          update od_recvwebbill t
             set t.cre_errorcode = v_result  
           where t.req_orderid = v_orderid
             and t.order_source = v_ordersource;
          commit;
          RETURN;
       end if;
       
       if l_p_product_provinceid != 0  then
          
          if  l_p_product_provinceid <> l_province_id  then 
            -- 手机号省份与货架省份不一致
            v_result := '0020';
            update od_recvwebbill t
               set t.cre_errorcode = v_result 
             where t.req_orderid = v_orderid
               and t.order_source = v_ordersource;
            commit;
            RETURN;
            
          elsif to_number(l_p_product_citycode) != 0 and l_p_product_citycode <> l_city_code then          
            -- 手机号城市与货架城市不一致
              v_result := '0021';
              update od_recvwebbill t
                 set t.cre_errorcode = v_result 
               where t.req_orderid = v_orderid
                 and t.order_source = v_ordersource;
              commit;
              RETURN;
            
          end if;
       end if;   
    
  end if;

  if l_count1 = 0 then
    -- 货架不存在
    v_result := '0004';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  elsif l_count2 = 0 then
    -- 货架存在,不开放
    v_result := '0005';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_chk_money <> '0000' then
    --充值金额与货架、商品金额不符
    v_result := l_chk_money;
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  -- 产品与手机号不匹配: 查询话费商品货架信息 不匹配返回 1020
  begin
    select p.product_splitflag,
           s.product_type,
           s.product_id,
           decode(p.product_waittime, null, 0, p.product_waittime),
           decode(p.product_delaytime, null, 0, p.product_delaytime)
      INTO l_product_splitflag,
           l_product_type,
           l_product_id,
           l_product_waittime,
           l_product_delaytime
      from po_productonlineinfo p, po_productinfo s
     where p.online_id = v_online_id
       and p.product_id = s.product_id
       and s.product_ispid = l_isp_id
       and rownum = 1;

    update od_recvwebbill t
       set t.req_busitype = l_product_type,
           t.split_flag   = l_product_splitflag
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      -- 无数据的异常,不匹配
      v_result := '0002';
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      return;
  end;

  -- 插入订单充值记录
  --     判断订单号的唯一约束,异常捕获 代码 1011
  --     插入成功与否异常 代码 1000
  begin
    -- 生成订单号
    sp_hf_generateorderid(l_seq_result, l_order_id);
    if l_seq_result = '1111' then
      -- 错误码生成错误
      v_result := '1111';
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
    end if;

    -- 插入记录
       insert into od_orderinfo
          (req_orderid,
           req_saleserialid,
           product_onlineid,
           req_productid,
           req_ordersource,
           req_userid,
           req_accnum,
           req_ispid,
           req_provinceid,
           req_citycode,
           req_money,
           charge_status,
           req_datetime,
           req_saletime,
           charge_begintime, -- 要求开始处理时间
           charge_linetime,  -- 要求完成时间
           product_splitflag,
           req_paymoney,
           req_remark,
           req_servicetype)
        select
           l_order_id,
           v_orderid,
           v_online_id,
           l_product_id,
           v_ordersource,
           v_agentid,
           v_mobilenum,
           l_isp_id,
           l_province_id,
           l_city_code,
           v_chargeamount,
           2, --下单成功为2,等待处理
           sysdate,
           to_date(v_ordertime, 'YYYY-MM-DD HH24:MI:SS'),
           sysdate + l_product_waittime / (24 * 60 * 60), -- 加秒
           sysdate + l_product_waittime / (24 * 60 * 60) +
           (l_product_delaytime) / (24 * 60), -- 加分钟
           l_product_splitflag,
           v_paymount,
           v_mark,
           l_product_type
   from od_recvwebbill r
   where r.req_orderid = v_orderid
     and r.order_source = v_ordersource
     and sysdate < (l_begin_sysdate + 1 / 1440);

   l_insert_count := sql%rowcount;--是否插入成功

   if l_insert_count = 1  then
          v_result     := '0000';
          v_reqorderid := l_order_id;
   else
          v_result     := '0016';
   end if;
    --  更新收单表中的订单号
    update od_recvwebbill t
       set t.hf_orderid = v_reqorderid,
           t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;

    commit;

  EXCEPTION
    WHEN dup_val_on_index THEN
      -- 唯一键约束
      v_result     := '0007';
      v_reqorderid := '';
      ROLLBACK;
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
    WHEN OTHERS THEN
      v_result     := '1111';
      v_reqorderid := '';
      ROLLBACK;
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
  end;

EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    ROLLBACK;
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;

end;
/

prompt
prompt Creating procedure SP_OD_MOBILERECHARGE_BEGIN
prompt =============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_MOBILERECHARGE_BEGIN(v_Result       OUT VARCHAR2, --结果码
                                                       v_ProcessCount OUT VARCHAR2, --输出处理的订单数
                                                       v_hforderid    OUT VARCHAR2, --订单号，以逗号隔开
                                                       v_hfserialid   OUT VARCHAR2, --话费流水号，以逗号隔开
                                                       v_ispid        OUT VARCHAR2, --运营商标识，以逗号隔开
                                                       v_provinceid   OUT VARCHAR2, --省份id，以逗号隔开
                                                       v_citycode     OUT VARCHAR2, --城市编码，以逗号隔开
                                                       v_accnum       OUT VARCHAR2, --充值帐号，以逗号隔开
                                                       v_chargemoney  OUT VARCHAR2, --充值金额，以逗号隔开
                                                       v_channelid    OUT VARCHAR2, --充值渠道id，以逗号隔开
                                                       v_stationtype  OUT VARCHAR2, --站点类型，以逗号隔开
                                                       v_channeltype  OUT VARCHAR2, --充值渠道类型，以逗号隔开
                                                       v_servicetype  OUT VARCHAR2, --充值业务类型，以逗号隔开
                                                       v_ordersource  OUT VARCHAR2, --订单来源
                                                       v_useid        OUT VARCHAR2 --代理商id
                                                       )
/************************************************************
  产品名称：话费充值系统
  产品版本：V1.0
  模块名称：数据库脚本-充值触发,配置渠道，生成话费流水号
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-05-05
  修改内容：
  ************************************************************/
 AS
  l_ProcessCount NUMBER;
  l_hforderid    varchar2(30);
  l_hfserialid   varchar2(30);
  l_ispid        NUMBER;
  l_provinceid   NUMBER;
  l_citycode     varchar2(10);
  l_accnum       VARCHAR2(30);
  l_chargemoney  NUMBER(10, 2);
  l_channelid    NUMBER;
  l_stationtype  VARCHAR2(10);
  l_channeltype  NUMBER;

  l_status      NUMBER;
  l_nowtime     DATE;
  l_onlineid    NUMBER;
  l_reqmoney    NUMBER(10, 2);
  l_trycount    NUMBER;
  l_cardsource  NUMBER;
  l_linedate    DATE;
  l_province    NUMBER; --卡归属省或手机归属省，第三方渠道就用手机归属省，外呼卡归属省
  l_servicetype NUMBER;
  l_dbflag      NUMBER;

  l_ordersource NUMBER;
  l_useid       VARCHAR2(30);

  cursor cur_charge is
    select req_orderid,
           product_onlineid,
           req_provinceid,
           req_citycode,
           req_accnum,
           req_trycount,
           req_ispid,
           charge_linetime,
           req_servicetype,
           req_ordersource,
           req_userid
      from (select req_orderid,
                   product_onlineid,
                   req_productid,
                   req_provinceid,
                   req_citycode,
                   req_accnum,
                   charge_linetime,
                   req_trycount,
                   req_ispid,
                   req_servicetype,
                   req_ordersource,
                   req_userid
              from od_orderinfo a, ch_ordersourceinfo b
             where a.charge_linetime >= sysdate --没超过到帐时间
               and a.charge_begintime <= sysdate --慢充还未到开始处理时间的不主动进行处理
               and a.charge_status = 2 --等待充值
               and a.req_trycount <= 8 --当充值次数大于8次时，不能再充值
               and a.req_ordersource = b.os_id
             order by nvl(a.charge_finishtime, a.charge_begintime) asc,
                      b.os_pri asc,
                      a.req_trycount asc)
     where rownum <= 20;

BEGIN
  v_result       := '1007';
  v_ProcessCount := '';
  v_hforderid    := '';
  v_hfserialid   := '';
  v_ispid        := '';
  v_provinceid   := '';
  v_citycode     := '';
  v_accnum       := '';
  v_chargemoney  := '';
  v_channelid    := '';
  v_stationtype  := '';
  v_channeltype  := '';
  v_servicetype  := '';
  v_ordersource  := '';
  v_useid        := '';

  l_nowtime      := SYSDATE;
  l_province     := '-1';
  l_ProcessCount := 0;
  l_dbflag       := 0;

  --1、取待充订单
  -----------------------------------------------------------------------------
  OPEN cur_charge;
  LOOP
    <<loopstart>>
    FETCH cur_charge
      INTO l_hforderid, l_onlineid, l_provinceid, l_citycode, l_accnum, l_trycount, l_ispid, l_linedate, l_servicetype, l_ordersource, l_useid;
    EXIT WHEN cur_charge%NOTFOUND;
  
    BEGIN
      --取一条订单
      SELECT req_money - charge_finishmoney, charge_status
        INTO l_reqmoney, l_status
        FROM od_orderinfo
       WHERE req_orderid = l_hforderid
         FOR UPDATE NOWAIT;
    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        GOTO loopstart;
    END;
  
    --状态被其他服务更新了，此处就不做操作
    IF l_status <> 2 OR l_reqmoney = 0 THEN
      ROLLBACK;
      GOTO loopstart;
    END IF;
  
    IF l_trycount >= 8 THEN
      UPDATE od_orderinfo
         SET charge_status = 4
       WHERE req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --超过要求完成时间，订单状态改为失败为退款
    IF l_linedate < l_nowtime THEN
      UPDATE od_orderinfo
         SET charge_status = 4
       WHERE req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --需要充值金额为0，异常订单
    IF l_reqmoney = 0 THEN
      UPDATE od_orderinfo
         SET charge_status = 8
       WHERE req_orderid = l_hforderid;
      COMMIT;
      sp_sys_dberrorinfo_create(8,
                                'SP_OD_MOBILERECHARGE_BEGIN',
                                '话费充值出现异常订单',
                                l_hforderid,
                                '订单状态：' || to_char(l_status),
                                '需要充值金额：' || to_char(l_reqmoney));
      GOTO loopstart;
    END IF;
  
    --超过要求完成时间，订单状态改为失败未退款
    IF l_linedate < l_nowtime THEN
      UPDATE od_orderinfo
         SET charge_status = 4
       WHERE req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --取充值渠道
    sp_hf_getchannel(l_hforderid,
                     v_result,
                     l_channelid,
                     l_stationtype,
                     l_chargemoney);
    IF v_result <> 0 THEN
      --没有找到渠道
      UPDATE od_orderinfo p
         SET p.charge_status     = 8,
             p.req_errorcode     = '1007',
             p.charge_finishtime = SYSDATE
       WHERE p.req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --初始化卡来源
    IF l_channeltype = 2 THEN
      --短信
      l_cardsource := 4;
      l_province   := l_provinceid;
    ELSIF l_channeltype IN (0, 1, 3, 4, 6) THEN
      --外呼
      l_cardsource := -1;
      l_province   := -1;
    ELSE
      --第三方充值
      l_cardsource := 3;
      l_province   := l_provinceid;
    END IF;
  
    --更新订单状态为等待处理,增加订单表冲值次数
    UPDATE od_orderinfo
       SET charge_status = 3,
           req_trycount  = decode(req_trycount, null, 0, req_trycount) + 1
     WHERE req_orderid = l_hforderid;
  
    --插入fullnote表
    sp_hf_generatehfserial(v_result, l_hfserialid);
    INSERT INTO od_fullnote
      (hf_serialid,
       hf_orderid,
       hf_ispid,
       wait_money,
       full_money,
       finish_money,
       begin_time,
       hf_status,
       hf_acchannel,
       hf_cardnum,
       hf_cardpwd,
       card_source,
       hf_cardprovinceid,
       req_accnum)
    VALUES
      (l_hfserialid,
       l_hforderid,
       l_ispid,
       l_reqmoney,
       l_chargemoney,
       0,
       l_nowtime,
       1,
       l_channelid,
       '-1',
       '-1',
       l_cardsource,
       l_province,
       l_accnum);
    

  
    --取到一条，计数器加1
    l_ProcessCount := l_ProcessCount + 1;
    v_hforderid    := v_hforderid || l_hforderid || ',';
    v_hfserialid   := v_hfserialid || l_hfserialid || ',';
    v_ispid        := v_ispid || l_ispid || ',';
    v_provinceid   := v_provinceid || l_provinceid || ',';
    v_citycode     := v_citycode || l_citycode || ',';
    v_accnum       := v_accnum || l_accnum || ',';
    v_chargemoney  := v_chargemoney || l_chargemoney || ',';
    v_channelid    := v_channelid || l_channelid || ',';
    v_stationtype  := v_stationtype || l_stationtype || ',';
    v_channeltype  := v_channeltype || l_channeltype || ',';
    v_servicetype  := v_servicetype || l_servicetype || ',';
    v_ordersource  := v_ordersource || l_ordersource || ',';
    v_useid        := v_useid || l_useid || ',';
  
    COMMIT;
  
    IF l_ProcessCount >= 9 THEN
      --取够条数或取了10条退出
      EXIT;
    END IF;
  
  END LOOP;
  CLOSE cur_charge;

  --3、取订单完成
  v_ProcessCount := to_char(l_ProcessCount);
  IF l_ProcessCount = 0 THEN
    v_result       := '1007';
    v_ProcessCount := '0';
    v_hforderid    := '-1';
    v_hfserialid   := '-1';
    v_ispid        := '-1';
    v_provinceid   := '-1';
    v_citycode     := '-1';
    v_accnum       := '-1';
    v_chargemoney  := '-1';
    v_channelid    := '-1';
    v_stationtype  := '-1';
    v_channeltype  := '-1';
    v_servicetype  := '-1';
    v_ordersource  := '-1';
    v_useid        := '-1';
    RETURN;
  END IF;

  v_result := '0';

EXCEPTION
  WHEN OTHERS THEN
    IF l_ProcessCount = 0 THEN
      v_result       := '11111';
      v_ProcessCount := '0';
      v_hforderid    := '-1';
      v_hfserialid   := '-1';
      v_ispid        := '-1';
      v_provinceid   := '-1';
      v_citycode     := '-1';
      v_accnum       := '-1';
      v_chargemoney  := '-1';
      v_channelid    := '-1';
      v_stationtype  := '-1';
      v_channeltype  := '-1';
      v_servicetype  := '-1';
      v_ordersource  := '-1';
      v_useid        := '-1';
      sp_sys_dberrorinfo_create(3,
                                'SP_OD_MOBILERECHARGE_BEGIN',
                                sqlerrm,
                                l_hfserialid,
                                NULL,
                                NULL);
    ELSE
      v_result       := '0';
      v_ProcessCount := to_char(l_ProcessCount);
      sp_sys_dberrorinfo_create(3,
                                'SP_OD_MOBILERECHARGE_BEGIN',
                                sqlerrm,
                                l_hfserialid,
                                NULL,
                                NULL);
    END IF;
  
    COMMIT;
END;
/

prompt
prompt Creating procedure SP_OD_NEEDNOTICEORDER_BEGIN
prompt ==============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_NEEDNOTICEORDER_BEGIN(v_result                    out varchar2, --结果码
                                                          v_processcount              out varchar2, --输出处理的订单数
                                                          v_orderid                   out varchar2, --订单号
                                                          v_saleserialid              out varchar2, --前台支付订单号
                                                          v_chargestatus              out varchar2, --订单状态
                                                          v_chargefinishmoney         out varchar2, --实际完成金额
                                                          v_osbackurl                 out varchar2, --通知地址URL
                                                          v_oskey                     out varchar2  -- 通知密钥
                                                          ) --获取记录集合
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-获取需要通知到销售平台的订单信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-5-23
  修改内容：
  功能描述：a) 系统将状态为“充值成功/充值失败”通知状态为“未通知”通知次数没有超过阀值的订单加载到待通知队列
            v2 weisd 2011.08.02  修改查询语句，返回拼串后的字符串,由于有地址URL，拼串是用逗号隔开，注意
            v3 weisd         03  由于订单失败，就不会再次充值（除非人工修改），即可直接通知，删除到限制时间通知
  ************************************************************/
  l_processcount        number;
  l_hforderid           varchar2(30);
  l_saleserialid        varchar2(40);
  l_chargestatus        number;
  l_chargefinishmoney   number(10, 2);
  l_osbackurl           varchar2(200);
  l_oskey               varchar2(200);

  l_charge_linetime date;
  l_ec_notifyflag   number;
  l_ec_notifycount  number;
  l_ec_notifytime   date;
  l_os_maxcnt       number;

  cursor cur_resultcur is
    select req_orderid, --订单号
           req_saleserialid, --前台支付订单号
           charge_status, --订单状态
           charge_finishmoney, --实际完成金额
           os_backurl,
           os_key,
           os_maxcnt
      from (select t.req_orderid, --订单号
                   t.req_saleserialid, --前台支付订单号
                   t.charge_status, --订单状态
                   t.req_money, --订单金额
                   t.charge_finishmoney, --实际完成金额
                   t.req_paymoney, --实际支付金额
                   t.product_onlineid, --货架id
                   t.req_productid, --商品id
                   t.req_provinceid, --省份id
                   t.req_citycode, --城市id
                   t.req_accnum, --充值号码
                   t.req_trycount, --充值次数
                   t.req_ispid, --运营上id
                   t.req_servicetype, --业务类型
                   t.req_ordersource, --订单来源
                   t.req_userid, --用户id代理商id
                   o.os_backurl, --后台通知地址
                   o.os_key, --验证密钥
                   o.os_maxcnt
              from od_orderinfo t, CH_ORDERSOURCEINFO o
             where t.req_ordersource = o.os_id
               and t.charge_status in (0, 4, 5, 6)
               and (t.ec_notifyflag = 1 or --未通知
                   (t.ec_notifyflag = 3 and --通知失败
                   (t.ec_notifycount + 1) <= o.os_maxcnt and --小于阀值
                   (t.ec_notifytime + (1 / 1440)) <= sysdate)) --距离上次1分钟
             order by nvl(t.charge_finishtime, t.charge_begintime) asc,
                      t.req_trycount asc)
     where rownum <= 20;


BEGIN

  v_result              := '1007';
  v_processcount        := '';
  v_orderid             := '';
  v_saleserialid        := '';
  v_chargestatus        := '';
  v_chargefinishmoney   := '';
  v_osbackurl           := '';
  v_oskey               := '';

  l_processcount := 0;

  OPEN cur_resultcur;
  LOOP
    <<loopstart>>
    FETCH cur_resultcur
      INTO l_hforderid ,l_saleserialid,l_chargestatus,l_chargefinishmoney,l_osbackurl,l_oskey,l_os_maxcnt;
    EXIT WHEN cur_resultcur%NOTFOUND;

    BEGIN
      --取一条订单
      select o.charge_status, o.charge_linetime, o.ec_notifyflag, o.ec_notifycount, o.ec_notifytime
        into l_chargestatus, l_charge_linetime, l_ec_notifyflag, l_ec_notifycount, l_ec_notifytime
        from od_orderinfo o
       where o.req_orderid = l_hforderid
         FOR UPDATE NOWAIT;
    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        GOTO loopstart;
    END;


   if l_chargestatus in (0, 4, 5, 6) then
      null; --继续往下走
   else
    --不是通知状态的
      ROLLBACK;
      GOTO loopstart;
   end if;

   if (l_ec_notifyflag = 1 or (l_ec_notifyflag = 3 and (l_ec_notifycount + 1) <= l_os_maxcnt and (l_ec_notifytime + (1 / 1440)) <= sysdate))  then
      null ;-- 继续往下执行，符合通知要求
   else
    --不是通知状态的
      ROLLBACK;
      GOTO loopstart;
   end if;

    -- 到这里说明需要通知
    --次数+1
    update od_orderinfo o
       set o.ec_notifyflag = 2 ,
           o.ec_notifytime = sysdate ,
           o.ec_notifycount = o.ec_notifycount + 1
     where o.req_orderid = l_hforderid ;

    --取到一条，计数器加1
    l_processcount       := l_processcount + 1;
    v_orderid            := v_orderid || l_hforderid || ',';
    v_saleserialid       := v_saleserialid || l_saleserialid || ',';
    v_chargestatus       := v_chargestatus || l_chargestatus || ',';
    v_chargefinishmoney  := v_chargefinishmoney || l_chargefinishmoney || ',';
    v_osbackurl          := v_osbackurl || l_osbackurl || ',';
    v_oskey              := v_oskey || l_oskey || ',';

    COMMIT;

    IF l_processcount >= 9 THEN
      --取够条数或取了10条退出
      EXIT;
    END IF;

  END LOOP;
  CLOSE cur_resultcur;

  --3、充值记录取数完成
  v_processcount := to_char(l_processcount);
  if l_processcount = 0 then
    v_result                := '1007';
    v_processcount          := '0';
    v_orderid               := '-1';
    v_saleserialid          := '-1';
    v_chargestatus          := '-1';
    v_chargefinishmoney     := '-1';
    v_osbackurl             := '-1';
    v_oskey                 := '-1';

    return;
  end if;

  v_result := '0000';

EXCEPTION
  WHEN OTHERS THEN
    if l_processcount = 0 then
      v_result          := '1111';
      v_processcount    := '0';
      v_orderid               := '-1';
      v_saleserialid          := '-1';
      v_chargestatus          := '-1';
      v_chargefinishmoney     := '-1';
      v_osbackurl             := '-1';
      v_oskey                 := '-1';

      sp_sys_dberrorinfo_create(3,
                                'SP_OD_NEEDNOTICEORDER_BEGIN',
                                sqlerrm,
                                l_hforderid,
                                null,
                                null);
    else
      v_result       := '0';
      v_processcount := to_char(l_processcount);
      sp_sys_dberrorinfo_create(3,
                                'SP_OD_NEEDNOTICEORDER_BEGIN',
                                sqlerrm,
                                l_hforderid,
                                null,
                                null);
    end if;

    COMMIT;

END;
/

prompt
prompt Creating procedure SP_OD_NEEDNOTICEREVERSE_BEGIN
prompt ================================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_NEEDNOTICEREVERSE_BEGIN(v_pageNo    number, --查询页码
                                                          v_pageSize  number, --每页显示大小
                                                          v_status    in VARCHAR2, --要获取的状态
                                                          v_resultCur out HF_CURSOR.CURSOR_TYPE) --获取记录集合
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-获取需要通知到销售平台的冲正信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-8
  修改内容：
  功能描述：a) 系统将状态为“冲正成功，冲正失败”通知状态为“未通知”通知次数没有超过阀值的加载到待通知队列
             在java中处理返回的冲正结果
  ************************************************************/
  l_pageNo   number(10);
  l_pageSize number(10);
BEGIN

  --v_status  暂时无用
  if v_pageSize is null or v_pageSize = 0 then
  
    l_pageSize := 10;
  
  else
  
    l_pageSize := v_pageSize;
  
  end if;

  OPEN v_resultCur FOR
  
    select req_orderid,
           req_saleserialid,
           charge_status,
           charge_finishmoney,
           req_userid,
           os_backurl,
           os_key,
           hf_serialid,
           hf_cancelstatus,
           finish_money,
           req_trycount
      from (select t.req_orderid, --订单号
                   t.req_saleserialid, --前台支付订单号
                   t.charge_status, --订单状态
                   t.charge_finishmoney, --实际完成金额
                   t.req_userid, --用户id代理商id
                   o.os_backurl, --后台通知地址
                   o.os_key, --验证密钥
                   c.hf_serialid, --话费充值记录流水
                   c.hf_cancelstatus, --冲正结果状态
                   c.finish_money, --冲正金额
                   c.req_trycount, --冲正次数
                   c.req_errorcode --冲正错误码
              from OD_CANCELORDERCHARGE c,
                   od_orderinfo         t,
                   CH_ORDERSOURCEINFO   o
             where c.hf_orderid = t.req_orderid
               and t.req_ordersource = o.os_id
               and c.hf_cancelstatus in (0, 3) -- 充值成功、失败(2个状态)
               and (c.ec_notifyflag = 1 or (c.ec_notifyflag = 3 and
               c.ec_notifytime + (1 / 48) < sysdate))
             order by nvl(c.ec_notifytime,
                          to_date('2001-01-01 01:01:01',
                                  'YYYY-MM-DD HH24:MI:SS')) asc,
                      c.req_sarttime asc)
     where rownum <= l_pageSize;
END;
/

prompt
prompt Creating procedure SP_OD_PREPAREORDERINFO
prompt =========================================
prompt
create or replace procedure hf.SP_OD_PREPAREORDERINFO(v_online_id    in NUMBER, -- 货架ID
                                                  v_mobilenum    in VARCHAR2, -- 手机号码
                                                  v_chargeamount in NUMBER, -- 充值金额
                                                  v_paymount     in NUMBER, -- 订单金额
                                                  v_orderid      in VARCHAR2, -- 订单号
                                                  v_ordersource  in VARCHAR2, -- 订单来源
                                                  v_agentid      in VARCHAR2, -- 代理商ID(用户ID)
                                                  v_ordertime    in VARCHAR2, -- 销售平台订单时间
                                                  v_mark         in VARCHAR2, -- 保留字段
                                                  v_receivetime  in VARCHAR2, --收单平台接收时间点
                                                  v_reqorderid   out VARCHAR2, -- 充值订单信息订单号
                                                  v_result       out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 话费充值     创建预下单
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：weisd
  添加日期：2011-6-23
  添加内容：插入话费充值创建预下单 流程类似：SP_OD_CREATEORDERINFO
            返回创建结果, 订单状态为:1
            然后再根据前台的发货调用SP_OD_SENDORDERINFO
            v3 2011.6.24 添加同一手机号，同一充值金额1分钟内不可重复提交
            v4 2011.7.7  添加一个接收时间receivetime
                         添加补充验证信息,货架针对的用户未验证，检查相应充值服务是否正常
  ******************************************************************************/

 AS
  l_isp_id            NUMBER; -- 运营商标识
  l_province_id       NUMBER; -- 归属省份ID
  l_city_code         VARCHAR2(4); -- 归属地市区号
  l_product_id        NUMBER; -- 商品ID
  l_product_splitflag NUMBER; -- 产品分次标识
  l_product_type      NUMBER; -- 产品类型(业务类型)
  l_order_id          VARCHAR2(30); -- 话费充值订单信息中的订单号(主键)
  l_seq_result        VARCHAR2(30); -- 订单号返回码
  l_po_exist          VARCHAR2(4); -- 临时变量,以在收单信息中使用
  l_count1            NUMBER; -- 货架存在
  l_count2            NUMBER; -- 货架可用
  l_count3            NUMBER; -- 订单来源
  l_product_waittime  NUMBER(10); -- 初始等待时长，即接收请求后等待多长时间开始处理，单位为秒
  l_product_delaytime NUMBER(10); -- 允许到帐时长，单位为分钟
  l_content           NUMBER(10, 2); --商品步长
  l_minmoney          NUMBER(10, 2); --商品最小金额
  l_maxmoney          NUMBER(10, 2); --商品最大金额
  l_chk_money         VARCHAR2(4); -- 临时变量充值金额
  l_moblie_type       NUMBER; -- 判断是否是固话
  l_moblie_trycount   number; --是否1分钟内重复充值
  l_begin_sysdate     date; -- 进入系统时候的时间,用于判断执行插入时候是否超时
  l_insert_count      number;--插入语句涉及到的数据量
  l_phone_valid       VARCHAR2(4); -- 手机号码验证结果
  l_v_product_type    number(1);--临时存储货架产品类型
  l_p_product_type      NUMBER(1);--货架的产品类型
  l_p_product_ispid      number(1); --货架运营商
	l_p_product_provinceid number(2); --货架省份
	l_p_product_citycode   varchar2(10);--货架城市

begin
  v_result     := '1111';
  v_reqorderid := '';
  l_count1     := 0;
  l_count2     := 0;
  l_po_exist   := '';
  l_chk_money  := '';
  l_phone_valid := '0000';--初始手机号码通过，防止对非手机充值干扰
  --设置时间，防止下单到入库时间过长
  if v_receivetime is null then 
     l_begin_sysdate := sysdate;
  else 
     l_begin_sysdate := to_date(v_receivetime,'yyyy-mm-dd hh24:mi:ss');
  end if;

  -- 查询订单来源是否存在或可用: 返回 1030
  select count(1)
    into l_count3
    from ch_ordersourceinfo t
   where t.os_id = v_ordersource
     and t.os_status = '0'
     and rownum = 1;

  -- 查询货架存在或可用状态: 不存在返回 1040 不可用返回1050
  select count(1), nvl(sum(decode(t.product_status, 0, 1, null, 0, 0)), 0)
    into l_count1, l_count2
    from po_productonlineinfo t
   where t.online_id = v_online_id
     and t.order_source = v_ordersource
     and rownum = 1;

  -- 查询充值金额是否与货架、商品金额匹配
  begin
    select p.product_content, p.product_minmoney, p.product_maxmoney, 
           p.product_type,p.product_ispid, p.product_provinceid, p.product_citycode
      into l_content,l_minmoney,l_maxmoney,l_p_product_type,l_p_product_ispid,l_p_product_provinceid,l_p_product_citycode
      from po_productinfo p, po_productonlineinfo t
     where p.product_id = t.product_id
       and t.online_id = v_online_id
       and t.order_source = v_ordersource
       and t.product_status = 0 ;

     l_chk_money := CHECK_CHARGEMONEY_MATCH(l_content,l_minmoney,l_maxmoney,v_chargeamount);

  EXCEPTION
     WHEN NO_DATA_FOUND THEN
    -- 无数据的异常
     l_chk_money := '0010';
  end;

  -- 是否是固话，暂时不支持
  select instr(v_mobilenum,'-') into l_moblie_type from dual;

  if l_moblie_type is not null and l_moblie_type > 0 then
      l_v_product_type := 1;--其他非手机充值
  else
      l_v_product_type := 0;--是手机充值
      
      IF (v_mobilenum  LIKE '%13800138000%') or (substr(v_mobilenum,1,1) = '0') THEN --非手机充值号码
          l_phone_valid := '0022';   --该号码不支持充值
      ELSIF length(v_mobilenum) <> 11 THEN 
          l_v_product_type := 1 ;--非手机充值
          l_phone_valid := '0023';   --手机号码有误
      ELSE 
          -- 查询号码信息表: 查询号码信息表 不存在返回 1060
          begin
            SELECT isp_id, province_id, city_code
              into l_isp_id, l_province_id, l_city_code
              FROM sys_accsegment
             WHERE acc_segment = substr(v_mobilenum, 0, 7)
               and rownum = 1;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              -- 无数据的异常
              l_po_exist := '0006';
          end;
      END IF;

  end if;

  -- 是否同一号码、同一面值1分钟重复充值
  select count(1)
    into l_moblie_trycount
    from od_orderinfo t
   where t.req_accnum = v_mobilenum
     and t.req_money = v_chargeamount
     and t.req_datetime >= sysdate - 1 / 1440
     and t.req_datetime <= sysdate;


  -- 插入收单记录
  -- 收单时间 货架ID 充值手机号 运营商标识 业务类型 订单号 订单来源 用户ID 是否支持分次 产品金额
  begin
    insert into od_recvwebbill
      (recv_date,
       online_id,
       req_accnum,
       req_ispid,
       req_busitype,
       pre_errorcode,
       req_orderid,
       order_source,
       urser_id,
       split_flag,
       product_content)
    values
      (sysdate,
       v_online_id,
       v_mobilenum,
       l_isp_id,
       l_product_type,
       '0',
       v_orderid,
       v_ordersource,
       v_agentid,
       l_product_splitflag,
       v_chargeamount);

    v_result := '0000';
    commit;

  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      -- 唯一索引冲突
      v_result := '0007';
      ROLLBACK;
      return;
  end;

  -- 参数的判断
  if l_moblie_trycount > 0 then
    -- 是否重复充值
    v_result := '0019';
    update od_recvwebbill t
       set t.cre_errorcode = v_result,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_count3 = 0 then
    -- 订单来源不存在
    v_result := '0003';
    update od_recvwebbill t
       set t.cre_errorcode = v_result,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_v_product_type != 0  then 
      -- 暂时不支持固话
      v_result := '0012';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  elsif l_phone_valid <> '0000'  then 
     --手机号码验证有误
      v_result := l_phone_valid;
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  end if;
  
  if l_p_product_type <> l_v_product_type  then 
      --充值类型与货架产品类型不一致
      v_result := '0017';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  end if;

  if l_po_exist = '0006' then
    -- 号码信息不存在
    v_result := l_po_exist;
    update od_recvwebbill t
       set t.cre_errorcode = v_result,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  
  else 
     if l_p_product_ispid <> l_isp_id then 
        -- 号码与货架所属运营商不一致
        v_result := '0018';
        update od_recvwebbill t
           set t.cre_errorcode = v_result,
               t.pre_errorcode = v_result
         where t.req_orderid = v_orderid
           and t.order_source = v_ordersource;
        commit;
        RETURN;
     end if;
     
     if l_p_product_provinceid != 0  then
        
        if  l_p_product_provinceid <> l_province_id  then 
          -- 手机号省份与货架省份不一致
          v_result := '0020';
          update od_recvwebbill t
             set t.cre_errorcode = v_result,
                 t.pre_errorcode = v_result
           where t.req_orderid = v_orderid
             and t.order_source = v_ordersource;
          commit;
          RETURN;
          
        elsif to_number(l_p_product_citycode) != 0 and l_p_product_citycode <> l_city_code then          
          -- 手机号城市与货架城市不一致
            v_result := '0021';
            update od_recvwebbill t
               set t.cre_errorcode = v_result,
                   t.pre_errorcode = v_result
             where t.req_orderid = v_orderid
               and t.order_source = v_ordersource;
            commit;
            RETURN;
          
        end if;

     end if;
  
  end if;

  if l_count1 = 0 then
    -- 货架不存在
    v_result := '0004';
    update od_recvwebbill t
       set t.cre_errorcode = v_result ,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  elsif l_count2 = 0 then
    -- 货架存在,不开放
    v_result := '0005';
    update od_recvwebbill t
       set t.cre_errorcode = v_result ,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_chk_money <> '0000' then
    --充值金额与货架、商品金额不符
    v_result := l_chk_money;
    update od_recvwebbill t
       set t.cre_errorcode = v_result,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  -- 产品与手机号不匹配: 查询话费商品货架信息 不匹配返回 1020
  begin
    select p.product_splitflag,
           s.product_type,
           s.product_id,
           decode(p.product_waittime, null, 0, p.product_waittime),
           decode(p.product_delaytime, null, 0, p.product_delaytime)
      INTO l_product_splitflag,
           l_product_type,
           l_product_id,
           l_product_waittime,
           l_product_delaytime
      from po_productonlineinfo p, po_productinfo s
     where p.online_id = v_online_id
       and p.product_id = s.product_id
       and s.product_ispid = l_isp_id
       and rownum = 1;

    update od_recvwebbill t
       set t.req_busitype = l_product_type,
           t.split_flag   = l_product_splitflag
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      -- 无数据的异常,不匹配
      v_result := '0002';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      return;
  end;

  -- 插入订单充值记录
  --     判断订单号的唯一约束,异常捕获 代码 1011
  --     插入成功与否异常 代码 1000
  begin
    -- 生成订单号
    sp_hf_generateorderid(l_seq_result, l_order_id);
    if l_seq_result = '1111' then
      -- 错误码生成错误
      v_result := '1111';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
    end if;

    -- 插入记录
    insert into od_orderinfo
      (req_orderid,
       req_saleserialid,
       product_onlineid,
       req_productid,
       req_ordersource,
       req_userid,
       req_accnum,
       req_ispid,
       req_provinceid,
       req_citycode,
       req_money,
       charge_status,
       req_datetime,
       req_saletime,
       charge_begintime, -- 要求开始处理时间
       charge_linetime,
       　 -- 要求完成时间
       product_splitflag,
       req_paymoney,
       req_remark,
       req_servicetype)
   select
       l_order_id,
       v_orderid,
       v_online_id,
       l_product_id,
       v_ordersource,
       v_agentid,
       v_mobilenum,
       l_isp_id,
       l_province_id,
       l_city_code,
       v_chargeamount,
       1, --预下单
       sysdate,
       to_date(v_ordertime, 'YYYY-MM-DD HH24:MI:SS'),
       sysdate + l_product_waittime / (24 * 60 * 60), -- 加秒
       sysdate + l_product_waittime / (24 * 60 * 60) +
       (l_product_delaytime) / (24 * 60), -- 加分钟
       l_product_splitflag,
       v_paymount,
       v_mark,
       l_product_type
   from od_recvwebbill r
   where r.req_orderid = v_orderid
     and r.order_source = v_ordersource
     and sysdate < (l_begin_sysdate + 1 / 1440);

   l_insert_count := sql%rowcount;--是否插入成功

   if l_insert_count = 1  then
          v_result     := '0000';
          v_reqorderid := l_order_id;
   else
          v_result     := '0016';
   end if;

    --  更新收单表中的订单号
    update od_recvwebbill t
       set t.hf_orderid = v_reqorderid,
           t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;

    commit;

  EXCEPTION
    WHEN dup_val_on_index THEN
      -- 唯一键约束
      v_result     := '0007';
      v_reqorderid := '';
      ROLLBACK;
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
    WHEN OTHERS THEN
      v_result     := '1111';
      v_reqorderid := '';
      ROLLBACK;
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
  end;

EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    ROLLBACK;
    update od_recvwebbill t
       set t.cre_errorcode = v_result,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;

end;
/

prompt
prompt Creating procedure SP_OD_RECHARGEFAILURE
prompt ========================================
prompt
create or replace procedure hf.SP_OD_RECHARGEFAILURE(v_hfserialid      in VARCHAR2, -- 话费流水号
                                                  v_channelserialid in VARCHAR2, -- 渠道流水号
                                                  v_sendserialid    in VARCHAR2, -- 发送给对方流水号
                                                  v_dealtime        in VARCHAR2, -- 处理时间
                                                  v_errorcode       in VARCHAR2, -- 错误码
                                                  v_result          out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 渠道返回话费充值失败处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-5-5
  添加内容：(1)  修改话费充值记录表状态为“失败”，记录完成时间，错误码等；
            (2)  判断订单是否可以继续充值；
            (3)  如果可以后，匹配其它的可充值渠道，生成新的话费充值记录，进行二次充值；
            (4)  如果不可以，修改话费订单表及收单表中的状态、错误友及相关信息；
            (5)  修改渠道失败计数表中相关信息
  
            v2 weisd 5.26 添加字段 sendserialid 发送给对方的流水
            v3 weisd 6.3 充值失败 后如果能再充值 再修改状态使之能继续充值
            v4 weisd 6.17 如果一笔充值失败会连续发送8次，故判断是否是已经失败的，且时间不是10分以内的
  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- 话费流水号存在
  l_hf_status          NUMBER(2); -- 话费充值状态
  l_hf_orderid         VARCHAR2(30); -- 话费订单号
  l_hf_acchannel       NUMBER(10); -- 渠道ID
  l_time               NUMBER(10); -- 是否可以继续冲值的时间差,以秒计算
  l_req_money          NUMBER(10, 2); -- 订单金额
  l_charge_finishmoney NUMBER(10, 2); -- 实际到账金额
  l_warninfo           VARCHAR2(200); -- 报警信息
  l_ch_failcount       NUMBER; -- 渠道失败计数
  l_req_trycount       NUMBER; -- 订单充值次数
  l_before_failtime    date; --上次失败时间
  l_product_delaytime  NUMBER(10); --货架到帐时长
  l_charge_linetime    date; --限制到帐时长

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  -- 查询话费流水号在话费冲值交易表中是否存在或可处理
  -- 当前交易状态 0：成功 1：等待 2：正在处理 3：失败 4：超时  9：结果未知，需人工审核
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- 话费流水号为不存在时的处理
  if l_od_fullnot_count = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '渠道返回充值失败,但话费充值记录中话费流水号' || v_hfserialid || '不存在';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态状态为成功时的处理
  if l_hf_status = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于成功状态，但渠道返回冲值失败消息!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态状态为部分成功时的处理
  if l_hf_status = 6 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid ||
                  '处于部分成功状态，但渠道返回冲值失败消息!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态状态为失败时的处理
  if l_hf_status = 3 then
    v_result := '0000';
    RETURN;
  end if;

  begin
    -- 话费流水号状态为等待时报警
    if l_hf_status = 1 then
      -- 向报警表中添加报警记录
      l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid ||
                    '处于等待状态，但渠道返回冲值失败消息!';
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    end if;
  
    -- 更新话费充值记录表
    update od_fullnote t
       set t.hf_status        = 3,
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.hf_errorcode     = v_errorcode,
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid
    -- t.trycount         = decode(t.trycount, null, 0, t.trycount) + 1 不在这里添加
     where t.hf_serialid = v_hfserialid;
  
    -- 查询出订单相关信息
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode((sysdate - d.charge_linetime) * 24 * 24 * 60,
                  null,
                  0,
                  (sysdate - d.charge_linetime) * 24 * 24 * 60),
           decode(d.req_money, null, 0, d.req_money),
           decode(d.charge_finishmoney, null, 0, d.charge_finishmoney),
           d.req_trycount,
           d.charge_finishtime,
           d.charge_linetime,
           p.product_delaytime
      into l_hf_orderid,
           l_hf_acchannel,
           l_time,
           l_req_money,
           l_charge_finishmoney,
           l_req_trycount,
           l_before_failtime,
           l_charge_linetime,
           l_product_delaytime
      from od_fullnote t, od_orderinfo d, PO_PRODUCTONLINEINFO p
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and d.product_onlineid = p.online_id(+)
       and rownum = 1;
  
    -- 判断订单号是否存在
    if l_hf_orderid is not null then
      -- 判断订单是否可以继续充值,超过8次,增加次数在取渠道那边
      if l_time >= 0 or l_req_trycount >= 8 then
        -- 不能继续冲值,修改订单状态: 4 失败未退款状态
        update od_orderinfo t
           set t.charge_status     = 4,
               t.req_errorcode     = v_errorcode,
               t.charge_finishtime = sysdate
         where t.req_orderid = l_hf_orderid;
        -- 更新收单信息的发货错误码
        update od_recvwebbill t
           set t.cnf_errorcode = v_errorcode
         where t.hf_orderid = l_hf_orderid;
      
      else
      
        if l_product_delaytime <= 5 then
          --5分钟以内的
          if (l_charge_linetime >= sysdate + 1 / 960) then
            -- 1.5分后到限制
            UPDATE od_orderinfo t
               SET t.charge_status     = 2,
                   t.product_slchannel = 3,
                   t.charge_begintime  = sysdate + 1 / 1440 --加一分钟
             where t.req_orderid = l_hf_orderid;
          else
            update od_orderinfo t
               set t.charge_status     = 4,
                   t.req_errorcode     = v_errorcode,
                   t.charge_finishtime = sysdate
             where t.req_orderid = l_hf_orderid;
            -- 更新收单信息的发货错误码
            update od_recvwebbill t
               set t.cnf_errorcode = v_errorcode
             where t.hf_orderid = l_hf_orderid;
          end if;
        
        else
          --判断订单的允许到账时长距离当前时间间隔，
          --如果是5分钟之内的，将订单状态置为失败，不再充值
          --如是是5分钟以上的，且没有超过限制时间-5分钟，延长orderinfo.charge_begintime时长3分钟，等待下一次匹配渠道
          if (l_charge_linetime >= sysdate + 4 / 1440) then
            -- 4分后到限制
            UPDATE od_orderinfo t
               SET t.charge_status     = 2,
                   t.product_slchannel = 3, --将优先原则改成无原则,以便下次能取到不同的渠道再次充值
                   t.charge_begintime  = sysdate + 1 / 480
             where t.req_orderid = l_hf_orderid;
          else
            update od_orderinfo t
               set t.charge_status     = 4,
                   t.req_errorcode     = v_errorcode,
                   t.charge_finishtime = sysdate
             where t.req_orderid = l_hf_orderid;
            -- 更新收单信息的发货错误码
            update od_recvwebbill t
               set t.cnf_errorcode = v_errorcode
             where t.hf_orderid = l_hf_orderid;
          end if;
        end if;
      
      end if;
    
    end if;
    -- 修改渠道失败计数表中相关信息
    select count(1)
      into l_ch_failcount
      from ch_failcount t
     where t.channel_id = l_hf_acchannel;
  
    if l_ch_failcount < 1 then
    
      insert into CH_FAILCOUNT
        (CHANNEL_ID, FAILCOUNT)
      values
        (l_hf_acchannel, 1);
    
    else
    
      update ch_failcount t
         set t.failcount = t.failcount + 1, t.last_update_date = sysdate
       where t.channel_id = l_hf_acchannel;
    
    end if;
    commit;
    v_result := '0000';
  
  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;

end;
/

prompt
prompt Creating procedure SP_OD_RECHARGEPARTSUCC
prompt =========================================
prompt
create or replace procedure hf.SP_OD_RECHARGEPARTSUCC(v_hfserialid      in VARCHAR2, -- 话费流水号
                                                  v_channelserialid in VARCHAR2, -- 渠道流水号
                                                  v_sendserialid    in VARCHAR2, -- 发送给对方流水号
                                                  v_dealamount      in NUMBER, -- 实际充值金额
                                                  v_dealtime        in VARCHAR2, -- 处理时间
                                                  v_errorcode       in VARCHAR2, -- 错误码
                                                  v_result          out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 渠道返回话费充值'部分成功'处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：weisd
  添加日期：2011-6
  添加内容：(1) 修改话费充值记录表状态为“部分成功”，记录完成时间，实际处理金额、本次充值金额，成本，渠道处理时间，完成时间等；
            (2) 修改话费订单表及收单表中的状态及相关信息；
            (3) 修改订单来源基本信息中当前走撮合和不走撮合笔数
            (4) 修改渠道信息表中的当日交易量

  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- 话费流水号存在
  l_hf_status          NUMBER(2); -- 话费充值状态
  l_hf_orderid         VARCHAR2(30); -- 话费订单号
  l_hf_acchannel       NUMBER(10); -- 渠道ID
  l_req_money          NUMBER(10,2); -- 订单金额
  l_charge_finishmoney NUMBER(10,2); -- 实际到账金额
  l_warninfo           VARCHAR2(200); -- 报警信息
  l_hf_cost            NUMBER(5, 2); -- 渠道成本
  l_dealamount         NUMBER(10,2); --临时实际充值金额
  l_old_finishamount     NUMBER(10,2); --已经部分成功的金额

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  l_dealamount := v_dealamount; --替换原来的

  -- 查询话费流水号在话费冲值交易表中是否存在或可处理
  -- 当前交易状态 0：成功 1：等待 2：正在处理 3：失败 4：超时 6：部分成功  9：结果未知，需人工审核
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- 话费流水号为不存在时的处理
  if l_od_fullnot_count = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '渠道返回充值部分成功,但话费充值流水号' || v_hfserialid || '不存在';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态状态为成功或失败
  if l_hf_status = 0 or l_hf_status = 3 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值流水号:' || v_hfserialid || '处于成功或失败状态:'|| l_hf_status ||',但渠道返回冲值部分成功消息:'||v_errorcode;
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 部分成功金额无效,或者为0， 部分成功必须传金额
  if l_dealamount is null or l_dealamount = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值流水号:' || v_hfserialid ||'渠道返回冲值部分成功,但是部分充值金额有误:'||l_dealamount;
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

      -- 暂时只能是一次部分成功
  if l_hf_status = 6 then
     select t.finish_money
         into l_old_finishamount
     from od_fullnote t
     where t.hf_serialid = v_hfserialid;

     if l_dealamount < l_old_finishamount  then
         -- 向报警表中添加报警记录
        l_warninfo := '话费充值流水号:' || v_hfserialid || '处于部分状态:'|| l_hf_status ||',但渠道返回冲值部分成功消息,返回金额比原金额小,金额为:'||l_dealamount;
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
        v_result := '0000';
        commit;
        RETURN;
     elsif l_dealamount = l_old_finishamount  then
       null;
       RETURN;
     else
       l_dealamount :=  l_dealamount -  l_old_finishamount;--原来已经加的不再计数
     end if;
  end if;


  begin
    -- 话费流水号状态为等待时报警
    if l_hf_status = 1 then
      -- 向报警表中添加报警记录
      l_warninfo := '话费充值流水号:' || v_hfserialid || '处于等待状态，但渠道返回冲值部分成功消息';
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    end if;

    -- 查询出订单号相关信息: 订单号,订单金额,实际到账金额
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode(d.req_money, null, 0, d.req_money),
           decode(d.charge_finishmoney, null, 0, d.charge_finishmoney)
      into l_hf_orderid, l_hf_acchannel, l_req_money, l_charge_finishmoney
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    -- 渠道信息中查询出成本
    select t.channel_cost
      into l_hf_cost
      from ch_channelinfo t
     where t.channel_id = l_hf_acchannel;

    if l_hf_cost is null then
      l_hf_cost := 1;
    end if;

    if l_dealamount is null then
         l_dealamount := 0; --金额不能为空和0，前面已经判断返回
     end if;
    -- 更新话费充值记录表
    update od_fullnote t
       set t.hf_status        = 6, --部分成功
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.hf_errorcode     = v_errorcode,
           t.finish_money     = decode(v_dealamount, -- 多次部分成功只能用传进来的
                                       null,
                                       0,
                                       v_dealamount),
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid,
           t.hf_cost          = v_dealamount * (l_hf_cost/100) --  增加成本计算
     where t.hf_serialid = v_hfserialid;

    -- 订单号存在
    if l_hf_orderid is not null then
      if (l_dealamount + l_charge_finishmoney) > l_req_money then
        -- 实际到账金额大于订单金额,修改订单状态为成功
        update od_orderinfo t
           set t.charge_status      = 0,
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) -- 这里不再 //+ 1
         where t.req_orderid = l_hf_orderid;

        -- 向报警表中添加报警记录
        l_warninfo := '话费充值流水号:' || v_hfserialid || '中订单金额为:' ||
                      l_req_money || '元,当前实际到账金额' || l_charge_finishmoney ||
                      ',渠道部分成功冲充值' || l_dealamount || '元,此次冲值中实际到账金额已大于订单金额!';
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
      elsif (l_dealamount + l_charge_finishmoney) < l_req_money then
        -- 实际到账金额小于订单金额,累加金额
        -- 这里是否还需要添加部分成功 这个流程要怎么走
        update od_orderinfo t
           set t.charge_status      = 6,
               t.charge_finishtime  = sysdate,
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount)
         where t.req_orderid = l_hf_orderid;
      else
        update od_orderinfo t
           set t.charge_status      = 0,
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.charge_finishtime  = sysdate,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) --// + 1
         where t.req_orderid = l_hf_orderid;
      end if;
    end if;

    -- 修改订单来源基本信息中当前走撮合和不走撮合笔数: 暂不管

    -- 修改渠道信息表中的当日交易量
    update ch_channelinfo t
       set t.channel_money = t.channel_money + l_dealamount
     where t.channel_id = l_hf_acchannel;

    commit;
    v_result := '0000';

  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;

end;
/

prompt
prompt Creating procedure SP_OD_RECHARGESUCCESS
prompt ========================================
prompt
create or replace procedure hf.SP_OD_RECHARGESUCCESS(v_hfserialid      in VARCHAR2, -- 话费流水号
                                                  v_channelserialid in VARCHAR2, -- 渠道流水号
                                                  v_sendserialid    in VARCHAR2, -- 发送给对方流水号
                                                  v_dealtime        in VARCHAR2, -- 处理时间
                                                  v_dealamount      in NUMBER, -- 实际充值金额
                                                  v_result          out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 渠道返回话费充值成功处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-5-4
  添加内容：(1) 修改话费充值记录表状态为“成功”，记录完成时间，实际处理金额、本次充值金额，成本，渠道处理时间，完成时间等；
            (2) 修改话费订单表及收单表中的状态及相关信息；
            (3) 修改订单来源基本信息中当前走撮合和不走撮合笔数
            (4) 修改渠道信息表中的当日交易量

            v2 weisd 5.26 添加字段 sendserialid 发送给对方的流水
            v3 weisd 6.8  补充一个临时 l_dealamount, 可能外面不传进来，那么最终修改订单表时候 实际完成金额由问题
                 发现遗留问题： if l_hf_cost is not null then  算成成本 等于 1
  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- 话费流水号存在
  l_hf_status          NUMBER(2); -- 话费充值状态
  l_hf_orderid         VARCHAR2(30); -- 话费订单号
  l_hf_acchannel       NUMBER(10); -- 渠道ID
  l_req_money          NUMBER(10,2); -- 订单金额
  l_charge_finishmoney NUMBER(10,2); -- 实际到账金额
  l_warninfo           VARCHAR2(200); -- 报警信息
  l_hf_cost            NUMBER(5, 2); -- 渠道成本
  l_dealamount         NUMBER(10,2); --临时实际充值金额

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  l_dealamount := v_dealamount; --替换原来的

  -- 查询话费流水号在话费冲值交易表中是否存在或可处理
  -- 当前交易状态 0：成功 1：等待 2：正在处理 3：失败 4：超时  9：结果未知，需人工审核
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- 话费流水号为不存在时的处理
  if l_od_fullnot_count = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '渠道返回充值成功,但话费充值流水号' || v_hfserialid || '不存在';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- 话费流水号当前状态为成功时的处理
  if l_hf_status = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值流水号:' || v_hfserialid || '处于成功状态，但渠道返回冲值成功消息';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  begin
    -- 话费流水号状态为等待时报警
    if l_hf_status = 1 then
      -- 向报警表中添加报警记录
      l_warninfo := '话费充值流水号:' || v_hfserialid || '处于等待状态，但渠道返回冲值成功消息';
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    end if;

    -- 查询出订单号相关信息: 订单号,订单金额,实际到账金额
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode(d.req_money, null, 0, d.req_money),
           decode(d.charge_finishmoney, null, 0, d.charge_finishmoney)
      into l_hf_orderid, l_hf_acchannel, l_req_money, l_charge_finishmoney
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    -- 渠道信息中查询出成本
    select t.channel_cost
      into l_hf_cost
      from ch_channelinfo t
     where t.channel_id = l_hf_acchannel;

    if l_hf_cost is null then
      l_hf_cost := 1;
    end if;

    if l_dealamount is null then
          select t.full_money  --外面没传金额，则查询充值记录表的 只能取这个
            into l_dealamount
          from od_fullnote t where t.hf_serialid = v_hfserialid;
     end if;
    -- 更新话费充值记录表
    update od_fullnote t
       set t.hf_status        = 0,
           t.hf_errorcode     = '0000',
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.finish_money     = decode(v_dealamount,
                                       null,
                                       t.full_money,
                                       v_dealamount),
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid,
           t.hf_cost          = l_dealamount * (l_hf_cost/100) --  增加成本计算 6.22修改
     where t.hf_serialid = v_hfserialid;



    -- 订单号存在
    if l_hf_orderid is not null then
      if (l_dealamount + l_charge_finishmoney) > l_req_money then
        -- 实际到账金额大于订单金额,修改订单状态为成功
        update od_orderinfo t
           set t.charge_status      = 0,
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) -- 这里不再 //+ 1
         where t.req_orderid = l_hf_orderid;

        -- 向报警表中添加报警记录
        l_warninfo := '话费充值流水号:' || v_hfserialid || '中订单金额为:' ||
                      l_req_money || '元,当前实际到账金额' || l_charge_finishmoney ||
                      ',渠道成功冲充值' || l_dealamount || '元,此次冲值中实际到账金额已大于订单金额!';
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
      elsif (l_dealamount + l_charge_finishmoney) < l_req_money then
        -- 实际到账金额小于订单金额,累加金额
        update od_orderinfo t
           set t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) -- //+ 1
         where t.req_orderid = l_hf_orderid;
      else
        update od_orderinfo t
           set t.charge_status      = 0,
               t.req_errorcode      = '0000',
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.charge_finishtime  = sysdate,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) --// + 1
         where t.req_orderid = l_hf_orderid;
      end if;
    end if;

    -- 修改订单来源基本信息中当前走撮合和不走撮合笔数: 暂不管

    -- 修改渠道信息表中的当日交易量
    update ch_channelinfo t
       set t.channel_money = t.channel_money + l_dealamount
     where t.channel_id = l_hf_acchannel;

    commit;
    v_result := '0000';

  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;

end;
/

prompt
prompt Creating procedure SP_OD_RECHARGETIMEOUT
prompt ========================================
prompt
create or replace procedure hf.SP_OD_RECHARGETIMEOUT(v_hfserialid      in VARCHAR2, -- 话费流水号
                                                  v_channelserialid in VARCHAR2, -- 渠道流水号
                                                  v_sendserialid    in VARCHAR2, -- 发送给对方流水号
                                                  v_dealtime        in VARCHAR2, -- 处理时间
                                                  v_errorcode       in VARCHAR2, -- 错误码
                                                  v_result          out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 渠道返回话费充值结果超时
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-5-10
  添加内容： (1) 当前时间如果小于订单要求完成时间，则等待渠道返回或走渠道充值结果查询流程；
             (2) 当前时间如果大于等于订单要求完成时间，则走结果未知人工处理流程；

             v2 weisd 5.26 添加字段 sendserialid 发送给对方的流水
                添加错误码
  ******************************************************************************/

 AS
  l_od_fullnot_count NUMBER; -- 话费流水号存在
  l_hf_status        NUMBER(2); -- 话费充值状态
  l_hf_orderid       VARCHAR2(30); -- 话费订单号
  l_hf_acchannel     NUMBER(10); -- 渠道ID
  l_time             NUMBER(10); -- 是否可以继续冲值的时间差,以秒计算
  l_warninfo         VARCHAR2(200); -- 报警信息

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  -- 查询话费流水号在话费冲值交易表中是否存在或可处理
  -- 当前交易状态 0：成功 1：等待 2：正在处理 3：失败 4：超时  9：结果未知，需人工审核
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- 话费流水号为不存在时的处理
  if l_od_fullnot_count = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '渠道返回充值超时,但话费充值记录中话费流水号' || v_hfserialid || '不存在';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态为成功时的处理
  if l_hf_status = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于成功状态，但渠道返回冲值超时!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

    -- 话费流水号状态为部分成功时的处理
  if l_hf_status = 6 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于部分成功状态，但渠道返回冲值超时!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态为失败时的处理
  if l_hf_status = 3 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于失败状态，但渠道返回冲值超时!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态为超时的处理
  if l_hf_status = 4 then
    v_result := '0000';
    commit;
    RETURN;
  end if;

  begin
    -- 话费流水号状态为等待时报警
    if l_hf_status = 1 then
      -- 向报警表中添加报警记录
      l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于等待状态，但渠道返回冲值超时!';
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    end if;

    -- 查询出订单相关信息
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode((sysdate - d.charge_linetime) * 24 * 24 * 60,
                  null,
                  0,
                  (sysdate - d.charge_linetime) * 24 * 24 * 60)
      into l_hf_orderid, l_hf_acchannel, l_time
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    -- 当前时间如果小于订单要求完成时间
    if l_time < 0 then
      -- 更新话费充值记录表,状态为4:超时
      update od_fullnote t
         set t.hf_status        = 4,
             t.chfinish_time    = to_date(v_dealtime,
                                          'YYYY-MM-DD HH24:MI:SS'),
             t.finish_time      = sysdate,
             t.hf_errorcode     = v_errorcode,
             t.channel_serialid = v_channelserialid
             --,t.trycount         = decode(t.trycount, null, 0, t.trycount) + 1
       where t.hf_serialid = v_hfserialid;
    else
      -- 更新话费充值记录表,状态为9:结果未知
      update od_fullnote t
         set t.hf_status        = 9,
             t.chfinish_time    = to_date(v_dealtime,
                                          'YYYY-MM-DD HH24:MI:SS'),
             t.finish_time      = sysdate,
             t.hf_errorcode     = v_errorcode,
             t.channel_serialid = v_channelserialid,
             t.send_serialid    = v_sendserialid
             --,t.trycount         = decode(t.trycount, null, 0, t.trycount) + 1
       where t.hf_serialid = v_hfserialid;
      -- 更新话费订单表,状态为8:结果未知,需要人工干预
      update od_orderinfo t
         set t.charge_status = 8, t.charge_finishtime = sysdate,t.req_errorcode = v_errorcode
       where t.req_orderid = l_hf_orderid;
    end if;

    commit;
    v_result := '0000';

  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;

end SP_OD_RECHARGETIMEOUT;
/

prompt
prompt Creating procedure SP_OD_RECHARGEUNKNOW
prompt =======================================
prompt
create or replace procedure hf.SP_OD_RECHARGEUNKNOW( v_hfserialid      in VARCHAR2, -- 话费流水号
                                                  v_channelserialid in VARCHAR2, -- 渠道流水号
                                                  v_sendserialid    in VARCHAR2, -- 发送给对方流水号
                                                  v_dealtime        in VARCHAR2, -- 处理时间
                                                  v_errorcode       in VARCHAR2, -- 错误码
                                                  v_result          out VARCHAR2 -- 结果代码
                                                  )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 渠道返回话费充值结果未知处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-5-9
  添加内容：(1) 修改话费充值记录表状态为“结果未知”等；
            (2) 修改话费订单表及收单表中的状态及相关信息；

            v2 weisd 5.26 添加字段 sendserialid 发送给对方的流水
  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- 话费流水号存在
  l_hf_status          NUMBER(2); -- 话费充值状态
  l_hf_orderid         VARCHAR2(30); -- 话费订单号
  l_hf_acchannel       NUMBER(10); -- 渠道ID
  l_time               NUMBER(10); -- 是否可以继续冲值的时间差,以秒计算
  l_req_money          NUMBER(10,2); -- 订单金额
  l_charge_finishmoney NUMBER(10,2); -- 实际到账金额
  l_warninfo           VARCHAR2(200); -- 报警信息

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  -- 查询话费流水号在话费冲值交易表中是否存在或可处理
  -- 当前交易状态 0：成功 1：等待 2：正在处理 3：失败 4：超时  9：结果未知，需人工审核
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- 话费流水号为不存在时的处理
  if l_od_fullnot_count = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '渠道返回充值未知结果,但话费充值记录中话费流水号' || v_hfserialid || '不存在';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态为成功时的处理
  if l_hf_status = 0 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于成功状态，但渠道返回冲值结果未知!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

    -- 话费流水号状态为部分成功时的处理
  if l_hf_status = 6 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于部分成功状态，但渠道返回冲值结果未知!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态为失败时的处理
  if l_hf_status = 3 then
    -- 向报警表中添加报警记录
    l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid || '处于失败状态，但渠道返回冲值结果未知!';
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- 话费流水号状态为失败时的处理
  if l_hf_status = 9 then
    v_result := '0000';
    RETURN;
  end if;

   begin
    -- 话费流水号状态为等待时报警
    if l_hf_status = 1 then
      -- 向报警表中添加报警记录
      l_warninfo := '话费充值记录中话费流水号:' || v_hfserialid ||
                    '处于等待状态，但渠道返回冲值结果未知!';
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    end if;

    -- 更新话费充值记录表,状态为9:结果未知
    update od_fullnote t
       set t.hf_status        = 9,
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.hf_errorcode     = v_errorcode,
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid
           --,t.trycount         = decode(t.trycount, null, 0, t.trycount) + 1
     where t.hf_serialid = v_hfserialid;

    -- 查询出订单相关信息
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode((sysdate - d.charge_linetime) * 24 * 24 * 60,
                  null,
                  0,
                  (sysdate - d.charge_linetime) * 24 * 24 * 60),
           decode(d.req_money, null, 0, d.req_money),
           decode(d.charge_finishmoney, null, 0, d.charge_finishmoney)
      into l_hf_orderid,
           l_hf_acchannel,
           l_time,
           l_req_money,
           l_charge_finishmoney
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    -- 判断订单号是否存在
    if l_hf_orderid is not null then
        -- 修改订单状态: 8 需要人工处理
        update od_orderinfo t
             set t.charge_status     = 8,
                 t.charge_finishtime = sysdate,
                 t.req_errorcode = v_errorcode

        where t.req_orderid = l_hf_orderid;
    end if;

    commit;
    v_result := '0000';

  EXCEPTION
    WHEN OTHERS THEN
      v_result := '1111';
      ROLLBACK;
  end;

end;
/

prompt
prompt Creating procedure SP_OD_REVERSE_DEAL
prompt =====================================
prompt
create or replace procedure hf.SP_OD_REVERSE_DEAL(v_hforderid in VARCHAR2, -- 订单号
                                               v_result    out VARCHAR2 -- 结果代码
                                               )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 判断订单是否可以充正
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：liujiangtao
  添加日期：2011-5-4
  添加内容：(1) 判断订单是否存在并状态
            (2) 获取订单话费流水记录的状态
            (3) 获取渠道的可充正状态
            (4) 返回相应代码给后台程序
            
          v2  weisd 修改结果返回码
  ******************************************************************************/
 AS
  l_count1 NUMBER; -- 订单可用
  l_count2 NUMBER; -- 订单状态
  l_count3 NUMBER; -- 话费流水的渠道是否可充值

begin
  v_result := '0000';
  begin
  
    -- 查询订单信息
    select count(1),
           nvl(sum(decode(t.charge_status, null, 3, t.charge_status)), 3)
      into l_count1, l_count2
      from od_orderinfo t
     where t.req_orderid = v_hforderid
       and rownum = 1;
  
    if l_count1 <= 0 then
      -- 订单不存在
      v_result := '2025';
      RETURN;
    end if;
  
    if l_count2 != 0 then
      -- 订单状态不为成功
      v_result := '2026';
      RETURN;
    end if;
    
    --  查询话费流水和渠道可充值
    SELECT COUNT(1)
      into l_count3
      from od_fullnote o, ch_channelinfo c
     where o.hf_orderid = v_hforderid
       and o.hf_acchannel = c.channel_id
       and o.hf_status = 0
       and c.channel_isreversed = 0
       and rownum = 1;
       
    if l_count3=0 then
       -- 没有找到可充正渠道信息: 渠道不充许充正(一个或多个渠道不允许充正)
       v_result := '2027';
       RETURN;
    end if;
  end;

  EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    ROLLBACK;
    
end SP_OD_REVERSE_DEAL;
/

prompt
prompt Creating procedure SP_OD_SEARCHREVERSE_BEGIN
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_SEARCHREVERSE_BEGIN(v_pageNo    number, --查询页码
                                                      v_pageSize  number, --每页显示大小
                                                      v_status    in VARCHAR2, --要获取的状态
                                                      v_resultCur out HF_CURSOR.CURSOR_TYPE) --获取记录集合
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-获取需要查询冲正结果状态未知的
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-10
  修改内容：
  功能描述：v1：查询冲正结果状态未知的

  ************************************************************/
  l_pageNo   number(10);
  l_pageSize number(10);
BEGIN

  if v_pageSize is null or v_pageSize = 0 then

    l_pageSize := 10;

  else

    l_pageSize := v_pageSize;

  end if;

  OPEN v_resultCur FOR
    select hf_serialid, --订单号
           hf_orderid, --前台支付订单号
           channel_serialid,
           send_serialid,
           hf_status,
           hf_ispid,
           hf_acchannel,
           req_accnum
      from (select r.hf_serialid,
                   f.hf_orderid,
                   f.channel_serialid,
                   f.send_serialid,
                   f.hf_status,
                   f.hf_ispid,
                   f.hf_acchannel,
                   f.req_accnum,
                   c.channel_pri
              from OD_CANCELORDERCHARGE r,
                   OD_FULLNOTE          f,
                   OD_ORDERINFO         o,
                   CH_CHANNELINFO       c
             where r.hf_serialid = f.hf_serialid
               and f.hf_orderid = o.req_orderid
               and f.hf_acchannel = c.channel_id
               and r.hf_cancelstatus = to_number(v_status)
               and r.req_trycount <= 8
               and r.req_sarttime <= sysdate - (1 / 48)
             order by r.req_sarttime asc, c.channel_pri desc
            --and f.hf_status = 0
            --and o.charge_status 0
            --and f.trycount <= 8
            )
     where rownum <= l_pageSize;
  --close v_resultCur; -- 不能关闭
END;
/

prompt
prompt Creating procedure SP_OD_SEARCHSTATUS_BEGIN
prompt ===========================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_SEARCHSTATUS_BEGIN(v_result          out varchar2, --结果码
                                                     v_processcount    out varchar2, --输出处理的订单数
                                                     v_hforderid       out varchar2, --订单号，以逗号隔开
                                                     v_hfserialid      out varchar2, --话费流水号，以逗号隔开
                                                     v_fullmoney       out varchar2, --充值金额，以逗号隔开
                                                     v_channelserialid out varchar2, --流水
                                                     v_sendserialid    out varchar2, --流水
                                                     v_ispid           out varchar2, --运营商ID
                                                     v_channelid       out varchar2, --渠道ID
                                                     v_accnum          out varchar2, --充值号码
                                                     v_provinceid      out varchar2, --
                                                     v_begintime       out varchar2, --
                                                     v_finishtime      out varchar2 --
                                                     ) --获取记录集合
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-获取需要查询充值状态的数据信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-08-02
  修改内容：
  功能描述：v1：查询出超时的，并且 时间不超过限制时间，没超过查询次数，
            v2:
            v3: 为了匹配lvs，修改查询sql ,正在查询1分钟之后无返回结果的
                遗留一个问题：就是如果第8次正在查询之后 对方没回应 那么状态就不会改变 ？
  
  ************************************************************/
  l_processcount    number;
  l_hforderid       varchar2(30);
  l_hfserialid      varchar2(30);
  l_ispid           number;
  l_accnum          varchar2(30);
  l_fullmoney       number(10, 2);
  l_channelid       number;
  l_channelserialid varchar2(40);
  l_sendserialid    varchar2(40);
  l_status          number; --话费充值记录状态
  l_trycount        number;
  l_provinceid      number;
  l_begintime       date;
  l_finishtime      date;

  l_qm_status   number; --查询状态
  l_hf_status   number; -- 是否通知了（修改）充值记录
  l_qm_trycount number; -- 查询次数
  l_qm_count    number; -- 是否存在记录
  l_qm_begintime date;-- 开始查询时间

  cursor cur_resultcur is
    select hf_serialid,
           hf_orderid,
           channel_serialid,
           send_serialid,
           hf_ispid,
           hf_acchannel,
           req_accnum,
           hf_cardprovinceid,
           begin_time,
           finish_time
      from (select f.hf_serialid,
                   f.hf_orderid,
                   f.channel_serialid,
                   f.send_serialid,
                   f.hf_ispid,
                   f.hf_acchannel,
                   f.req_accnum,
                   f.hf_cardprovinceid,
                   f.begin_time,
                   f.finish_time,
                   c.channel_pri
              from OD_FULLNOTE f, OD_ORDERINFO o, CH_CHANNELINFO c
             where f.hf_orderid = o.req_orderid
               and f.hf_acchannel = c.channel_id
               and f.hf_status = 4 --为 4超时
               and o.charge_status not in (0, 4, 5) --订单状态不能为最终状态的
               and f.trycount < 8 --查询限制 v_maxcnt 固定了
               and not exists
             (select 1
                      from OD_PHONECHARGEMONEY tp
                     where tp.hf_serialid = f.hf_serialid 
                       and (tp.hf_status = 0 or tp.qm_trycount >= 8 
                           or tp.qm_finishtime > (sysdate - 1/2880)
                           or (tp.qm_status = 2 and tp.qm_begintime > (sysdate - 1/1440)) 
                           ))
             order by c.channel_pri,
                      o.charge_linetime asc,
                      o.charge_finishtime asc)
     where rownum <= 20;

BEGIN

  v_result          := '1007';
  v_processcount    := '';
  v_hforderid       := '';
  v_hfserialid      := '';
  v_fullmoney       := '';
  v_channelserialid := '';
  v_sendserialid    := '';
  v_ispid           := '';
  v_channelid       := '';
  v_accnum          := '';
  v_provinceid      := '';
  v_begintime       := '';
  v_finishtime      := '';

  l_processcount := 0;

  OPEN cur_resultcur;
  LOOP
    <<loopstart>>
    FETCH cur_resultcur
      INTO l_hfserialid, l_hforderid, l_channelserialid, l_sendserialid, l_ispid, l_channelid, l_accnum, l_provinceid, l_begintime, l_finishtime;
    EXIT WHEN cur_resultcur%NOTFOUND;
  
    BEGIN
      --取一条订单
      select f.hf_status, f.trycount
        into l_status, l_trycount
        from OD_FULLNOTE f
       where f.hf_serialid = l_hfserialid
         FOR UPDATE NOWAIT;
    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        GOTO loopstart;
    END;
  
    --不是超时，大于等于8次就不再查询
    IF l_status <> 4 or l_trycount >= 8 THEN
      ROLLBACK;
      GOTO loopstart;
    END IF;
  
    -- 是否存在查询历史
    select count(1)
      into l_qm_count
      from OD_PHONECHARGEMONEY tp
     where tp.hf_serialid = l_hfserialid;
  
    if l_qm_count > 0 then
      select tp.qm_status, tp.hf_status, tp.qm_trycount,tp.qm_begintime 
        into l_qm_status, l_hf_status, l_qm_trycount,l_qm_begintime 
        from OD_PHONECHARGEMONEY tp
       where tp.hf_serialid = l_hfserialid;
      -- 是否已经通知（修改）话费记录，是否超过8次，是否正在查询,但是不是1分钟之内的
      if l_hf_status = 0 or l_qm_trycount >= 8 or (l_qm_status = 2 and l_qm_begintime > (sysdate - 1/1440) )  then
        ROLLBACK;
        GOTO loopstart;
      end if;
      --修改为正在查询，修改次数
      update OD_PHONECHARGEMONEY p
         set p.qm_begintime = sysdate,
             p.qm_status    = 2,
             p.qm_trycount  = p.qm_trycount + 1
       where p.hf_serialid = l_hfserialid;
    
    else
      --插入查询记录，状态为2
      insert into OD_PHONECHARGEMONEY
        (HF_SERIALID,
         HF_ACCPROVINCEID,
         HF_FINISHTIME,
         QM_BEGINTIME,
         QM_FINISHTIME,
         QM_STATUS,
         QM_TRYCOUNT,
         QM_PHONEMONEY,
         HF_ISPID,
         HF_STATUS)
      values
        (l_hfserialid,
         l_provinceid,
         l_finishtime,
         sysdate,
         null,
         2, --都是系统自动查询，故为2
         1, --查询次数为 1
         0,
         l_ispid,
         1);
    
    end if;
  
    --次数+1
    update OD_FULLNOTE f
       set f.trycount = f.trycount + 1
     where f.hf_serialid = l_hfserialid;
  
    --取到一条，计数器加1
    l_processcount    := l_processcount + 1;
    v_hforderid       := v_hforderid || l_hforderid || ',';
    v_hfserialid      := v_hfserialid || l_hfserialid || ',';
    v_fullmoney       := v_fullmoney || l_fullmoney || ',';
    v_channelserialid := v_channelserialid || l_channelserialid || ',';
    v_sendserialid    := v_sendserialid || l_sendserialid || ',';
    v_ispid           := v_ispid || l_ispid || ',';
    v_channelid       := v_channelid || l_channelid || ',';
    v_accnum          := v_accnum || l_accnum || ',';
    v_provinceid      := v_provinceid || l_provinceid || ',';
    v_begintime       := v_begintime ||
                         to_char(l_begintime, 'yyyymmddhh24miss') || ',';
    v_finishtime      := v_finishtime ||
                         to_char(l_finishtime, 'yyyymmddhh24miss') || ',';
  
    COMMIT;
  
    IF l_processcount >= 9 THEN
      --取够条数或取了10条退出
      EXIT;
    END IF;
  
  END LOOP;
  CLOSE cur_resultcur;

  --3、充值记录取数完成
  v_processcount := to_char(l_processcount);
  if l_processcount = 0 then
    v_result          := '1007';
    v_processcount    := '0';
    v_hforderid       := '-1';
    v_hfserialid      := '-1';
    v_fullmoney       := '-1';
    v_channelserialid := '-1';
    v_sendserialid    := '-1';
    v_ispid           := '-1';
    v_channelid       := '-1';
    v_accnum          := '-1';
    v_provinceid      := '-1';
    v_begintime       := '-1';
    v_finishtime      := '-1';
  
    return;
  
  end if;

  v_result := '0000';

EXCEPTION
  WHEN OTHERS THEN
    if l_processcount = 0 then
      v_result          := '1111';
      v_processcount    := '0';
      v_hforderid       := '-1';
      v_hfserialid      := '-1';
      v_fullmoney       := '-1';
      v_channelserialid := '-1';
      v_sendserialid    := '-1';
      v_ispid           := '-1';
      v_channelid       := '-1';
      v_accnum          := '-1';
      v_provinceid      := '-1';
      v_begintime       := '-1';
      v_finishtime      := '-1';
    
      sp_sys_dberrorinfo_create(3,
                                'SP_OD_SEARCHSTATUS_BEGIN',
                                sqlerrm,
                                l_hfserialid,
                                null,
                                null);
    else
      v_result       := '0';
      v_processcount := to_char(l_processcount);
      sp_sys_dberrorinfo_create(3,
                                'SP_OD_SEARCHSTATUS_BEGIN',
                                sqlerrm,
                                l_hfserialid,
                                null,
                                null);
    end if;
  
    COMMIT;
  
END;
/

prompt
prompt Creating procedure SP_OD_SENDORDERINFO
prompt ======================================
prompt
create or replace procedure hf.SP_OD_SENDORDERINFO(v_hforderid    in VARCHAR2, -- 话费平台订单号
                                                v_online_id    in NUMBER, -- 货架ID
                                                v_mobilenum    in VARCHAR2, -- 手机号码
                                                v_chargeamount in NUMBER, -- 充值金额
                                                v_paymount     in NUMBER, -- 订单金额
                                                v_orderid      in VARCHAR2, -- 订单号
                                                v_ordersource  in VARCHAR2, -- 订单来源
                                                v_agentid      in VARCHAR2, -- 代理商ID(用户ID)
                                                v_ordertime    in VARCHAR2, -- 销售平台订单时间
                                                v_mark         in VARCHAR2, -- 保留字段
                                                v_result       out VARCHAR2 -- 结果代码
                                                )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 话费充值预下单转变为已完成支付等待处理
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：weisd
  添加日期：2011-6-23
  添加内容：预下单(SP_OD_PREPAREORDERINFO)成功后，
            前台确认完毕后再次请求发货充值，即修改为已完成支付，等待处理
            08.14 修改返回码

  ******************************************************************************/

 AS

  l_hforderid_count    NUMBER(10); --是否存在该话费订单
  l_now_hforder_status NUMBER(2); -- 当前订单的状态

begin
  v_result := '1111';

  -- 查询发送过来的订单号是否已存在，是否与信息匹配
  select count(1)
    into l_hforderid_count
    from od_orderinfo t
   where t.req_orderid = v_hforderid
     and t.product_onlineid = v_online_id
     and t.req_accnum = v_mobilenum
     and t.req_money = v_chargeamount
     and t.req_saleserialid = v_orderid
     and t.req_ordersource = v_ordersource
     and t.req_userid = v_agentid
     and t.req_paymoney = v_paymount;
  -- v_ordertime    in VARCHAR2, -- 销售平台订单时间  暂时不匹配
  if l_hforderid_count = 1 then

    BEGIN
      --取当前订单
      select t.charge_status
        into l_now_hforder_status
        from od_orderinfo t
       where t.req_orderid = v_hforderid
         and t.req_saleserialid = v_orderid
         FOR UPDATE;
    EXCEPTION
      WHEN OTHERS THEN
        v_result := '0014'; --获取要发货的订单准备修改失败（可能正在被使用）
        ROLLBACK;
    END;

    if l_now_hforder_status = 1 then
      update od_orderinfo t
         set t.charge_status = 2
       where t.req_orderid = v_hforderid
         and t.req_saleserialid = v_orderid;
         
      v_result := '0000';

    else

      v_result := '0015'; --当前订单状态不是预下单，无法进行发货请求

    end if;

  else
    v_result := '0013'; --由预下单进行发送请求时候查询订单失败(根据参数匹配查询订单数不为1)
  end if;
  --  更新收单表中的订单号
  update od_recvwebbill t
     set t.cnf_errorcode = v_result
   where t.hf_orderid = v_hforderid
     and t.req_orderid = v_orderid;

  commit;

EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    ROLLBACK;
    update od_recvwebbill t
       set t.cnf_errorcode = v_result
     where t.hf_orderid = v_hforderid
       and t.req_orderid = v_orderid;
    commit;

end;
/

prompt
prompt Creating procedure SP_OD_SET_CHANNELSTATUS
prompt ==========================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_SET_CHANNELSTATUS(v_resultno    in VARCHAR2, --resutlno 9988
                                                    v_channelkey in VARCHAR2, --需要修改的渠道id集合
                                                    v_opttype    in number, --修改状态
                                                    v_opttime    in VARCHAR2, --发送过来的时间
                                                    v_optname    in VARCHAR2, --操作人id，name
                                                    v_errorcode  in VARCHAR2, --错误码
                                                    v_errormsg   in VARCHAR2, --错误信息
                                                    v_result     out VARCHAR2) --返回结果代码

 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-修改渠道开关状态
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-22
  修改内容：
  功能描述：根据消息(网关，以及应用发送回来的消息)修改渠道开关状态
            v_errormsg 应尽可能少
            v_channelkey ,key,key,key,key,key,集合体
  ************************************************************/

  l_channelid VARCHAR2(40); -- 渠道id
  l_count     NUMBER(10); -- 条数
  l_status    NUMBER(2); -- 旧状态
  l_optuser   VARCHAR2(40);
  l_warninfo  VARCHAR2(200); -- 报警信息

  CURSOR channelid_split is
    select column_value
      from table(cast(myconvert(v_channelkey, ',') as t_vc))
     where column_value is not null;

BEGIN
  v_result := '1111';

  if v_opttype is null or v_optname is null then
    l_warninfo := '修改渠道状态' || l_channelid || '无效,参数不匹配:' || l_status || ',' ||
                  v_resultno || ',' || v_opttype || v_opttime || ',' ||
                  v_optname || ',' || v_errorcode || ',' || v_errormsg;
  
    sp_sys_dberrorinfo_create(1, '渠道', '', l_warninfo, '', '');
    commit;
    v_result := '1111';
    return;
  end if;

  if v_resultno = '9988' then
    --渠道发送过啦关闭的
    l_optuser := 'system';
  else
    l_optuser := v_optname;
  end if;

  OPEN channelid_split;

  l_channelid := '';

  LOOP
    FETCH channelid_split
      INTO l_channelid;
    EXIT WHEN channelid_split%NOTFOUND;
  
    select count(1)
      into l_count
      from CH_CHANNELINFO c
     where c.channel_id = l_channelid;
  
    if l_count > 0 then
      select c.channel_status
        into l_status
        from CH_CHANNELINFO c
       where c.channel_id = l_channelid;

      if v_opttype = l_status then
        null;
      else
        update CH_CHANNELINFO c
           set c.channel_status = v_opttype
         where c.channel_id = l_channelid;
      
/*        insert into CH_CONFIG_QUARTZ
          (CHANNEL_ID,
           UPDATE_TIME,
           OPT_USERID,
           OPT_TYPE,
           OPT_LOG,
           ON_TIME,
           AUTO_NO)
        values
          (l_channelid,
           sysdate,
           l_optuser,
           v_opttype,
           v_resultno||','||v_opttime || ',' || v_errorcode || ',' || v_errormsg, --不能超过长度
           null,
           0);*/
           
      end if;
    
    end if;
  
  END LOOP;
  CLOSE channelid_split;
  --循环全部成功
  commit;
  v_result := '0000';

EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure SP_OD_UPDATEORDERNOTICESTATUS
prompt ================================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_UPDATEORDERNOTICESTATUS(v_hforderid       in VARCHAR2, --要获取的状态
                                                          v_reqsaleserialid in VARCHAR2, --前台支付订单号
                                                          v_ecnoticeflag    in VARCHAR2, --要修改的状态
                                                          v_needagain       out VARCHAR2, --是否需要再通知，针对通知失败
                                                          v_result          out VARCHAR2) -- 结果代码
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-修改订单的通知状态为如：正在通知,或其通知成功、通知失败 根据v_ecnoticeflag 判断
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-5-23
  修改内容：
  功能描述：修改订单的通知状态为如：正在通知,或其通知成功、通知失败 对于通知失败的，返回是否还需要再通知
                                    v_result 为修改状态是否修改成功
                                    v_needagain  先判断v_result 为 00000 再根据  22222 为需要再通知
            v2 weisd 由于文档要求 过5分钟重发，当前返回会立即重发，故在SP_OD_NEEDNOTICEORDER_BEGIN 中包括
            v3 weisd 8.02 不支持在这里修改为正在通知了
  ************************************************************/
  l_count       NUMBER(10); -- 条数
  l_noticestatus  NUMBER(4);--一个temp状态
BEGIN

  --v_status  暂时无用
  v_result    := '11111';
  v_needagain := '00000';

  select count(1)
    into l_count
    from od_orderinfo t
   where t.req_orderid = v_hforderid
     and t.req_saleserialid = v_reqsaleserialid;

  if (l_count <> 1) or (v_ecnoticeflag is null) then

    v_result := '11111';

  else
        --后台通知标识：0已通知，1未通知，2正在通知,3通知失败
        if v_ecnoticeflag = 0 then

            l_noticestatus := 0;
            update od_orderinfo t
               set t.ec_notifyflag  = l_noticestatus,
                   t.ec_notifytime  = sysdate
             where t.req_orderid = v_hforderid
               and t.req_saleserialid = v_reqsaleserialid;

            v_result    := '00000';
            v_needagain := '00000';

        elsif v_ecnoticeflag = 1 then

            v_result    := '00000';
            v_needagain := '00000';

        elsif v_ecnoticeflag = 2 then 
            -- 不支持在这里修改为正在通知了
            v_result    := '11111';
            v_needagain := '00000';

        elsif v_ecnoticeflag = 3 then 
               l_noticestatus := 3;
               v_needagain := '00000';
               update od_orderinfo t
                 set t.ec_notifyflag  = l_noticestatus,
                     t.ec_notifytime  = sysdate
               where t.req_orderid = v_hforderid
                 and t.req_saleserialid = v_reqsaleserialid;

               v_result := '00000';

        else
          v_result := '11111';
        end if;

    COMMIT;
  end if;

EXCEPTION
  WHEN OTHERS THEN
    v_result := '11111';
    ROLLBACK;

END;
/

prompt
prompt Creating procedure SP_OD_UPDATEORDERSTATUS
prompt ==========================================
prompt
create or replace procedure hf.SP_OD_UPDATEORDERSTATUS(v_hfserialid     in VARCHAR2, -- 话费流水号
                                                    v_orderid        in varchar2, --订单号
                                                    v_fullnot_status in number, -- 修改后的话费充值记录状态
                                                    v_order_status   in number, --修改后的订单状态
                                                    v_result         out VARCHAR2 -- 结果代码
                                                    )

  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 修改话费充值记录表和订单表中记录的状态
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  添加日期：2011-5-5
  添加内容：如果v_hfserialid<>null,订单号为v_hfserialid对应的订单号
  ******************************************************************************/

 AS
  l_orderid varchar2(30);
begin
  v_result  := '1111';
  l_orderid := v_orderid;
  if v_hfserialid is not null then
    select nvl(t.hf_orderid, '')
      into l_orderid
      from od_fullnote t
     where t.hf_serialid = v_hfserialid;
  end if;
  if v_hfserialid is not null and v_fullnot_status is not null then
    update od_fullnote f
       set f.hf_status = v_fullnot_status
     where f.hf_serialid = v_hfserialid;
  end if;
  if l_orderid is not null and v_order_status is not null then
    update od_orderinfo o
       set o.charge_status = v_order_status
     where o.req_orderid = l_orderid;
  end if;
  commit;
  v_result := '0000';

EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    ROLLBACK;
end;
/

prompt
prompt Creating procedure SP_OD_UPDATE_SEARCHSTATUS
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_UPDATE_SEARCHSTATUS(v_hfserialid   in VARCHAR2, --话费流水号
                                                      v_status       in NUMBER, --要修改的状态OD_PHONEC
                                                      v_chargestatus in VARCHAR2, --充值结果状态
                                                      v_dealtime     in VARCHAR2, --充值处理时间
                                                      v_dealamount   in NUMBER, --实际充值金额
                                                      v_errorcode    in VARCHAR2, --错误码
                                                      v_restatus     out VARCHAR2, --当前状态OD_PHONEC
                                                      v_isupdate     out VARCHAR2, --是否更新了话费状态
                                                      v_result       out VARCHAR2) -- 结果代码
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-修改 OD_PHONECHARGEMONEY 中查询结果状态
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-3
  修改内容：
  功能描述：负责处理查询超时的充值记录的结果状态修改，暂不提供OD_PHONE中1、4状态修改
            对需要查询的充值记录
            对返回结果：充值成功、充值失败
            v2:weisd 补充查询结果为：渠道正在处理
                     补充部分成功处理
            v3:weisd 去除把状态修改为正在查询，只在查询出来的时候修改,
                     并且距离限制时间有30秒查询等地
  ************************************************************/
  l_count           NUMBER(10); -- 条数
  l_status          NUMBER(2);
  l_isupdate        number(1); -- p中是否已经更新了话费表
  l_temp_isupdate   number(1); -- temp
  l_search_count    NUMBER(10); -- 查询次数
  l_hf_status       number(1); --原充值记录状态
  l_dealtime        varchar2(30); --处理时间
  l_dealamount      NUMBER(10, 2); --临时处理金额
  l_result          varchar2(10); --调用存储过程临时结果
  l_channelserialid VARCHAR2(40); -- 渠道流水号
  l_sendserialid    VARCHAR2(40); -- 发送给对方流水号
  l_warninfo        VARCHAR2(200); -- 报警信息
  l_fullmoney       NUMBER(10, 2); --充值记录的充值金额
  l_hf_orderid      VARCHAR2(30); -- 话费订单号
  l_time            NUMBER(10); -- 是否可以继续冲值的时间差,以秒计算

BEGIN

  v_result        := '1111';
  l_result        := '2222';
  l_temp_isupdate := 1; --默认值
  if v_dealtime is null then

    l_dealtime := to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS');

  else
    l_dealtime := v_dealtime;

  end if;
  if v_dealamount is null or v_dealamount = 0 then

    l_dealamount := null;

  else
    l_dealamount := v_dealamount;

  end if;

  if v_status in (1,2,4) then
    --暂时不提供功能
    v_restatus := v_status;
    v_result   := '1111';
    return;
  end if;

  select count(1)
    into l_count
    from OD_PHONECHARGEMONEY p
   where p.hf_serialid = v_hfserialid;

  if l_count = 0 then
     -- 没有记录无法进行下一步操作
      l_warninfo := '查询充值记录' || v_hfserialid || '有结果返回处理,但是无法从OD_PHONECHARGEMONEY获取到' ||
                    v_hfserialid ||'查询记录';
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
      --不是超时的,且已经更改过订单状态的 返回
      v_restatus := v_status;
      v_result   := '1111';
    return;
  end if;

  BEGIN
    --查询操作
    SELECT p.qm_status, p.hf_status, p.qm_trycount
      INTO l_status, l_isupdate, l_search_count
      FROM OD_PHONECHARGEMONEY p
     WHERE p.hf_serialid = v_hfserialid
       FOR UPDATE;
  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
  END;

  if v_status = 3 then
    -- 查询出订单相关信息
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           decode((sysdate - d.charge_linetime) * 24 * 24 * 60,
                  null,
                  0,
                  (sysdate - d.charge_linetime) * 24 * 24 * 60),
           t.trycount
      into l_hf_orderid, l_time, l_search_count
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    if l_search_count >= 8 or (l_time + 30) > 0 then
      --如果已经到达8次了 或者已经 + 30秒 >= 要求完成时间
      -- 更新话费充值记录表,状态为9:结果未知
      update od_fullnote t
         set t.hf_status     = 9,
             t.chfinish_time = to_date(l_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
             t.finish_time   = sysdate,
             t.hf_errorcode  = v_errorcode
       where t.hf_serialid = v_hfserialid;
      -- 更新话费订单表,状态为8:结果未知,需要人工干预
      update od_orderinfo t
         set t.charge_status     = 8,
             t.charge_finishtime = sysdate,
             t.req_errorcode     = v_errorcode
       where t.req_orderid = l_hf_orderid;

       l_temp_isupdate := 0;

    end if;
        --查询失败,修改查询状态，
    update OD_PHONECHARGEMONEY p
       set p.qm_finishtime = sysdate, p.qm_status = v_status, p.hf_status = l_temp_isupdate
     where p.hf_serialid = v_hfserialid;

    v_restatus := v_status;
    v_isupdate := l_temp_isupdate;
    v_result   := '0000';
    commit;
    return;
  end if;

  if v_status = 0 then
    -- 查询成功 查询结果处理，查询次数，查询状态 查询结果，修改订单状态及到账金额
    select f.hf_status, f.channel_serialid, f.send_serialid, f.full_money
      into l_hf_status, l_channelserialid, l_sendserialid, l_fullmoney
      from OD_FULLNOTE f
     where f.hf_serialid = v_hfserialid;

    if l_hf_status = 4 and l_isupdate = 1 then

      --根据查询结果更新相应状态 更新订单状态，如果充值状态已经是 最终状态了
      if v_chargestatus = '0' then
        --充值成功

        SP_OD_RECHARGESUCCESS(v_hfserialid,
                              l_channelserialid,
                              l_sendserialid,
                              l_dealtime,
                              l_dealamount,
                              l_result); --调用成功处理

      elsif v_chargestatus = '3' then
        --充值失败

        SP_OD_RECHARGEFAILURE(v_hfserialid,
                              l_channelserialid,
                              l_sendserialid,
                              l_dealtime,
                              v_errorcode,
                              l_result);

      elsif v_chargestatus = '6' then
            -- 部分成功
          SP_OD_RECHARGEPARTSUCC(v_hfserialid,
                                l_channelserialid,
                                l_sendserialid,
                                l_dealamount,
                                l_dealtime,
                                v_errorcode,
                                l_result);

      elsif v_chargestatus = '2' then
            -- 渠道正在处理 不做处理，只修改查询状态
         null;

      end if;

      if l_result = '0000' then

        l_temp_isupdate := 0; --设置为已通知
        v_restatus      := v_chargestatus;
        v_isupdate      := '0';
        v_result        := '0000';

      elsif l_result = '1111' then
        l_warninfo := '查询充值记录' || v_hfserialid || '成功,修改充值结果处理最终状态' ||
                      v_chargestatus || '失败' || l_result;
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
        v_restatus := v_chargestatus;
        v_isupdate := l_isupdate;
        v_result   := '3333'; --查询到充值记录最终状态，但是修改数据库订单之类失败

      else
        l_warninfo := '查询充值记录' || v_hfserialid ||
                      '成功,但是充值状态不是最终结果,不对充值记录做修改：' || v_chargestatus ||
                      ',只修改查询次数';
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
        v_restatus := v_chargestatus;
        v_isupdate := l_isupdate;
        v_result   := '0000'; --查询到充值记录最终状态，但是修改数据库订单之类失败

      end if;

    else
      l_warninfo := '查询充值记录' || v_hfserialid || '成功,但是记录状态不为超时,而是' ||
                    l_hf_status || ',l_isupdate:' || l_isupdate;
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
      --不是超时的,且已经更改过订单状态的 返回
      v_restatus := v_status;
      v_isupdate := l_isupdate;
      v_result   := '0000';
    end if;

    update OD_PHONECHARGEMONEY p
       set p.qm_finishtime = sysdate,
           p.qm_phonemoney = l_fullmoney -
                             decode(l_dealamount,
                                    null,
                                    l_fullmoney,
                                    l_dealamount),
           p.qm_status     = v_status,
           p.hf_status     = l_temp_isupdate
     where p.hf_serialid = v_hfserialid;

    commit;
  end if;

EXCEPTION
  WHEN OTHERS THEN
    v_restatus := '';
    v_isupdate := '';
    v_result   := '1111';
    ROLLBACK;
    --v_status||v_chargestatus||v_dealtime||v_dealamount||v_errorcode||l_hf_status||l_result
    l_warninfo := '查询充值记录' || v_hfserialid || '异常,' || v_status || ',' ||
                  v_chargestatus || ',' || v_dealtime || ',' ||
                  v_dealamount || ',' || v_errorcode || ',' || l_hf_status || ',' ||
                  l_result;
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    commit;
END;
/

prompt
prompt Creating procedure WEISD_TEST
prompt =============================
prompt
CREATE OR REPLACE PROCEDURE HF.WEISD_TEST(                v_Result       OUT VARCHAR2 --结果码
                                                       )
/************************************************************
  产品名称：话费充值系统
  产品版本：V1.0
  模块名称：数据库脚本-充值触发,配置渠道，生成话费流水号
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-05-05
  修改内容：
  ************************************************************/
 AS




BEGIN

insert into WEISD_TEST2 (HF_SERIALID) values ('2222222');

v_Result := '1111';

commit;

EXCEPTION
  WHEN OTHERS THEN


    COMMIT;
END;
/

prompt
prompt Creating procedure WEISD_TEST_SP1
prompt =================================
prompt
CREATE OR REPLACE PROCEDURE HF.WEISD_TEST_SP1(re out varchar2)
/************************************************************
  产品名称：话费充值系统
  产品版本：V1.0
  模块名称：数据库脚本-充值触发,配置渠道，生成话费流水号
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-05-05
  修改内容：
  ************************************************************/
 AS

result2 varchar2(50);
l_status number;

BEGIN

/*insert into WEISD_TEST2 (HF_SERIALID) values ('111111');

WEISD_TEST_SP2(result2);

dbms_output.put_line(result2);

insert into WEISD_TEST2 (HF_SERIALID) values ('3333333');*/

select t.qm_status into l_status

from weisd_test2 t 
where t.hf_serialid = '3333333'

for update;

dbms_output.put_line(l_status);

update weisd_test2 t  set t.qm_status = 3;

dbms_output.put_line(l_status);

commit;
--rollback;
re :='0000';
EXCEPTION
  WHEN OTHERS THEN
re := 'error';

    rollback;
END;
/

prompt
prompt Creating procedure WEISD_TEST_SP2
prompt =================================
prompt
CREATE OR REPLACE PROCEDURE HF.WEISD_TEST_SP2(                v_Result       OUT VARCHAR2 --结果码
                                                       )
/************************************************************
  产品名称：话费充值系统
  产品版本：V1.0
  模块名称：数据库脚本-充值触发,配置渠道，生成话费流水号
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：wjjava
  修改日期：2011-05-05
  修改内容：
  ************************************************************/
 AS

l_status number;


BEGIN
/*
insert into WEISD_TEST2 (HF_SERIALID) values ('2222222');

v_Result := '1111';

--commit;
rollback;*/

select t.qm_status into l_status

from weisd_test2 t 
where t.hf_serialid = '3333333'

for update;


dbms_output.put_line(l_status);

commit;

v_Result := '0000';

EXCEPTION
  WHEN OTHERS THEN


    rollback;
END;
/


spool off
