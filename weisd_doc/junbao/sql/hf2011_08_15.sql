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
  * ���ڼ���ֵ����Ƿ�����ܽ��ƥ��
  *   PRODUCT_CONTENT    NUMBER(10,2), ����
  *   PRODUCT_MINMONEY   NUMBER(10,2), ��С���
  *   PRODUCT_MAXMONEY   NUMBER(10,2), �����
  *   CHARGE_MONEY       NUMBER(10,2), ��ֵ���
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
      --������
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
  HF_DBWARNING(1,'cov_poinfo_order',SQLCODE||'��'||SQLERRM,'','','');
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ���ȡ����֧�ֵ���ֵ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-5-10
  �޸����ݣ�
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ���ȡ����֧�ֵ�ҵ������
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-5-10
  �޸����ݣ�
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
                                        '�ֻ�' || ',');
      --ҵ�����ͣ�0�ֻ���1�̻���2С��ͨ��3���
    ELSIF l_temp_business = 1 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        '�̻�' || ',');
    ELSIF l_temp_business = 2 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        'С��ͨ' || ',');
    ELSIF l_temp_business = 3 THEN
      l_support_business_name := CONCAT(l_support_business_name,
                                        '���' || ',');
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ���ȡ����֧�ֵ���������ID��������ͨ�͹رյģ���Ϣ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-5-10
  �޸����ݣ���������ID,��","����
  ************************************************************/
  l_channelIds VARCHAR2(100);
BEGIN
  l_channelIds := '';
  FOR channel IN (SELECT cc.channel_id, cc.channel_name
                    FROM ch_channelinfo cc
                   WHERE channel_needcard = 0 --����������
                     AND channel_isp = v_ispId --֧����Ӫ��
                     AND (channel_provinceid = v_provinceId or
                         channel_provinceid = '-1') --֧��ʡ��
                     AND (channel_citycode = '-1' OR
                         channel_citycode LIKE '%' || v_cityCode || '%') --֧�ֵ���
                        /* AND channel_status = 0 */ --��������
                     AND (cc.channel_limit_money = -1 or
                         cc.channel_money <= cc.channel_limit_money) --������û�г���
                     AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
                         (SELECT DISTINCT channel_id
                            FROM ch_notmatchchannel nm
                           WHERE order_source = v_osId
                             AND (user_id = '-1' OR user_id = v_userId))
                     AND ( --֧����ֵ
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ���ȡ����֧�ֵĹرյ�����ID��Ϣ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-5-10
  �޸����ݣ���������ID,��","����
  ************************************************************/
  l_channelIds VARCHAR2(100);
BEGIN
  l_channelIds := '';
  FOR channel IN (SELECT cc.channel_id, cc.channel_name
                    FROM ch_channelinfo cc
                   WHERE channel_needcard = 0 --����������
                     AND channel_isp = v_ispId --֧����Ӫ��
                     AND (channel_provinceid = v_provinceId or
                         channel_provinceid = '-1') --֧��ʡ��
                     AND (channel_citycode = '-1' OR
                         channel_citycode LIKE '%' || v_cityCode || '%') --֧�ֵ���
                         AND channel_status > 0  --��������
                     AND (cc.channel_limit_money = -1 or
                         cc.channel_money <= cc.channel_limit_money) --������û�г���
                     AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
                         (SELECT DISTINCT channel_id
                            FROM ch_notmatchchannel nm
                           WHERE order_source = v_osId
                             AND (user_id = '-1' OR user_id = v_userId))
                     AND ( --֧����ֵ
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ���ȡ����֧�ֵĿ�ͨ������ID��Ϣ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-5-10
  �޸����ݣ���������ID,��","����
  ************************************************************/
  l_channelIds VARCHAR2(100);
BEGIN
  l_channelIds := '';
  FOR channel IN (SELECT cc.channel_id, cc.channel_name
                    FROM ch_channelinfo cc
                   WHERE channel_needcard = 0 --����������
                     AND channel_isp = v_ispId --֧����Ӫ��
                     AND (channel_provinceid = v_provinceId or
                         channel_provinceid = '-1') --֧��ʡ��
                     AND (channel_citycode = '-1' OR
                         channel_citycode LIKE '%' || v_cityCode || '%') --֧�ֵ���
                         AND channel_status = 0  --��������
                     AND (cc.channel_limit_money = -1 or
                         cc.channel_money <= cc.channel_limit_money) --������û�г���
                     AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
                         (SELECT DISTINCT channel_id
                            FROM ch_notmatchchannel nm
                           WHERE order_source = v_osId
                             AND (user_id = '-1' OR user_id = v_userId))
                     AND ( --֧����ֵ
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
      decode(ABS(trunc(time)),0,'',ABS(trunc(time))||'��')||
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
        -- ���ָ������֮���ǲ��Ǵ�����ȵ�ֵ
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
 * �ָ��ַ����Զ��庯������ʽ
 * s_str:Ҫ���ָ���ַ���
 * split_str:�ָ���
 * ʹ�÷�����
 *       (1).create or replace type t_vc is table of varchar2(100);  --Ҫ����ռ�ڴ棬Ҳ���������
 *       (2).select column_value from table(cast(myconvert(s_str, split_str) as t_vc))  order by 1 nulls first;
 * �磺select column_value from table(cast(myconvert('a+b+b+d+e+f', '+') as t_vc))  order by 1 nulls first;
 * �õ����ǣ�a b b d e f
 */
/

prompt
prompt Creating procedure PO_PRODUCTINFO_EDIT
prompt ======================================
prompt
CREATE OR REPLACE PROCEDURE HF.PO_PRODUCTINFO_EDIT (
                                                    -- v_productname varchar2,--��Ʒ����
                                                    v_ispid       NUMBER, --��Ӫ�̱�ʶ 0���й���ͨ1���й��ƶ�
                                                    v_delaytime   NUMBER, --����ʱ�䣬��λΪ����
                                                    v_provinceids VARCHAR2, --��Ʒʡ��ID
                                                    v_cardmoneys  VARCHAR2, --��ѡ��ֵ���ԡ���������
                                                    v_citycodes   VARCHAR2, --��������
                                                    v_Type        NUMBER, --�������� 0����� 1���޸� 2:ɾ��
                                                    v_productid   NUMBER, --��ƷID
                                                    v_productType NUMBER, --��Ʒ����
                                                    v_Result      OUT VARCHAR2,
                                                    v_success     OUT VARCHAR2) AS
  /************************************************************
  ��Ʒ���ƣ��»��ѳ�ֵϵͳ
  ģ�����ƣ����ݿ�ű�-������Ʒ��Ϣ�����\�޸�\ɾ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  ������Ա��wangjiang
  �������ڣ�2008-11-28
  ����������������Ʒ��Ϣ�����\�޸�\ɾ��,���ʱ��
            ʡ�ݺ͵��ж��ǵ�ѡ��
            ��ѭ����ӳ�ʡID��ͬ�������Զ���ͬ����Ʒ��¼
  ************************************************************/
  l_provinceid     VARCHAR2(10);
  l_curr_cardmoney VARCHAR2(10); --ѭ��ʱ�õ�����ֵ

  l_product_name_temp  VARCHAR2(100); --��Ʒ����
  l_product_name       VARCHAR2(200); --��Ʒ����
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
  CURSOR CUR_cardAmount IS --��ֵ�б�
    SELECT COLUMN_VALUE
      FROM TABLE(CAST(Myconvert(v_cardmoneys, ',') AS t_vc))
     WHERE COLUMN_VALUE IS NOT NULL;
  CURSOR CUR_cityCode(CITY VARCHAR2) IS --�����б�
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
                '�ֻ�',
                1,
                '�̻�',
                2,
                'С��ͨ',
                '���')
    INTO l_productType
    FROM dual;

  IF v_Type = 0 THEN
    --���
    SELECT DECODE(v_ispid, 0, '��ͨ', 1, '�ƶ�', 2, '����', v_ispid)
      INTO l_temp_name1
      FROM dual;
    SELECT DECODE(v_delaytime,
                  5,
                  '5����ʵʱ��ֵ',
                  1440,
                  '24Сʱ��ֵ',
                  2880,
                  '48Сʱ��ֵ',
                  120,
                  '2Сʱ����',
                  999,
                  '����9�㵽��',
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
                                l_curr_cardmoney || 'Ԫ' || l_delaytime;
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
              --��dealer�û���������Ʒ��Ϣ
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
           DECODE(v_ispid, 1, '�ƶ�', 0, '��ͨ', 2, '����', v_ispid) ||
           l_productType ||
           DECODE(SIGN(v_delaytime - 60),
                  -1,
                  v_delaytime || '����',
                  0,
                  1 || 'Сʱ',
                  (v_delaytime / 60) || 'Сʱ') ||
           DECODE(TO_NUMBER(v_cardmoneys),
                  0,
                  '������',
                  TO_NUMBER(v_cardmoneys) || 'Ԫ') || '��ֵ'
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
CREATE OR REPLACE PROCEDURE HF.PO_PRODUCTONLINE_AUDITING(v_opttype           in VARCHAR2, --��������
                                                      v_auditing_status   in number, --���״̬
                                                      v_auditing_username in VARCHAR2, --�����
                                                      v_manids            in VARCHAR2, --���id����
                                                      v_applytype         in varchar2, --��������
                                                      v_result            OUT VARCHAR2) AS
  /**************************************************************************************
  ��Ʒ���ƣ�֧������+����ϵͳ3.0
  ģ�����ƣ����ݿ�ű�-���ֻ���ֵ-->������Ϣ����
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  ������Ա��weisd
  �������ڣ�2011-07-12
  ����������
             ������˲���
             v_opttype��1 �޸���Ϣ����
             Ӧ��ȷ��v_applytype ��ͬһ����ÿ���Σ���ͬ���������Ͷ�Ӧ��ͬ�Ĳ���ʵ��
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

  CURSOR cur_details(manId number) IS --������ϸ�α�
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
            -- �޸�main�����״̬
            update PO_AUDITING_MAIN t
               set t.AUDITING_STATUS   = v_auditing_status,
                   t.AUDITING_USERNAME = v_auditing_username,
                   t.AUDITING_TIME     = sysdate
             where t.man_id = l_manId
               and t.apply_type = v_applytype;
          
            if v_auditing_status = 1 then
              --����ϸ�α�
              OPEN cur_details(l_manId);
              LOOP
                FETCH cur_details
                  INTO l_user_id, l_product_schanneltype, l_product_splitflag, l_product_waittime, l_product_delaytime, l_product_status, l_product_discount, l_product_price, l_online_id, l_product_id, l_order_source;
                EXIT WHEN cur_details%NOTFOUND;
                --���»��ܱ�����
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
           v_id varchar2,   --id ��ţ���','�ָ�
           v_reqOrderSource number,--������Դ
           v_reqUserid varchar2,   --������
           v_productIsptype number,--��Ӫ��
           v_productProvinceid number,
           v_productMoney varchar2, --��ֵ
           v_productDelaytime number,
           v_productRate number,
           v_adjustType number,
           v_begindate varchar2,
           v_enddate varchar2,
           v_type number, --�������� 0����� 1���޸� 2:ɾ��
           v_Result out varchar2
          ) as
/*******************************************************
  ��Ʒ���ƣ���ֵϵͳ
  ģ�����ƣ����ݿ�ű�-�Զ������������õ����\�޸�\ɾ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE10g
  ������Ա��zhouwx
  �������ڣ�2011-07-1
  ����������������Ʒ��Ϣ�����\�޸�\ɾ��,���ʱ
            ��ֵ����Ϊ'50,100' ��
            �����̿���Ϊ'001,002'��
            ������Ϊ��ѡ

*******************************************************/
  l_curr_cardmoney varchar2(10); --ѭ��ʱ�õ�����ֵ
  l_curr_userid varchar2(30);
  l_is_cardamountValid BOOLEAN;
  l_id number;
  l_id_count number;
  CURSOR CUR_cardAmount IS --��ֵ�б�
     SELECT COLUMN_VALUE
     FROM TABLE(CAST(Myconvert(v_productMoney, ',') AS t_vc))
     WHERE COLUMN_VALUE IS NOT NULL;
   CURSOR cur_userid is --�������б�
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
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_CITY" (v_ispId            VARCHAR2, --��Ӫ��
                                                    v_orderSource      VARCHAR2, --������Դ
                                                    v_provinceId       VARCHAR2, --ʡ��
                                                    v_codeCitys        VARCHAR2, --���� ���ԣ��Ÿ�����
                                                    v_userId           VARCHAR2, --�û�Id
                                                    v_cardMoney        VARCHAR2, --¼����ֵ
                                                    v_schannelType     VARCHAR2, --���ȷ�ʽ
                                                    v_ratio            VARCHAR2, --�ۿ���
                                                    v_splitFlag        VARCHAR2, --�Ƿ�ִ�
                                                    v_waitTime         VARCHAR2, --��ʼ�ȴ�ʱ��
                                                    v_productDelaytime VARCHAR2, --��Ʒ����ʱ������Ʒ���еģ�
                                                    v_onlineDelaytime  VARCHAR2, --������ʱ��
                                                    v_status           VARCHAR2, --��Ʒ״̬
                                                    v_productType      VARCHAR2, --��Ʒҵ������
                                                    v_orderIndex       VARCHAR2, --���ȼ�
                                                    v_oper             VARCHAR2, --������
                                                    v_result           OUT VARCHAR2, --ִ�н��
                                                    v_insertNum        OUT VARCHAR2 --��ӻ��ܵ�����
                                                    ) AS
  /************************************************************
  ��Ʒ���ƣ��»��ѳ�ֵϵͳ2.0
  ģ�����ƣ����ݿ�ű�-��Ʒ������Ϣ�����\�޸�\ɾ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  ������Ա��wangjiang
  �������ڣ�2008-11-26
  ������������Ʒ������Ϣ�����_��������ѭ��
            v2 wiesd 2011-08-08 l_curr_codecity ���rownum = 1
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

  CURSOR c_products(p_ispid NUMBER, p_provinceid NUMBER, p_cardmoney NUMBER, p_delaytime NUMBER, p_codecity VARCHAR) IS --������Ʒ�α�
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
      --�˴�ȱ���жϵ�ǰ�����Ƿ����ڵ�ǰʡ�Ĵ���
      --��������ڵ�ǰʡ��Ӧ����ѭ��
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

         -- ע��by 
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
            --���������ͬ�ļ�¼�������

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

        END LOOP; --����ѭ���α�
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
  --DBMS_OUTPUT.PUT_LINE(SQLCODE||'��'||SQLERRM);
   v_result := '11111';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure PO_PRODUCTONLINE_CARDMONEY
prompt =============================================
prompt
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_CARDMONEY" (v_ispId            VARCHAR2, --��Ӫ��
                                                         v_ordersource      VARCHAR2, --������Դ
                                                         v_provinceId       VARCHAR2, --ʡ��
                                                         v_codeCitys        VARCHAR2, --���� ���ԣ��Ÿ�����
                                                         v_userId           VARCHAR2, --�û�Id
                                                         v_cardMoneys       VARCHAR2, --¼��Ķ����ֵ���ԣ��Ÿ�����
                                                         v_schannelType     VARCHAR2, --���ȷ�ʽ
                                                         v_ratio            VARCHAR2, --�ۿ���
                                                         v_splitFlag        VARCHAR2, --�Ƿ�ִ�
                                                         v_waitTime         VARCHAR2, --��ʼ�ȴ�ʱ��
                                                         v_productDelaytime VARCHAR2, --��Ʒ����ʱ������Ʒ���еģ�
                                                         v_onlineDelaytime  VARCHAR2, --������ʱ��
                                                         v_status           VARCHAR2, --��Ʒ״̬
                                                         v_productType      VARCHAR2, --��Ʒҵ������
                                                         v_orderIndex       VARCHAR2, --���ȼ�
                                                         v_oper             VARCHAR2, --������
                                                         v_result           OUT VARCHAR2, --ִ�н��
                                                         v_insertNum        OUT VARCHAR2 --��ӻ��ܵ�����
                                                         ) AS
  /************************************************************
  ��Ʒ���ƣ��»��ѳ�ֵϵͳ2.0
  ģ�����ƣ����ݿ�ű�-��Ʒ������Ϣ�����\�޸�\ɾ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  ������Ա��wangjiang
  �������ڣ�2008-11-26
  ������������Ʒ������Ϣ�����_��ֵѭ��
  ************************************************************/
  l_curr_cardMoney VARCHAR2(20); --������Ǯ��ID
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
      PO_Productonline_City(v_ispId, --��Ӫ��
                              v_ordersource, --������Դ
                              v_provinceId, --ʡ��
                              v_codeCitys, --���� ���ԣ��Ÿ�����
                              v_userId, --�û�Id
                              l_curr_cardMoney, --¼����ֵ
                              v_schannelType, --���ȷ�ʽ
                              v_ratio, --�ۿ���
                              v_splitFlag, --�Ƿ�ִ�
                              v_waitTime, --��ʼ�ȴ�ʱ��
                              v_productDelaytime, --��Ʒ����ʱ������Ʒ���еģ�
                              v_onlineDelaytime, --������ʱ��
                              v_status, --��Ʒ״̬
                              v_productType, --��Ʒҵ������
                              l_orderIndex,-- ���ȼ�
                              v_oper,   --������
                              v_result, --ִ�н��
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
CREATE OR REPLACE PROCEDURE HF."PO_PRODUCTONLINE_DEALER" (v_ispId            VARCHAR2, --��Ӫ��
                                                      v_ordersource      VARCHAR2, --������Դ
                                                      v_provinceId       VARCHAR2, --ʡ��
                                                      v_codeCitys        VARCHAR2, --���� ���ԣ��Ÿ�����
                                                      v_userIds          VARCHAR2, --�û�Id(������ID,�ԣ��Ÿ���)
                                                      v_cardMoneys       VARCHAR2, --¼��Ķ����ֵ���ԣ��Ÿ�����
                                                      v_schannelType     VARCHAR2, --���ȷ�ʽ
                                                      v_ratio            VARCHAR2, --�ۿ���
                                                      v_splitFlag        VARCHAR2, --�Ƿ�ִ�
                                                      v_waitTime         VARCHAR2, --��ʼ�ȴ�ʱ��
                                                      v_productDelaytime VARCHAR2, --��Ʒ����ʱ������Ʒ���еģ�
                                                      v_onlineDelaytime  VARCHAR2, --������ʱ��
                                                      v_status           VARCHAR2, --��Ʒ״̬
                                                      v_productType      VARCHAR2, --��Ʒҵ������
                                                      v_orderIndex       VARCHAR2, --���ȼ�
                                                      v_applyUserName    VARCHAR2, --������ add by gengxj
                                                      v_result           OUT VARCHAR2, --ִ�н��
                                                      v_insertNum        OUT VARCHAR2 --��ӻ��ܵ�����
                                                      ) AS
  /************************************************************
  ��Ʒ���ƣ��»��ѳ�ֵϵͳ2.0
  ģ�����ƣ����ݿ�ű�-��Ʒ������Ϣ�����\�޸�\ɾ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  ������Ա��wangjiang
  �������ڣ�2008-11-26
  ������������Ʒ������Ϣ�����_������ѭ��
  ************************************************************/
  l_curr_userId  VARCHAR2(20); --ѭ��ʱ�õ��Ĵ�����ID
  l_curr_purseId VARCHAR2(20); --������Ǯ��ID
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
      PO_PRODUCTONLINE_CARDMONEY(v_ispId, --��Ӫ��
                                   v_ordersource, --������Դ
                                   v_provinceId, --ʡ��
                                   v_codeCitys, --���� ���ԣ��Ÿ�����
                                   l_curr_purseId, --�û�Id(������ID,�ԣ��Ÿ���)
                                   v_cardMoneys, --¼��Ķ����ֵ���ԣ��Ÿ�����
                                   v_schannelType, --���ȷ�ʽ
                                   v_ratio, --�ۿ���
                                   v_splitFlag, --�Ƿ�ִ�
                                   v_waitTime, --��ʼ�ȴ�ʱ��
                                   v_productDelaytime, --��Ʒ����ʱ������Ʒ���еģ�
                                   v_onlineDelaytime, --������ʱ��
                                   v_status, --��Ʒ״̬
                                   v_productType, --��Ʒҵ������
                                   v_orderIndex,--���ȼ�
                                   v_applyUserName, --������ 
                                   v_result, --ִ�н��
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
                                                    v_op_type          VARCHAR2, --�������� 0:��� 1:�޸�
                                                    v_onlineIds        VARCHAR2, --����id(�޸�ʱ��)
                                                    v_ispId            VARCHAR2, --��Ӫ��
                                                    v_orderSource      VARCHAR2, --������Դ
                                                    v_provinceId       VARCHAR2, --ʡ��
                                                    v_codecitys        VARCHAR2, --���� ���ԣ��Ÿ�����
                                                    v_userIds          VARCHAR2, --�û�Id(������ID,�ԣ��Ÿ���)
                                                    v_cardMoneys       VARCHAR2, --¼��Ķ����ֵ���ԣ��Ÿ�����
                                                    v_schannelType     VARCHAR2, --���ȷ�ʽ
                                                    v_ratio            VARCHAR2, --�ۿ���
                                                    v_splitFlag        VARCHAR2, --�Ƿ�ִ�
                                                    v_waitTime         VARCHAR2, --��ʼ�ȴ�ʱ��
                                                    v_productDelaytime VARCHAR2, --��Ʒ����ʱ������Ʒ���еģ�
                                                    v_onlineDelaytime  VARCHAR2, --������ʱ��
                                                    v_status           VARCHAR2, --��Ʒ״̬
                                                    v_productType      VARCHAR2, --��Ʒҵ������
                                                    v_orderIndex       VARCHAR2, --���ȼ�
                                                    v_applyUserName    VARCHAR2, --������   add by gengxj
                                                    v_result           OUT VARCHAR2, --ִ�н��
                                                    v_insertNum        OUT VARCHAR2 --��ӻ��ܵ�����
                                                    ) AS

  /**********************************
  ���ܣ����������޸��漰����
  ���ߣ�
  ʱ�䣺
        �ô洢��������ֻ���漰������ӡ�
        v2 weisd 2011-08-09 l_codecitys �޸ĳ���
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
    --�û�id����
    IF (v_userIds IS NULL) THEN
      --������Ϊ'ALL'
      l_userIds := 'ALL';
    ELSE
      l_userIds := UPPER(v_userIds);
    END IF;
    --���в���
    IF (v_codecitys IS NULL) THEN
      --������Ϊ'00'
      l_codecitys := '00';
    ELSE
      l_codecitys := v_codecitys;
    END IF;
 OPEN cur_provinceIds ;
 LOOP
   FETCH cur_provinceIds INTO lv_pid ;
   EXIT WHEN cur_provinceIds%NOTFOUND;

    PO_PRODUCTONLINE_DEALER(v_ispId, --��Ӫ��
                              v_ordersource, --������Դ
                              lv_pid, --ʡ��
                              l_codecitys, --���� ���ԣ��Ÿ�����
                              l_userIds, --�û�Id(������ID,�ԣ��Ÿ���)
                              v_cardMoneys, --¼��Ķ����ֵ���ԣ��Ÿ�����
                              v_schannelType, --���ȷ�ʽ
                              v_ratio, --�ۿ���
                              v_splitFlag, --�Ƿ�ִ�
                              v_waitTime, --��ʼ�ȴ�ʱ��
                              v_productDelaytime, --��Ʒ����ʱ������Ʒ���еģ�
                              v_onlineDelaytime, --������ʱ��
                              v_status, --��Ʒ״̬
                              v_productType , --��Ʒҵ������
                              v_orderIndex,--���ȼ�
                              v_applyUserName,      --������ add by gengxj
                              v_result, --ִ�н��
                              v_insertNum --��ӻ��ܵ�����
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
CREATE OR REPLACE PROCEDURE HF.PO_PRODUCTONLINE_UPDATE(v_opttype      VARCHAR2, --�������� 0 ״̬�޸� 1 �޸����������޸�  2 ɾ��
                                                      v_onlineids    VARCHAR2, --Ҫ�޸ĵ����л���id���ԡ���������
                                                      v_status       VARCHAR2, --����״̬ 0�ϼ� 1�¼� 2��ͣ
                                                      v_delaytime    VARCHAR2, --������ʱ��
                                                      v_splitflag    VARCHAR2, --�Ƿ�֧�ִַ�0������ 1����ֹ
                                                      v_schanneltype VARCHAR2, --��������ԭ��1���������ٶȣ�2���������ɱ���3����������
                                                      v_ratio        VARCHAR2, --�ۿ���
                                                      v_userid       VARCHAR2, --�û�ID�����ĳ��Ʒ��Ҫ���ض����û�������ͬ�ļ۸���˴���ֵ���������û�����ALL
                                                      v_remark       VARCHAR2, --������ע
                                                      v_waittime     VARCHAR2, --�ȴ�����ʱ��
                                                      v_orderindex   VARCHAR2, --���ȼ�
                                                      v_lockstatus   VARCHAR2, --��������
                                                      v_opter        VARCHAR2, --������
                                                      v_result       OUT VARCHAR2 --,
                                                      ) AS
  /**************************************************************************************
  ��Ʒ���ƣ�֧������+����ϵͳ3.0
  ģ�����ƣ����ݿ�ű�-���ֻ���ֵ-->������Ϣ����
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  ������Ա��weisd
  �������ڣ�2011-07-11
  ����������
            �ϲ����ܼ����޸Ĳ�������¼��־����˱�
            ��Ϣ��¼����˱���һ����Ҫ��ˣ�ĳЩֻ��Ϊ�˼�¼��־��Ϣ
            ����  v_opttype �������֣�
             0:״̬�޸�
             1:�޸����������޸�(�������)
             2:������Ϣ�޸���Ҫ��˵ģ����ۿ����ɸı�
             3:�����ͽ���
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
    -- ������˼�¼���������ﲻ�������Ϣ,״̬ʹ��ͬһ
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
       '�������¼�״̬�޸�',
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
  
    --״̬�޸�(�����ܵ����¼�)
    UPDATE PO_PRODUCTONLINEINFO t
       SET t.product_status = v_status
     WHERE t.online_id IN (SELECT COLUMN_VALUE
                             FROM TABLE(CAST(Myconvert(v_onlineids, ',') AS t_vc))
                            WHERE COLUMN_VALUE IS NOT NULL);
  
  ELSIF v_opttype = '1' THEN
  
    -- ������˼�¼���������ﲻ�������Ϣ,״̬ʹ��ͬһ
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
       '�����޸�,��������', --v_remark,
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
  
    l_product_status := null; --״̬���޸�
  
    --��¼�����޸�������־
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
       '�����޸�,����', --v_remark,
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
    --��������
  
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
    v_Result       OUT           VARCHAR2, --�����
    v_HFserialid   OUT           VARCHAR2  --������
)
IS
/******************************************************************************
��Ʒ���ƣ����ѳ�ֵϵͳ
��Ʒ�汾��V1a
ģ�����ƣ����ݿ�ű�-���ɻ��ѳ�ֵ��ˮ
ģ��汾��1.0.0.0
���뻷����ORACLE9i��ORACLE10g
�޸���Ա��wjjava
�޸����ڣ�2011-04-15
�޸����ݣ�
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
    l_sysid := '01';--Ŀǰֻ��һ������ƽ̨
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
    v_Result       OUT           VARCHAR2, --�����
    v_OrderID      OUT           VARCHAR2  --������
)
IS
/******************************************************************************
��Ʒ���ƣ����ѳ�ֵϵͳ
��Ʒ�汾��V1a
ģ�����ƣ����ݿ�ű�-���ɶ�����
ģ��汾��1.0.0.0
���뻷����ORACLE9i��ORACLE10g
�޸���Ա��wjjava
�޸����ڣ�2011-04-15
�޸����ݣ�
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
    l_sysid := '01';--Ŀǰֻ��һ������ƽ̨
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
CREATE OR REPLACE PROCEDURE HF.SP_HF_GETCHANNEL(v_orderid     VARCHAR2, --������
                                             v_result      OUT VARCHAR2, --���������
                                             v_channelid   OUT VARCHAR2, --����id
                                             v_stationtype OUT VARCHAR2, --վ������
                                             v_fullmoney   OUT VARCHAR2 --��ֵ���
                                             
                                             )
/************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V3a
  ģ�����ƣ����ݿ�ű�-ȡ��ֵ����
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-05-06
  �޸����ݣ�
  ************************************************************/
 AS
  l_ordersource NUMBER;
  l_userid      varchar2(30);
  l_isptype     NUMBER;
  l_provinceid  NUMBER;
  l_citycode    VARCHAR2(10);
  l_fullmoney   NUMBER;
  l_reqmoney    NUMBER(10, 2); --�������

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

  l_nocardcount NUMBER; --��������������

  l_ryswcount NUMBER; --��������������ͨ����
  l_ucswitch  NUMBER; --��ͨ3a��ֵ����

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

  --1��ȡ������Ϣ
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

  --����������ֵ����
  SELECT COUNT(0)
    INTO l_needcount
    FROM od_fullnote f
   WHERE f.hf_orderid = v_orderid
     AND f.hf_acchannel IN (SELECT channel_id
                              FROM ch_channelinfo p
                             WHERE p.channel_needcard = 1
                               AND p.channel_type <> 0);

  --��������ֵ�������ϵ�����������ͬ���治ͬ��ʽѡȡ��������ʽ��ͬ�������ٶȣ��ɱ���������sql��ѯ������
  SELECT COUNT(0)
    INTO l_nocardcount
    FROM ch_channelinfo cc
   WHERE channel_needcard = 0 --����������
     AND channel_isp = l_isptype --֧����Ӫ��
     AND (channel_provinceid = 0 or
         l_provinceid in
         (select cp.channel_provinceid
             from ch_channelprovince cp
            where cp.channel_id = cc.channel_id)) --֧��ʡ��
     AND (channel_citycode = '-1' OR
         channel_citycode LIKE '%' || l_citycode || '%') --֧�ֵ���
     AND channel_status = 0 --��������
     AND (cc.channel_limit_money = -1 or
         cc.channel_money <= cc.channel_limit_money) --������û�г���
     AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
         (SELECT DISTINCT channel_id
            FROM ch_notmatchchannel nm
           WHERE order_source = l_ordersource
             AND (user_id = '-1' OR user_id = l_userid))
     AND ( --֧����ֵ
          (channel_step <> 0 AND
          MOD(l_fullmoney - channel_minmoney, channel_step) = 0 AND
          l_fullmoney >= cc.channel_minmoney AND
          l_fullmoney <= cc.channel_maxmoney) OR
          (channel_step = 0 AND
          l_fullmoney IN
          (SELECT DISTINCT money
              FROM ch_money
             WHERE channel_id = cc.channel_id)) OR --�ɷִ�
          (l_SplitFlag = 0 AND l_fullmoney >= 100) OR --�ɲ���
          (l_SplitFlag = 2 AND l_fullmoney > 50 AND l_finishmoney > 0))
     AND EXISTS (SELECT *
            FROM ch_supporttbusiness
           WHERE channel_id = cc.channel_id
             AND service_type = l_servicetype);

  --<<����Ҫ������>>
  --=====================================================================================
  <<NoCard_Label>>
  IF l_schanneltype = 1 THEN
    --�ٶ�����
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
                               WHERE channel_needcard = 0 --����������
                                 AND channel_isp = l_isptype --֧����Ӫ��
                                 AND (channel_provinceid = 0 or
                                     l_provinceid in
                                     (select cp.channel_provinceid
                                         from ch_channelprovince cp
                                        where cp.channel_id = cc.channel_id)) --֧��ʡ��
                                 AND (channel_citycode = '-1' OR
                                     channel_citycode LIKE
                                     '%' || l_citycode || '%') --֧�ֵ���
                                 AND channel_status = 0 --��������
                                 AND (cc.channel_limit_money = -1 or
                                     cc.channel_money <=
                                     cc.channel_limit_money) --������û�г���
                                 AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
                                     (SELECT DISTINCT channel_id
                                        FROM ch_notmatchchannel nm
                                       WHERE order_source = l_ordersource
                                         AND (user_id = '-1' OR
                                             user_id = l_userid))
                                 AND ( --֧����ֵ
                                      (channel_step <> 0 AND
                                      MOD(l_fullmoney - channel_minmoney,
                                           channel_step) = 0 AND
                                      l_fullmoney >= cc.channel_minmoney AND
                                      l_fullmoney <= cc.channel_maxmoney) OR
                                      (channel_step = 0 AND
                                      l_fullmoney IN
                                      (SELECT DISTINCT money
                                          FROM ch_money
                                         WHERE channel_id = cc.channel_id)) OR --�ɷִ�
                                      (l_SplitFlag = 0 AND l_fullmoney >= 100 AND
                                      l_fullmoney >= cc.channel_minmoney) OR --�ɲ���
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
    
      --ֻ��һ�����������Ĳ���������������2��1��ϵ�ߣ������򲻱�
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
    --�ɱ�����
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
                               WHERE channel_needcard = 0 --����������
                                 AND channel_isp = l_isptype --֧����Ӫ��
                                 AND (channel_provinceid = 0 or
                                     l_provinceid in
                                     (select cp.channel_provinceid
                                         from ch_channelprovince cp
                                        where cp.channel_id = cc.channel_id)) --֧��ʡ��
                                 AND (channel_citycode = '-1' OR
                                     channel_citycode LIKE
                                     '%' || l_citycode || '%') --֧�ֵ���
                                 AND channel_status = 0 --��������
                                 AND (cc.channel_limit_money = -1 or
                                     cc.channel_money <=
                                     cc.channel_limit_money) --������û�г���
                                 AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
                                     (SELECT DISTINCT channel_id
                                        FROM ch_notmatchchannel nm
                                       WHERE order_source = l_ordersource
                                         AND (user_id = '-1' OR
                                             user_id = l_userid))
                                 AND ( --֧����ֵ
                                      (channel_step <> 0 AND
                                      MOD(l_fullmoney - channel_minmoney,
                                           channel_step) = 0 AND
                                      l_fullmoney >= cc.channel_minmoney AND
                                      l_fullmoney <= cc.channel_maxmoney) OR
                                      (channel_step = 0 AND
                                      l_fullmoney IN
                                      (SELECT DISTINCT money
                                          FROM ch_money
                                         WHERE channel_id = cc.channel_id)) OR --�ɷִ�
                                      (l_SplitFlag = 0 AND l_fullmoney >= 100 AND
                                      l_fullmoney >= cc.channel_minmoney) OR --�ɲ���
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
    
      --ֻ��һ�����������Ĳ���������������2��1��ϵ�ߣ������򲻱�
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
    --������
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
                               WHERE cc.channel_needcard = 0 --����������
                                 AND cc.channel_isp = l_isptype --֧����Ӫ��
                                 AND (channel_provinceid = 0 or
                                     l_provinceid in
                                     (select cp.channel_provinceid
                                         from ch_channelprovince cp
                                        where cp.channel_id = cc.channel_id)) --֧��ʡ��
                                 AND (cc.channel_citycode = '-1' OR
                                     channel_citycode LIKE
                                     '%' || l_citycode || '%') --֧�ֵ���
                                 AND cc.channel_status = 0 --��������
                                 AND (cc.channel_limit_money = -1 or
                                     cc.channel_money <=
                                     cc.channel_limit_money) --������û�г���
                                 AND cc.channel_id NOT IN --���ǽ�ֹ�ߵ�����
                                     (SELECT DISTINCT channel_id
                                        FROM ch_notmatchchannel nm
                                       WHERE order_source = l_ordersource
                                         AND (user_id = '-1' OR
                                             user_id = l_userid))
                                 AND ( --֧����ֵ
                                      (channel_step <> 0 AND
                                      MOD(l_fullmoney - cc.channel_minmoney,
                                           cc.channel_step) = 0 AND
                                      l_fullmoney >= cc.channel_minmoney AND
                                      l_fullmoney <= cc.channel_maxmoney) OR
                                      (channel_step = 0 AND
                                      l_fullmoney IN
                                      (SELECT DISTINCT money
                                          FROM ch_money
                                         WHERE channel_id = cc.channel_id)) OR --�ɷִ�
                                      (l_SplitFlag = 0 AND l_fullmoney >= 100 AND
                                      l_fullmoney >= cc.channel_minmoney) OR --�ɲ���
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
    
      --ֻ��һ�����������Ĳ���������������2��1��ϵ�ߣ������򲻱�
      IF (l_nocardcount = 1 AND
         l_noneedcount - l_trycount + 1 <= 2 * l_needcount + 2) OR
         (l_noneedcount - l_trycount <= l_needcount) THEN
        --��ǰ����ֵ��1
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

  --<<������������ֵ���>>
  --=========================================================================================
  <<chargemoney_label>>
  IF v_result <> '0' THEN
    --û�����������������ߴ�������
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

  GOTO End_Label; --ȡ����������

  --<<��������>>
  --=======================================================================================
  <<NeedCard_Label>>
--ȡ���ζ�����ͬ���ʹ�������
  SELECT NVL(SUM(decode(c.channel_type, 1, 1, 0)), 0),
         NVL(SUM(decode(c.channel_type, 4, 1, 3, 1, 0)), 0),
         NVL(SUM(decode(c.channel_type, 11, 1, 0)), 0)
    INTO l_asrunioncount, l_ykccount, l_swebcount
    FROM od_fullnote f, ch_channelinfo c
   WHERE f.hf_orderid = v_orderid
     AND f.hf_status IS NOT NULL
     AND f.hf_acchannel = c.channel_id
     AND c.channel_type IN (1, 3, 4, 11);

  --�������ͨ������������������Ĵ����ж�
  IF l_isptype = 0 THEN
    SELECT COUNT(*)
      INTO l_ryswcount
      FROM ch_channelinfo t
     WHERE t.channel_type = 4
       AND t.channel_isp = 0
       AND t.channel_needcard = 1
       AND t.channel_status = 0;
  END IF;

  --ȡ��������
  BEGIN
    SELECT channel_id, channel_stationtype
      INTO v_channelid, v_stationtype
      FROM (SELECT ccl.channel_id, ccl.channel_stationtype, ccl.channel_type
              FROM ch_channelinfo ccl
             WHERE ccl.channel_needcard = 1 --��Ҫ��
               AND ccl.channel_isp = l_isptype
               AND (ccl.channel_provinceid = 0 or
                   l_provinceid in
                   (select cp.channel_provinceid
                       from ch_channelprovince cp
                      where cp.channel_id = ccl.channel_id)) --֧��ʡ��
               AND (channel_citycode = '-1' OR
                   channel_citycode LIKE '%' || l_citycode || '%') --֧�ֵ���
               AND channel_status IN (0, 2) --��������
               AND (ccl.channel_limit_money = -1 or
                   ccl.channel_money <= ccl.channel_limit_money) --������û�г���
               AND ((channel_type NOT IN (0, 9, 10) AND l_isptype <> 0) --�ų�007��������������ֵ����
                   OR (l_ucswitch <= 0 AND
                   channel_type NOT IN (0, 9, 10, 3) AND l_isptype = 0) OR
                   (l_ucswitch <= 0 AND channel_type = 3 AND l_isptype = 0 AND
                   l_ryswcount > 0) --�����һ�������������ж����������������ڿ�ͨ
                   OR (l_ucswitch > 0 AND channel_type IN (1, 4) AND
                   l_isptype = 0) --�����3a����ȡ�������������ƽ̨
                   )
               AND channel_id <> 145
               AND channel_id NOT IN --���ǽ�ֹ�ߵ�����
                   (SELECT DISTINCT channel_id
                      FROM ch_notmatchchannel
                     WHERE order_source = l_ordersource
                       AND (user_id = '-1' OR user_id = l_userid))
               AND ( --֧����ֵ
                    (channel_step <> 0 AND
                    MOD(l_fullmoney - channel_minmoney, channel_step) = 0 AND
                    l_fullmoney >= ccl.channel_minmoney AND
                    l_fullmoney <= ccl.channel_maxmoney) OR
                    (channel_step = 0 AND
                    l_fullmoney IN
                    (SELECT DISTINCT money
                        FROM ch_money
                       WHERE channel_id = ccl.channel_id)) OR --�ɷִ�
                    (l_SplitFlag <> 1 AND l_fullmoney >= channel_minmoney))
               AND EXISTS
             (SELECT *
                      FROM ch_supporttbusiness
                     WHERE channel_id = ccl.channel_id
                       AND service_type = l_servicetype)
               AND ( --�ƶ��ɷִζ�����С��30Ԫʱ����ȡȫ������������
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
                      decode(ccl.channel_type, 11, 0, 1, 1, 2) ASC --����web������Ȼ��asr����
            )
     WHERE rownum = 1;
    v_fullmoney := l_fullmoney;
  EXCEPTION
    WHEN OTHERS THEN
      IF v_Result = '81001' THEN
        l_trycount := l_trycount + 1;
        GOTO NoCard_Label;
      ELSE
        v_result := '1007'; --��֧�ֲ���������
        RETURN;
      END IF;
  END;

  --<<����������>>
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
create or replace procedure hf.SP_M_CHANNEL_ONOFF(v_channel_ids  varchar2, --�漰��������ID��ϣ��ԡ�,������
                                               v_opt_type     number, --��������0����ͨ  1�� �ر�
                                               v_auto_no      number, --�Ƿ��Զ���ͨ  0������Ҫ   1����Ҫ  2����ִ��
                                               v_opt_userid   varchar2, --������ID
                                               v_opt_username varchar2, --����������
                                               v_opt_log      varchar2, --������־
                                               v_on_time      varchar2, --���v_auto_no=1������Ϊ��ͨʱ��
                                               v_linkOnline   number, --�Ƿ�������� 0�������� 1������         
                                               v_result       out VARCHAR2,
                                               v_desc         out varchar2)
/******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ��ֵ�������ع���
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��wjjava
  �����������Գ�ֵ�����Ŀ��ز������м�¼
            �����ͨ
            --1.���ر�����ʱ�¼ܵĻ����ϼܣ���Ҫ�жϴ˻����Ƿ���Ϊ�¼� 
            --2.�ж��Ƿ��������Զ���ͨ����������˻�ûִ�У��޸ĳ���ִ��
            --3.�޸�û��ƥ�䵽�������ҿ���ͨ����������ֵ�Ķ�����״̬Ϊ�ȴ���ֵ
            --4.�޸�����״̬Ϊ��ͨ
            --4.��¼��־
           ����ر�
            --1.�¼�ֻ��ͨ����������ֵ�Ļ���
            --2.�޸�����״̬Ϊ�ر�
            --3.��¼��־
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
  --������֤
  if v_channel_ids is null or v_opt_type is null or v_auto_no is null or
     v_opt_userid is null or (v_opt_type <> 0 and v_opt_log is null) or
     (v_auto_no = 1 and v_on_time is null) then
    v_result := '0001';
    v_desc   := '��������';
    return;
  end if;
  if v_opt_type = 0 then
    --��ͨͨ��
    --2.�ж��Ƿ��������Զ���ͨ����������˻�ûִ�У��޸ĳ���ִ��
    --3.�޸�û��ƥ�䵽�������ҿ���ͨ����������ֵ�Ķ�����״̬Ϊ�ȴ���ֵ
    --����״̬��0�򿪣�1�رգ�2һ����ͨ���رգ�3��ͣ  ,5����ά���Զ��ر�,8ǩ�˹ر�
    FOR channel IN (select *
                      from ch_channelinfo t
                     where t.channel_id in
                           (select column_value
                              from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
                             where column_value is not null)
                       and t.channel_status in (1, 2, 3)) LOOP
      if v_linkOnline = 1 then
        --�����������
        --1.���ر�����ʱ�¼ܵĻ����ϼܣ���Ҫ�жϴ˻����Ƿ���Ϊ�¼� 
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
                           and t.apply_type = 0 --���¼�   
                           and t.auditing_status = 1 --����ͨ��
                        ) a,
                       po_productonlineinfo b,
                       po_productinfo p
                 where a.online_id = b.online_id
                   and a.rn = 1
                   and a.apply_username = 'channel' --���һ�����������رն��¼ܵ�
                   and a.status = 1 --���һ��Ϊ�¼�
                   and b.product_status = 1 --��ǰΪ�¼ܵĻ���
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
                                     and t.apply_type = 0 --���¼�   
                                     and t.auditing_status = 1 --����ͨ��
                                  ) a,
                                 po_productonlineinfo b,
                                 po_productinfo p
                           where a.online_id = b.online_id
                             and a.rn = 1
                             and a.apply_username = 'channel' --���һ�����������رն��¼ܵ�
                             and a.status = 1 --���һ��Ϊ�¼�
                             and b.product_status = 1 --��ǰΪ�¼ܵĻ���
                             and b.product_id = p.product_id)
                   where ',' || channel.channel_id || ',' like channelIds);
          --��¼�����ϼ���־
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
             '������ͨ���������ϼ�',
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
                               and t.apply_type = 0 --���¼�   
                               and t.auditing_status = 1 --����ͨ��
                            ) a,
                           po_productonlineinfo b,
                           po_productinfo p
                     where a.online_id = b.online_id
                       and a.rn = 1
                       and a.apply_username = 'channel' --���һ�����������رն��¼ܵ�
                       and a.status = 1 --���һ��Ϊ�¼�
                       --and b.product_status = 1 --��ǰΪ�¼ܵĻ���
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
               t.done_log  = '���˹���ͨ��������ִ��!',
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
      v_desc := '������' || l_online_count || '���ʻ���ͬʱ���ϼܡ�';
    end if;
    --4.�޸�����״̬
    update ch_channelinfo t
       set t.channel_status = 0
     where t.channel_id in
           (select column_value
              from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
             where column_value is not null)
       and t.channel_status in (1, 2, 3);
    --5.������־
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
    v_desc := '������ͨ�ɹ���' || v_desc;
  elsif v_opt_type = 1 then
    --�ر�����
    l_count         := 0;
    l_channel_count := 0;
    if v_linkOnline = 1 then
      --�����������
      --1.�¼�ֻ��ͨ����������ֵ�Ļ���
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
          --��¼�����ϼ���־
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
             '�����رչ��������¼�',
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
    
      v_desc := '������' || l_count || '���ʻ���ͬʱ���¼ܡ�';
    end if;
    --2.�޸�����״̬Ϊ�ر�
    update ch_channelinfo t
       set t.channel_status = 1
     where t.channel_id in
           (select column_value
              from table(cast(MYCONVERT(v_channel_ids, ',') as t_vc))
             where column_value is not null)
       and t.channel_status = 0;
    --3.������־
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
    v_desc := '�����رճɹ���' || v_desc;
  end if;
  commit;
EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    v_desc   := '�洢����ִ���쳣������ʧ�ܣ�';
    ROLLBACK;
end;
/

prompt
prompt Creating procedure SP_JOB_CHANNEL_AUTO_ONOFF
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_JOB_CHANNEL_AUTO_ONOFF is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-�����Զ�����JOB(41)
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-6-13
  �޸����ݣ�
  ����������ÿ2����ִ��һ�Σ������������ع��������õ��Զ��������ݵ�ָ��ʱ��ִ��
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
      --����Ϊ��ͨʱ���Զ�����Ϊ�ر�
      l_doneLog := '�����Ѿ�Ϊ�ر�״̬�������ٹرգ�';
      l_isDone  := 1;
    elsif channel_auto.opt_type = 1 and l_channelStatus = 0 then
      --����Ϊ�ر�ʱ���Զ�����Ϊ��ͨ
      l_doneLog := '�����Ѿ�Ϊ��ͨ״̬�������ٿ�ͨ��';
      l_isDone  := 1;
    else
      l_doneLog := '�Զ�ִ�н����';
    end if;
    select decode(channel_auto.opt_type, 0, 1, 1, 0)
      into l_optType
      from dual;
    if l_optType is null then
      l_doneLog := '����������ִֹ�У�';
      l_isDone  := 1;
    end if;
    --�޸�Ϊ��ִ��
    update ch_onoff_log l
       set l.auto_no = 2, l.done_log = l_doneLog, l.done_time = sysdate
     where l.log_id = channel_auto.log_id;
    if l_isDone = 0 then
      sp_m_channel_onoff(v_channel_ids  => channel_auto.channel_id,
                         v_opt_type     => l_optType,
                         v_auto_no      => 0,
                         v_opt_userid   => 'system',
                         v_opt_username => 'system',
                         v_opt_log      => '������' || channel_auto.log_id ||
                                           '���Զ�ִ��',
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
    ��Ʒ���ƣ���ֵϵͳ
    ģ�����ƣ����ݿ�ű�-�Զ��������ܼ۸�
    ģ��汾��1.0.0.0
    ���뻷����ORACLE10g
    ������Ա��wjjava
    �������ڣ�2011-07-1
    ��������������po_rule_autochange���е����ã�ÿ���賿ִ��һ�Σ��޸ķ��������Ļ��ܼ۸񣬲���¼��־
  *******************************************************/

  l_day       number(2);
  l_begindate date;
  l_enddate   date;
  l_count     number;
  l_manId     number;
begin
  --��һ����ִ�м�¼
  for online_auto in (select *
                        from po_rule_autochange t
                       where t.adjust_type = 0) loop
    if online_auto.cyc_id = 0 and online_auto.adjust_type = 0 then
      --�������Ϊÿ��ִ��һ�Σ�����������ʱ������
      --�жϵ�ǰ�����Ƿ�Ϊ1��
      select to_number(to_char(sysdate, 'dd')) into l_day from dual;
      select count(1)
        into l_count
        from po_rule_rundetial t
       where t.rule_id = online_auto.id
         and t.begindate >= trunc(sysdate, 'MONTH');
      if l_day = 1 and l_count = 0 then
        --���Ϊ1����û������ִ�����ݣ�������ִ�е����ݣ����򲻴�������������һ����¼
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
  --���������Զ�ִ�м�¼��״̬Ϊδִ�С�ʱ����Ҫ������ʱ�䷶Χ֮�ڵļ�¼�����м۸����

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
       AND (run_auto.req_userid = 'ALL' OR run_auto.req_userid = q.user_id) --������id
       AND (run_auto.product_isptype = -1 OR
           run_auto.product_isptype = p.product_ispid) --��Ӫ��
       AND (run_auto.product_provinceid = -1 OR
           run_auto.product_provinceid = p.product_provinceid) --ʡ��
       AND (run_auto.product_money = -1 OR
           run_auto.product_money = p.product_content) --��ֵ
       AND (run_auto.product_delaytime = -1 OR
           run_auto.product_delaytime = p.product_delaytime);
    if l_count > 0 then
      --������ܲ�����־����
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
         '���ܼ۸��Զ�����', --v_remark,
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
               run_auto.req_userid = q.user_id) --������id
           AND (run_auto.product_isptype = -1 OR
               run_auto.product_isptype = p.product_ispid) --��Ӫ��
           AND (run_auto.product_provinceid = -1 OR
               run_auto.product_provinceid = p.product_provinceid) --ʡ��
           AND (run_auto.product_money = -1 OR
               run_auto.product_money = p.product_content) --��ֵ
           AND (run_auto.product_delaytime = -1 OR
               run_auto.product_delaytime = p.product_delaytime) --��������
        ;
      --�޸ļ۸�
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
                     run_auto.req_userid = q.user_id) --������id
                 AND (run_auto.product_isptype = -1 OR
                     run_auto.product_isptype = p.product_ispid) --��Ӫ��
                 AND (run_auto.product_provinceid = -1 OR
                     run_auto.product_provinceid = p.product_provinceid) --ʡ��
                 AND (run_auto.product_money = -1 OR
                     run_auto.product_money = p.product_content) --��ֵ
                 AND (run_auto.product_delaytime = -1 OR
                     run_auto.product_delaytime = p.product_delaytime));
    end if;
    --�޸ļ�¼״̬Ϊ��ִ��
    update po_rule_rundetial t
       set t.status = 1, t.man_id = l_manId
     where t.id = run_auto.id;
  
  end loop;
  --���������Զ�ִ�м�¼��״̬Ϊ��ִ�С�ʱ����Ҫ������ʱ�䷶Χ֮��ļ�¼�����м۸�ָ�
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
    --������ܲ�����־����
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
       '���ܼ۸��Զ��ָ���������' || rollback_auto.man_id || '��',
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
  
    --�޸ļ۸�
    UPDATE PO_PRODUCTONLINEINFO t
       SET t.product_price = (select d.old_price
                                from po_auditing_detail d
                               where d.man_id = rollback_auto.man_id
                                 and d.online_id = t.online_id)
     WHERE t.online_id IN
           (select distinct a.online_id
              FROM po_auditing_detail a
             WHERE a.man_id = rollback_auto.man_id);
    --�޸ļ�¼״̬Ϊ�ѻָ�
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-��������Ϣ���еġ����������ս�������ÿ���賿��0
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-6-13
  �޸����ݣ�
  ���������������ݿ� job����
            v1����������Ϣ���еġ����������ս�������ÿ���賿��0
  
  ************************************************************/
  l_warninfo VARCHAR2(200); -- ������Ϣ
BEGIN

  update CH_CHANNELINFO c
     set c.channel_money = 0
   where c.channel_status = 0;

  commit;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    -- �򱨾�������ӱ�����¼
    l_warninfo := 'ÿ���賿���������ս�����Ϊ0���쳣������';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
  
END;
/

prompt
prompt Creating procedure SP_M_CHANNELMANAGER
prompt ======================================
prompt
create or replace procedure hf.SP_M_CHANNELMANAGER(v_dealType               number, --����ʽ 0����� 1���޸� 2��ɾ��
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
                                                v_channel_provinceid     varchar2, --ʡ��id,��','����
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ��ֵ��������
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��wjjava
  �����������Գ�ֵ���������ӡ�ɾ�����޸ĵĲ���
  2011-08-04�޸�
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
      
        --��������ʡ�ݱ�
        insert into ch_channelprovince
          select v_channel_id, column_value
                    from table(cast(MYCONVERT(v_channel_provinceid, ',') as t_vc))
                   where column_value is not null;
          --���뵽������ֵ��
           insert into ch_money
            select v_channel_id, column_value
                    from table(cast(MYCONVERT(v_supportMoneys, ',') as t_vc))
                   where column_value is not null;
        --���뵽����ҵ�����ͱ�
        insert into ch_supporttbusiness
          select v_channel_id, column_value
            from table(cast(MYCONVERT(v_channelSupportBusiness, ',') as t_vc))
           where column_value is not null;
        v_desc := '������ӳɹ���';
   
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
      
        --��������ʡ�ݱ�
        insert into ch_channelprovince(channel_id,channel_provinceid)
        values(v_channel_id,to_number(v_channel_provinceid));
          
          --���뵽������ֵ��
           insert into ch_money
            select v_channel_id, column_value
                    from table(cast(MYCONVERT(v_supportMoneys, ',') as t_vc))
                   where column_value is not null;
      
        --���뵽����ҵ�����ͱ�
        insert into ch_supporttbusiness
          select v_channel_id, column_value
            from table(cast(MYCONVERT(v_channelSupportBusiness, ',') as t_vc))
           where column_value is not null;
        v_desc := '������ӳɹ���';
                   
      end if;
    
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        v_result := '0001';
        v_desc   := '����ID�ظ�';
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
    --���뵽������ֵ��
    insert into ch_money
      select v_channel_id, column_value
        from table(cast(MYCONVERT(v_supportMoneys, ',') as t_vc))
       where column_value is not null;
    --���뵽����ҵ�����ͱ�
    delete ch_supporttbusiness t where t.channel_id = v_channel_id;
    insert into ch_supporttbusiness
      select v_channel_id, column_value
        from table(cast(MYCONVERT(v_channelSupportBusiness, ',') as t_vc))
       where column_value is not null;
    v_desc := '�����޸ĳɹ���';
  elsif v_dealType = 2 then
    delete ch_money t where t.channel_id = v_channel_id;
    delete ch_supporttbusiness t where t.channel_id = v_channel_id;
    delete ch_channelinfo where channel_id = v_channel_id;
    v_desc := '����ɾ���ɹ���';
  end if;
  commit;
EXCEPTION
  WHEN OTHERS THEN
    v_result := '1111';
    v_desc   := '�洢����ִ���쳣������ʧ�ܣ�';
    ROLLBACK;
end;
/

prompt
prompt Creating procedure SP_M_MANUALDEALORDER_LOCK
prompt ============================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_M_MANUALDEALORDER_LOCK(v_opt_type       IN NUMBER, --�������� 0������ 1������
                                                       v_isjudge_status in number, --�Ƿ��ж�״̬ 0���ж� 1�����ж�
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �˹���������
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��wjjava
  ����������������ǰ����Ķ�������ֹ����ͬʱ����ͬһ�ʶ���
            v2 weisd 2011-08-10 ���䶩��ƥ�䲻������ʧ�ܴ���  2 �������������ý���
               v_hf_serialid Ϊ������
  ******************************************************************************/
BEGIN
  v_res := '0000';
  IF v_opt_type = 0 THEN
    IF v_hf_serialid IS NULL OR v_opt_userid IS NULL THEN
      v_res  := '0001';
      v_desc := '������ȫ';
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
        v_desc := '�ó�ֵ��¼�ѱ�����';
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
      v_desc := '������ȫ';
      RETURN;
    END IF;
    DELETE od_customcheck_failorder H WHERE H.HF_SERIALID = v_hf_serialid;
    v_desc := '�����ɹ�';
    commit;
    RETURN;
    -- ��������
  ELSIF v_opt_type = 2 THEN
    IF v_hf_serialid IS NULL OR v_opt_userid IS NULL THEN
      v_res  := '0001';
      v_desc := '������ȫ';
      RETURN;
    END IF;
    if v_isjudge_status = 0 then
      select count(1)
        into l_hfcount
        from od_fullnote h1
       where h1.hf_orderid = v_hf_serialid
         and h1.hf_status not in (0, 3); -- ��������״̬��
      IF l_hfcount <> 0 THEN
        v_res  := '0003';
        v_desc := '������¼����δ��ɵĳ�ֵ��¼';
        RETURN;
      END IF;

      select h2.charge_status, h2.req_errorcode
        into l_order_status, l_errorcode
        from od_orderinfo h2
       where h2.req_orderid = v_hf_serialid;

      IF l_order_status <> 8 THEN
        v_res  := '0004';
        v_desc := '�ö�����¼�ѱ�����';
        RETURN;
      END IF;
      IF l_errorcode <> '1007' THEN
        --�����޷���ȡ����
        v_res  := '0005';
        v_desc := '�ö������ǻ�ȡ����ʧ��,��ϵ�����Ա';
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
    v_desc := '������ȫ';
    RETURN;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    v_res  := '1111';
    v_desc := 'ִ�д洢�����쳣';
    ROLLBACK;
END;
/

prompt
prompt Creating procedure SP_M_MANUALDEALORDER
prompt =======================================
prompt
create or replace procedure hf.SP_M_MANUALDEALORDER(v_dealType    number, --����ʽ 0����ֵ�ɹ� 1����ֵʧ�� 2���ٴγ�ֵ
                                                  v_orderid     in VARCHAR2, --������
                                                  v_hfserialid  in VARCHAR2, --������ˮ��
                                                  v_finishmoney in NUMBER, --ʵ�ֳ�ֵ��ֻ�е�v_dealType=1ʱ����ֵ
                                                  v_checkIsp    in varchar2, --��������ʵ���
                                                  v_remark      in varchar2, --��ע
                                                  v_operid      in varchar2, --������ID���ͷ�ID��
                                                  v_opername    in varchar2, --����������
                                                  v_result      out VARCHAR2)
/******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �˹���������
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��wjjava
  �����������Խ��δ֪�����Ĵ�����������ֵ�ɹ�������ֵʧ�ܴ���������ֵ����
            08.11  weisd ���� ��������������  v_dealType ����������
  ******************************************************************************/
 AS
  l_warninfo            VARCHAR2(200); -- ������Ϣ
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
        v_result := '0001'; --�����쳣
        goto lockorder;
      end if;
      if l_curr_hfstatus not in (4, 9) or l_curr_orderstatus not in (7, 8) then
        v_result := '0002'; --״̬�쳣
        goto lockorder;
      end if;
      if v_finishmoney > l_curr_fu_fullmoney then
        v_result := '0003'; --����쳣���ɹ����ܴ��ڶ������γ�ֵ���
        goto lockorder;
      end if;
      --0����ֵ�ɹ�����
      --�޸Ļ��ѳ�ֵ��¼��״̬Ϊ�ɹ�
      --�޸Ķ���������������=�ɹ����޸�״̬Ϊ�ɹ���������ȣ��޸�״̬Ϊ�ȴ������ȴ���һ�ε��޸�
      if (v_finishmoney - l_curr_fu_fullmoney) = 0 then
        l_new_hfstatus := 0; --�ɹ�
      elsif (v_finishmoney - l_curr_fu_fullmoney) < 0 then
        l_new_hfstatus := 6; --���ֳɹ�
      elsif (v_finishmoney - l_curr_fu_fullmoney) > 0 then
        --�澯���ɹ���������������ܷ���
        l_new_hfstatus := 0; --�ɹ�
        l_warninfo     := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid ||
                          '���������ѳ�ֵ����ʵ��Ҫ����!';
        sp_sys_dberrorinfo_create(1, 'system', '', l_warninfo, '', '');
      end if;
      update od_fullnote t
         set t.hf_status    = l_new_hfstatus,
             t.finish_money = v_finishmoney,
             t.finish_time  = sysdate
       where t.hf_serialid = v_hfserialid;
      if (v_finishmoney + l_curr_finishmoney - l_curr_money) = 0 then
        l_new_orderstatus := 0; --�����ɹ�
      elsif (v_finishmoney + l_curr_finishmoney - l_curr_money) < 0 then
        if sysdate < l_curr_linetime and l_curr_trycount < 8 then
          --�����û�ж���Ҫ�����ʱ�䣬�ҳ�ֵ����û�г���8�Σ�����������ֵ
          l_new_orderstatus := 2; --�����ȴ�����
        else
          l_new_orderstatus := 6; --�������ֳɹ�
        end if;
      elsif (v_finishmoney + l_curr_finishmoney - l_curr_money) > 0 then
        --�澯���ɹ���������������ܷ���
        l_new_orderstatus := 0; --�ɹ�
        l_warninfo        := '������:' || v_orderid || '�����ѳ�ֵ��¼�л�����ˮ��:' ||
                             v_hfserialid || '�ѳ�ֵ������ʵ��Ҫ����!';
        sp_sys_dberrorinfo_create(1, 'system', '', l_warninfo, '', '');
      end if;
      UPDATE od_orderinfo t
         SET t.charge_status      = l_new_orderstatus,
             t.charge_finishmoney = v_finishmoney + l_curr_finishmoney,
             t.charge_finishtime  = sysdate
       WHERE t.req_orderid = v_orderid;

    elsif v_dealType = 1 then
      --1����ֵʧ�ܴ���
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_checkIsp is null then
        v_result := '0001'; --�����쳣
        goto lockorder;
      end if;
      if l_curr_hfstatus not in (4, 9) or l_curr_orderstatus not in (7, 8) then
        v_result := '0002'; --״̬�쳣
        goto lockorder;
      end if;
      l_new_hfstatus := 3;
      update od_fullnote t
         set t.hf_status = l_new_hfstatus, t.finish_time = sysdate
       where t.hf_serialid = v_hfserialid;
      if l_curr_finishmoney > 0 then
        l_new_orderstatus := 6; --�������ֳɹ�
      else
        l_new_orderstatus := 4; --����ʧ��
      end if;
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.req_errorcode     = l_hferrorcode,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    elsif v_dealType = 2 then
      --2���ٴγ�ֵ����
      --1�����ó�ֵ��¼״̬Ϊʧ��
      --2�����ö���״̬Ϊ�ȴ��������ӳ�Ҫ�����ʱ��5����
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_checkIsp is null then
        v_result := '0001'; --�����쳣
        goto lockorder;
      end if;
      if l_curr_hfstatus not in (4, 9) or l_curr_orderstatus not in (7, 8) then
        v_result := '0002'; --״̬�쳣
        goto lockorder;
      end if;
      l_new_hfstatus := 3; --
      update od_fullnote t
         set t.hf_status = l_new_hfstatus, t.finish_time = sysdate
       where t.hf_serialid = v_hfserialid;
      l_new_orderstatus := 2; --�����ȴ�����
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.charge_linetime   = sysdate + 5 / 1440, --��ǰʱ�䡪+5����
             t.req_errorcode     = l_hferrorcode,
             t.req_trycount      = 0,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;

    elsif v_dealType = 3 then
      --����ʧ�ܴ���
      if v_dealType is null or v_hfserialid is null or v_orderid is null or
         v_checkIsp is null then
        v_result := '0001'; --�����쳣
        goto lockorder;
      end if;
      if l_curr_orderstatus <> 8 or l_hferrorcode <> '1007' then
        v_result := '0002'; --״̬�쳣
        goto lockorder;
      end if;

      select count(1)
        into l_hfstatus_count
        from od_fullnote h1
       where h1.hf_orderid = v_orderid
         and h1.hf_status not in (0, 3); -- ��������״̬��
      IF l_hfstatus_count <> 0 THEN
        v_result  := '0004';--������¼����δ��ɵĳ�ֵ��¼
        goto lockorder;
      END IF;

      l_new_orderstatus := 4;
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.req_errorcode     = l_hferrorcode,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    end if;

    --��¼������־
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
  --�������
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
create or replace procedure hf.SP_M_WAITINGDEALORDER(v_dealType number, --����ʽ 0����ֵ�ɹ� 1����ֵʧ�� 2���ٴγ�ֵ
                                                  v_orderid  in VARCHAR2, --������
                                                  v_operid   in varchar2, --������ID���ͷ�ID��
                                                  v_opername in varchar2, --����������
                                                  v_result   out VARCHAR2,
                                                  v_desc     out varchar2)
/******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ������������
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��wjjava
  ���������������ڴ����ѹ�Ҫ�����ʱ��Ķ������д���
            ��������ֵ�ɹ�������ֵʧ�ܴ���������ֵ����
            8.12 weisd �ٴγ�ֵʱ�� ������ 1010
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
    --�������һ�γ�ֵ��¼��ˮ��
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
        v_result := '1001'; --�ü�¼�ѱ�����
        v_desc   := '�ü�¼�ѱ�����';
        return;
      end if;
    else
      v_result := '1000'; --û���ҵ���ֵ��¼
      v_desc   := 'û���ҵ���ֵ��¼';
      return;
    end if;
    if l_hfserialid is null or v_orderid is null then
      v_result := '0001'; --�����쳣
      v_desc   := '�����쳣';
      return;
    end if;
    if l_curr_hfstatus <> 2 or l_curr_orderstatus <> 3 then
      v_result := '0002'; --״̬�쳣
      v_desc   := '״̬�쳣';
      return;
    end if;
    if v_dealType = 0 then
      --0����ֵ�ɹ�����
      --�޸Ļ��ѳ�ֵ��¼��״̬Ϊ�ɹ�
      --�޸Ķ���������������=�ɹ����޸�״̬Ϊ�ɹ���������ȣ��޸�״̬Ϊ�ȴ������ȴ���һ�ε��޸�
      l_new_hfstatus := 0; --�ɹ�
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
      --1����ֵʧ�ܴ���    
      l_new_hfstatus := 3;
      update od_fullnote t
         set t.hf_status = l_new_hfstatus,t.hf_errorcode = l_hferrorcode, t.finish_time = sysdate
       where t.hf_serialid = l_hfserialid;
      l_new_orderstatus := 4; --����ʧ��
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.req_errorcode     = l_hferrorcode,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    elsif v_dealType = 2 then
      --2���ٴγ�ֵ����
      --1�����ó�ֵ��¼״̬Ϊʧ��
      --2�����ö���״̬Ϊ�ȴ��������ӳ�Ҫ�����ʱ��5����
      if l_hferrorcode is null then 
         l_hferrorcode := '1010';
      end if;
      
      l_new_hfstatus := 3;
      update od_fullnote t
         set t.hf_status = l_new_hfstatus, t.hf_errorcode = l_hferrorcode, t.finish_time = sysdate
       where t.hf_serialid = l_hfserialid;
      l_new_orderstatus := 2; --�����ȴ�����   
      UPDATE od_orderinfo t
         SET t.charge_status     = l_new_orderstatus,
             t.charge_linetime   = sysdate + 5 / 1440,
             t.req_errorcode     = l_hferrorcode,
             t.req_trycount      = 0,
             t.charge_finishtime = sysdate
       WHERE t.req_orderid = v_orderid;
    end if;
    --��¼������־
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
       'δ��ʵ',
       '',
       l_new_hfstatus,
       l_curr_hfstatus,
       l_new_orderstatus,
       l_curr_orderstatus,
       l_curr_finishtime,
       l_curr_finishmoney,
       '�����������˴���');
    v_result := '0000';
    v_desc   := '����ɹ�';
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
create or replace procedure hf.SP_OD_CANCELORDERCHARGE(v_opttype        in number, --��������
                                                    v_hfserialid     in VARCHAR2,
                                                    v_orderid        in VARCHAR2,
                                                    v_finishmoney    in NUMBER,
                                                    v_hfcancelstatus in NUMBER,
                                                    v_errorcode      in NUMBER,
                                                    v_result         out VARCHAR2)
/******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ���ѳ�����Ϣ
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-5-16
  ������ݣ�����/���»��ѳ�����Ϣ
  --
  v_opttype 3�����ؽ������  0��������£� 2 Ϊ�޸�֪ͨ״̬

  v1.1 weisd ������� v_zhorderid ������ˮ
  v2   weisd ����opttype �������� �ǲ�����»��ǽ������ �������������Ϣ��һ��
             ���ڳ�������������ɹ����޸ĳ�ֵ��¼���Լ�������״̬
             ɾ��  v_zhorderid
  ******************************************************************************/
 AS
  l_count         NUMBER; -- ������Ϣ�Ƿ����
  l_warninfo      VARCHAR2(200); -- ������Ϣ
  l_orderid       varchar2(30);
  l_hfserialid    varchar2(30);
  l_finishmoney   NUMBER(10, 2);
  l_currentstatus NUMBER(10, 2); --��ǰ����״̬

begin
  v_result      := '1111';
  l_finishmoney := 0;

  begin

    if v_opttype = 3 then
      --������ش���
      select count(1)
        into l_count
        from od_cancelordercharge t
       where t.hf_serialid = v_hfserialid;

      if l_count = 1 and v_hfcancelstatus is not null then
        -- �Ѿ������������

        select t.hf_orderid,
               t.hf_serialid,
               t.finish_money,
               t.hf_cancelstatus
          into l_orderid, l_hfserialid, l_finishmoney, l_currentstatus
          from od_cancelordercharge t
         where t.hf_serialid = v_hfserialid
           for update;

        if l_currentstatus != 0 then
          --��ǰ״̬Ϊ ���ɹ�
          if v_hfcancelstatus = 0 then
            --�����ɹ�
            update OD_FULLNOTE f
               set f.hf_status    = 3,
                   f.finish_time  = sysdate,
                   f.hf_errorcode = v_errorcode,
                   f.finish_money = f.finish_money - l_finishmoney
             where f.hf_orderid = l_orderid
               and f.hf_serialid = l_hfserialid
               and f.hf_status = 0;
            --�޸Ķ���״̬ ���ܼ��޸Ķ���������أ�
            update OD_ORDERINFO o
               set o.charge_status      = 4,
                   o.charge_finishtime  = sysdate,
                   o.req_errorcode = v_errorcode,
                   o.charge_finishmoney = o.charge_finishmoney -
                                          l_finishmoney
             where o.req_orderid = l_orderid
               and o.charge_status = 0;
          end if;

          if v_hfcancelstatus = 4 and l_currentstatus != 1  then --��ѯδ֪,�����޸�ԭ�������ȷ��
             null;
          else
            update od_cancelordercharge
               set hf_cancelstatus = v_hfcancelstatus,
                   req_sarttime    = sysdate,
                   req_errorcode   = decode(v_hfcancelstatus,0,null,v_errorcode)
             where hf_serialid = v_hfserialid;

          end if;

        else
          --��ǰΪ�ɹ���
          if v_hfcancelstatus != 0 then
            -- �򱨾�������ӱ�����¼
            l_warninfo := '�����������:' || v_hfserialid || ',��ǰ״̬:' ||
                          l_currentstatus || 'Ԫ,����״̬' || v_hfcancelstatus ||
                          ',�ɳ����ɹ���ʧ�ܴ治ƥ��!'||',v_errorcode:'||v_errorcode;
            sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');

          end if; --��ǰ�ɹ�����ѯҲ�ɹ��������ı�
          --���磺��ѯʱ�������δ֪�ģ�����ѯ�󷵻ؽ���ɹ��ˣ����Ƕ�����״̬�Ѿ��ǳɹ��ˣ���ô��ô����


        end if;
        v_result := '0000';
      else
        --����
        l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid ||
                      '�������ؼǹ�����,���ݳ�����ˮ��ѯ��¼����,״̬:' ||v_hfcancelstatus||',v_errorcode:'||v_errorcode||',count:'|| l_count;
        sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
        v_result := '1111';
      end if;

    elsif v_opttype = 2 then
      --�޸�֪ͨ���״̬
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
      -- ��ѯ������Ϣ�Ƿ����
      select count(1)
        into l_count
        from od_cancelordercharge t
       where t.hf_serialid = v_hfserialid
         and t.hf_orderid = v_orderid
         and rownum = 1;

      if l_count <= 0 then
        -- û�м�¼,��Ӽ�¼
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
        -- �Ѿ��м�¼,���¼�¼
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
create or replace procedure hf.SP_OD_CREATEORDERINFO(v_online_id    in NUMBER, -- ����ID
                                                  v_mobilenum    in VARCHAR2, -- �ֻ�����
                                                  v_chargeamount in NUMBER, -- ��ֵ���
                                                  v_paymount     in NUMBER, -- �������
                                                  v_orderid      in VARCHAR2, -- ������
                                                  v_ordersource  in VARCHAR2, -- ������Դ
                                                  v_agentid      in VARCHAR2, -- ������ID(�û�ID)
                                                  v_ordertime    in VARCHAR2, -- ����ƽ̨����ʱ��
                                                  v_mark         in VARCHAR2, -- �����ֶ�
                                                  v_receivetime  in VARCHAR2, --�յ�ƽ̨����ʱ���
                                                  v_reqorderid   out VARCHAR2, -- ��ֵ������Ϣ������
                                                  v_result       out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ���ѳ�ֵ������Ϣ
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-4-15
  ������ݣ����뻰�ѳ�ֵ������Ϣ �������� SP_OD_PREPAREORDERINFO
            v2 2011.06.23 ��SPΪֱ���µ�,����Ԥ�µ�,���Ǳ�����֤��Ϣ,�µ��ɹ�״̬Ϊ:2
            v3 2011.6.24 ���ͬһ�ֻ��ţ�ͬһ��ֵ���1�����ڲ����ظ��ύ
  ******************************************************************************/

 AS
  l_isp_id            NUMBER; -- ��Ӫ�̱�ʶ
  l_province_id       NUMBER; -- ����ʡ��ID
  l_city_code         VARCHAR2(4); -- ������������
  l_product_id        NUMBER; -- ��ƷID
  l_product_splitflag NUMBER; -- ��Ʒ�ִα�ʶ
  l_product_type      NUMBER; -- ��Ʒ����(ҵ������)
  l_order_id          VARCHAR2(30); -- ���ѳ�ֵ������Ϣ�еĶ�����(����)
  l_seq_result        VARCHAR2(30); -- �����ŷ�����
  l_po_exist          VARCHAR2(4); -- ��ʱ����,�����յ���Ϣ��ʹ��
  l_count1            NUMBER; -- ���ܴ���
  l_count2            NUMBER; -- ���ܿ���
  l_count3            NUMBER; -- ������Դ
  l_product_waittime  NUMBER(10); -- ��ʼ�ȴ�ʱ���������������ȴ��೤ʱ�俪ʼ������λΪ��
  l_product_delaytime NUMBER(10); -- ������ʱ������λΪ����
  l_content           NUMBER(10, 2); --��Ʒ����
  l_minmoney          NUMBER(10, 2); --��Ʒ��С���
  l_maxmoney          NUMBER(10, 2); --��Ʒ�����
  l_chk_money         VARCHAR2(4); -- ��ʱ������ֵ���
  l_moblie_type       NUMBER; -- �ж��Ƿ��ǹ̻���
  l_moblie_trycount   number; --�Ƿ�1�������ظ���ֵ
  l_begin_sysdate     date; -- ����ϵͳʱ���ʱ��,�����ж�ִ�в���ʱ���Ƿ�ʱ
  l_insert_count      number;--��������漰����������
  l_phone_valid       VARCHAR2(4); -- �ֻ�������֤���
  l_v_product_type    number(1);--��ʱ�洢���ܲ�Ʒ����
  l_p_product_type      NUMBER(1);--���ܵĲ�Ʒ����
  l_p_product_ispid      number(1); --������Ӫ��
	l_p_product_provinceid number(2); --����ʡ��
	l_p_product_citycode   varchar2(10);--���ܳ���

begin
  v_result     := '1111';
  v_reqorderid := '';
  l_count1     := 0;
  l_count2     := 0;
  l_po_exist   := '';
  l_chk_money  := '';
  l_phone_valid := '0000';--��ʼ�ֻ�����ͨ������ֹ�Է��ֻ���ֵ����
  --����ʱ�䣬��ֹ�µ������ʱ�����
  if v_receivetime is null then 
     l_begin_sysdate := sysdate;
  else 
     l_begin_sysdate := to_date(v_receivetime,'yyyy-mm-dd hh24:mi:ss');
  end if;

  -- ��ѯ������Դ�Ƿ���ڻ����: ���� 1030
  select count(1)
    into l_count3
    from ch_ordersourceinfo t
   where t.os_id = v_ordersource
     and t.os_status = '0'
     and rownum = 1;

  -- ��ѯ���ܴ��ڻ����״̬: �����ڷ��� 1040 �����÷���1050
  select count(1), nvl(sum(decode(t.product_status, 0, 1, null, 0, 0)), 0)
    into l_count1, l_count2
    from po_productonlineinfo t
   where t.online_id = v_online_id
     and t.order_source = v_ordersource
     and rownum = 1;

  -- ��ѯ��ֵ����Ƿ�����ܡ���Ʒ���ƥ��
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
      -- �����ݵ��쳣
      l_chk_money := '0010';
  end;

  -- �Ƿ��ǹ̻�����ʱ��֧��
  select instr(v_mobilenum, '-') into l_moblie_type from dual;

  if l_moblie_type is not null and l_moblie_type > 0 then
    l_v_product_type := 1;--�������ֻ���ֵ
  else
      l_v_product_type := 0;--���ֻ���ֵ
      
      IF (v_mobilenum  LIKE '%13800138000%') or (substr(v_mobilenum,1,1) = '0') THEN --���ֻ���ֵ����
          l_phone_valid := '0022';   --�ú��벻֧�ֳ�ֵ
      ELSIF length(v_mobilenum) <> 11 THEN 
          l_v_product_type := 1 ;--���ֻ���ֵ
          l_phone_valid := '0023';   --�ֻ���������
      ELSE 
          -- ��ѯ������Ϣ��: ��ѯ������Ϣ�� �����ڷ��� 1060
          begin
            SELECT isp_id, province_id, city_code
              into l_isp_id, l_province_id, l_city_code
              FROM sys_accsegment
             WHERE acc_segment = substr(v_mobilenum, 0, 7)
               and rownum = 1;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              -- �����ݵ��쳣
              l_po_exist := '0006';
          end;
      END IF;
  end if;

  -- �Ƿ�ͬһ���롢ͬһ��ֵ1�����ظ���ֵ
  select count(1)
    into l_moblie_trycount
    from od_orderinfo t
   where t.req_accnum = v_mobilenum
     and t.req_money = v_chargeamount
     and t.req_datetime >= sysdate - 1 / 1440
     and t.req_datetime <= sysdate;

  -- �����յ���¼
  -- �յ�ʱ�� ����ID ��ֵ�ֻ��� ��Ӫ�̱�ʶ ҵ������ ������ ������Դ �û�ID �Ƿ�֧�ִַ� ��Ʒ���
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
      -- Ψһ������ͻ
      v_result := '0007';
      ROLLBACK;
      return;
  end;

  -- �������ж�
  if l_moblie_trycount > 0 then
    -- �Ƿ��ظ���ֵ
    v_result := '0019';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_count3 = 0 then
    -- ������Դ������
    v_result := '0003';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_v_product_type != 0 then
      -- ��ʱ��֧�ֹ̻�
      v_result := '0012';
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  elsif l_phone_valid <> '0000'  then 
     --�ֻ�������֤����
      v_result := l_phone_valid;
      update od_recvwebbill t
         set t.cre_errorcode = v_result 
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  end if;

  if l_p_product_type <> l_v_product_type  then 
      --��ֵ��������ܲ�Ʒ���Ͳ�һ��
      v_result := '0017';
      update od_recvwebbill t
         set t.cre_errorcode = v_result 
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  end if;

  if l_po_exist = '0006' then
      -- ������Ϣ������
      v_result := l_po_exist;
      update od_recvwebbill t
         set t.cre_errorcode = v_result 
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
    
  else 
       if l_p_product_ispid <> l_isp_id then 
          -- ���������������Ӫ�̲�һ��
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
            -- �ֻ���ʡ�������ʡ�ݲ�һ��
            v_result := '0020';
            update od_recvwebbill t
               set t.cre_errorcode = v_result 
             where t.req_orderid = v_orderid
               and t.order_source = v_ordersource;
            commit;
            RETURN;
            
          elsif to_number(l_p_product_citycode) != 0 and l_p_product_citycode <> l_city_code then          
            -- �ֻ��ų�������ܳ��в�һ��
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
    -- ���ܲ�����
    v_result := '0004';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  elsif l_count2 = 0 then
    -- ���ܴ���,������
    v_result := '0005';
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  if l_chk_money <> '0000' then
    --��ֵ�������ܡ���Ʒ����
    v_result := l_chk_money;
    update od_recvwebbill t
       set t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  -- ��Ʒ���ֻ��Ų�ƥ��: ��ѯ������Ʒ������Ϣ ��ƥ�䷵�� 1020
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
      -- �����ݵ��쳣,��ƥ��
      v_result := '0002';
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      return;
  end;

  -- ���붩����ֵ��¼
  --     �ж϶����ŵ�ΨһԼ��,�쳣���� ���� 1011
  --     ����ɹ�����쳣 ���� 1000
  begin
    -- ���ɶ�����
    sp_hf_generateorderid(l_seq_result, l_order_id);
    if l_seq_result = '1111' then
      -- ���������ɴ���
      v_result := '1111';
      update od_recvwebbill t
         set t.cre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
    end if;

    -- �����¼
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
           charge_begintime, -- Ҫ��ʼ����ʱ��
           charge_linetime,  -- Ҫ�����ʱ��
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
           2, --�µ��ɹ�Ϊ2,�ȴ�����
           sysdate,
           to_date(v_ordertime, 'YYYY-MM-DD HH24:MI:SS'),
           sysdate + l_product_waittime / (24 * 60 * 60), -- ����
           sysdate + l_product_waittime / (24 * 60 * 60) +
           (l_product_delaytime) / (24 * 60), -- �ӷ���
           l_product_splitflag,
           v_paymount,
           v_mark,
           l_product_type
   from od_recvwebbill r
   where r.req_orderid = v_orderid
     and r.order_source = v_ordersource
     and sysdate < (l_begin_sysdate + 1 / 1440);

   l_insert_count := sql%rowcount;--�Ƿ����ɹ�

   if l_insert_count = 1  then
          v_result     := '0000';
          v_reqorderid := l_order_id;
   else
          v_result     := '0016';
   end if;
    --  �����յ����еĶ�����
    update od_recvwebbill t
       set t.hf_orderid = v_reqorderid,
           t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;

    commit;

  EXCEPTION
    WHEN dup_val_on_index THEN
      -- Ψһ��Լ��
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
CREATE OR REPLACE PROCEDURE HF.SP_OD_MOBILERECHARGE_BEGIN(v_Result       OUT VARCHAR2, --�����
                                                       v_ProcessCount OUT VARCHAR2, --�������Ķ�����
                                                       v_hforderid    OUT VARCHAR2, --�����ţ��Զ��Ÿ���
                                                       v_hfserialid   OUT VARCHAR2, --������ˮ�ţ��Զ��Ÿ���
                                                       v_ispid        OUT VARCHAR2, --��Ӫ�̱�ʶ���Զ��Ÿ���
                                                       v_provinceid   OUT VARCHAR2, --ʡ��id���Զ��Ÿ���
                                                       v_citycode     OUT VARCHAR2, --���б��룬�Զ��Ÿ���
                                                       v_accnum       OUT VARCHAR2, --��ֵ�ʺţ��Զ��Ÿ���
                                                       v_chargemoney  OUT VARCHAR2, --��ֵ���Զ��Ÿ���
                                                       v_channelid    OUT VARCHAR2, --��ֵ����id���Զ��Ÿ���
                                                       v_stationtype  OUT VARCHAR2, --վ�����ͣ��Զ��Ÿ���
                                                       v_channeltype  OUT VARCHAR2, --��ֵ�������ͣ��Զ��Ÿ���
                                                       v_servicetype  OUT VARCHAR2, --��ֵҵ�����ͣ��Զ��Ÿ���
                                                       v_ordersource  OUT VARCHAR2, --������Դ
                                                       v_useid        OUT VARCHAR2 --������id
                                                       )
/************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1.0
  ģ�����ƣ����ݿ�ű�-��ֵ����,�������������ɻ�����ˮ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-05-05
  �޸����ݣ�
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
  l_province    NUMBER; --������ʡ���ֻ�����ʡ�����������������ֻ�����ʡ�����������ʡ
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
             where a.charge_linetime >= sysdate --û��������ʱ��
               and a.charge_begintime <= sysdate --���仹δ����ʼ����ʱ��Ĳ��������д���
               and a.charge_status = 2 --�ȴ���ֵ
               and a.req_trycount <= 8 --����ֵ��������8��ʱ�������ٳ�ֵ
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

  --1��ȡ���䶩��
  -----------------------------------------------------------------------------
  OPEN cur_charge;
  LOOP
    <<loopstart>>
    FETCH cur_charge
      INTO l_hforderid, l_onlineid, l_provinceid, l_citycode, l_accnum, l_trycount, l_ispid, l_linedate, l_servicetype, l_ordersource, l_useid;
    EXIT WHEN cur_charge%NOTFOUND;
  
    BEGIN
      --ȡһ������
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
  
    --״̬��������������ˣ��˴��Ͳ�������
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
  
    --����Ҫ�����ʱ�䣬����״̬��Ϊʧ��Ϊ�˿�
    IF l_linedate < l_nowtime THEN
      UPDATE od_orderinfo
         SET charge_status = 4
       WHERE req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --��Ҫ��ֵ���Ϊ0���쳣����
    IF l_reqmoney = 0 THEN
      UPDATE od_orderinfo
         SET charge_status = 8
       WHERE req_orderid = l_hforderid;
      COMMIT;
      sp_sys_dberrorinfo_create(8,
                                'SP_OD_MOBILERECHARGE_BEGIN',
                                '���ѳ�ֵ�����쳣����',
                                l_hforderid,
                                '����״̬��' || to_char(l_status),
                                '��Ҫ��ֵ��' || to_char(l_reqmoney));
      GOTO loopstart;
    END IF;
  
    --����Ҫ�����ʱ�䣬����״̬��Ϊʧ��δ�˿�
    IF l_linedate < l_nowtime THEN
      UPDATE od_orderinfo
         SET charge_status = 4
       WHERE req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --ȡ��ֵ����
    sp_hf_getchannel(l_hforderid,
                     v_result,
                     l_channelid,
                     l_stationtype,
                     l_chargemoney);
    IF v_result <> 0 THEN
      --û���ҵ�����
      UPDATE od_orderinfo p
         SET p.charge_status     = 8,
             p.req_errorcode     = '1007',
             p.charge_finishtime = SYSDATE
       WHERE p.req_orderid = l_hforderid;
      COMMIT;
      GOTO loopstart;
    END IF;
  
    --��ʼ������Դ
    IF l_channeltype = 2 THEN
      --����
      l_cardsource := 4;
      l_province   := l_provinceid;
    ELSIF l_channeltype IN (0, 1, 3, 4, 6) THEN
      --���
      l_cardsource := -1;
      l_province   := -1;
    ELSE
      --��������ֵ
      l_cardsource := 3;
      l_province   := l_provinceid;
    END IF;
  
    --���¶���״̬Ϊ�ȴ�����,���Ӷ������ֵ����
    UPDATE od_orderinfo
       SET charge_status = 3,
           req_trycount  = decode(req_trycount, null, 0, req_trycount) + 1
     WHERE req_orderid = l_hforderid;
  
    --����fullnote��
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
    

  
    --ȡ��һ������������1
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
      --ȡ��������ȡ��10���˳�
      EXIT;
    END IF;
  
  END LOOP;
  CLOSE cur_charge;

  --3��ȡ�������
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
CREATE OR REPLACE PROCEDURE HF.SP_OD_NEEDNOTICEORDER_BEGIN(v_result                    out varchar2, --�����
                                                          v_processcount              out varchar2, --�������Ķ�����
                                                          v_orderid                   out varchar2, --������
                                                          v_saleserialid              out varchar2, --ǰ̨֧��������
                                                          v_chargestatus              out varchar2, --����״̬
                                                          v_chargefinishmoney         out varchar2, --ʵ����ɽ��
                                                          v_osbackurl                 out varchar2, --֪ͨ��ַURL
                                                          v_oskey                     out varchar2  -- ֪ͨ��Կ
                                                          ) --��ȡ��¼����
 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-��ȡ��Ҫ֪ͨ������ƽ̨�Ķ�����Ϣ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-5-23
  �޸����ݣ�
  ����������a) ϵͳ��״̬Ϊ����ֵ�ɹ�/��ֵʧ�ܡ�֪ͨ״̬Ϊ��δ֪ͨ��֪ͨ����û�г�����ֵ�Ķ������ص���֪ͨ����
            v2 weisd 2011.08.02  �޸Ĳ�ѯ��䣬����ƴ������ַ���,�����е�ַURL��ƴ�����ö��Ÿ�����ע��
            v3 weisd         03  ���ڶ���ʧ�ܣ��Ͳ����ٴγ�ֵ�������˹��޸ģ�������ֱ��֪ͨ��ɾ��������ʱ��֪ͨ
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
    select req_orderid, --������
           req_saleserialid, --ǰ̨֧��������
           charge_status, --����״̬
           charge_finishmoney, --ʵ����ɽ��
           os_backurl,
           os_key,
           os_maxcnt
      from (select t.req_orderid, --������
                   t.req_saleserialid, --ǰ̨֧��������
                   t.charge_status, --����״̬
                   t.req_money, --�������
                   t.charge_finishmoney, --ʵ����ɽ��
                   t.req_paymoney, --ʵ��֧�����
                   t.product_onlineid, --����id
                   t.req_productid, --��Ʒid
                   t.req_provinceid, --ʡ��id
                   t.req_citycode, --����id
                   t.req_accnum, --��ֵ����
                   t.req_trycount, --��ֵ����
                   t.req_ispid, --��Ӫ��id
                   t.req_servicetype, --ҵ������
                   t.req_ordersource, --������Դ
                   t.req_userid, --�û�id������id
                   o.os_backurl, --��̨֪ͨ��ַ
                   o.os_key, --��֤��Կ
                   o.os_maxcnt
              from od_orderinfo t, CH_ORDERSOURCEINFO o
             where t.req_ordersource = o.os_id
               and t.charge_status in (0, 4, 5, 6)
               and (t.ec_notifyflag = 1 or --δ֪ͨ
                   (t.ec_notifyflag = 3 and --֪ͨʧ��
                   (t.ec_notifycount + 1) <= o.os_maxcnt and --С�ڷ�ֵ
                   (t.ec_notifytime + (1 / 1440)) <= sysdate)) --�����ϴ�1����
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
      --ȡһ������
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
      null; --����������
   else
    --����֪ͨ״̬��
      ROLLBACK;
      GOTO loopstart;
   end if;

   if (l_ec_notifyflag = 1 or (l_ec_notifyflag = 3 and (l_ec_notifycount + 1) <= l_os_maxcnt and (l_ec_notifytime + (1 / 1440)) <= sysdate))  then
      null ;-- ��������ִ�У�����֪ͨҪ��
   else
    --����֪ͨ״̬��
      ROLLBACK;
      GOTO loopstart;
   end if;

    -- ������˵����Ҫ֪ͨ
    --����+1
    update od_orderinfo o
       set o.ec_notifyflag = 2 ,
           o.ec_notifytime = sysdate ,
           o.ec_notifycount = o.ec_notifycount + 1
     where o.req_orderid = l_hforderid ;

    --ȡ��һ������������1
    l_processcount       := l_processcount + 1;
    v_orderid            := v_orderid || l_hforderid || ',';
    v_saleserialid       := v_saleserialid || l_saleserialid || ',';
    v_chargestatus       := v_chargestatus || l_chargestatus || ',';
    v_chargefinishmoney  := v_chargefinishmoney || l_chargefinishmoney || ',';
    v_osbackurl          := v_osbackurl || l_osbackurl || ',';
    v_oskey              := v_oskey || l_oskey || ',';

    COMMIT;

    IF l_processcount >= 9 THEN
      --ȡ��������ȡ��10���˳�
      EXIT;
    END IF;

  END LOOP;
  CLOSE cur_resultcur;

  --3����ֵ��¼ȡ�����
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
CREATE OR REPLACE PROCEDURE HF.SP_OD_NEEDNOTICEREVERSE_BEGIN(v_pageNo    number, --��ѯҳ��
                                                          v_pageSize  number, --ÿҳ��ʾ��С
                                                          v_status    in VARCHAR2, --Ҫ��ȡ��״̬
                                                          v_resultCur out HF_CURSOR.CURSOR_TYPE) --��ȡ��¼����
 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-��ȡ��Ҫ֪ͨ������ƽ̨�ĳ�����Ϣ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-6-8
  �޸����ݣ�
  ����������a) ϵͳ��״̬Ϊ�������ɹ�������ʧ�ܡ�֪ͨ״̬Ϊ��δ֪ͨ��֪ͨ����û�г�����ֵ�ļ��ص���֪ͨ����
             ��java�д����صĳ������
  ************************************************************/
  l_pageNo   number(10);
  l_pageSize number(10);
BEGIN

  --v_status  ��ʱ����
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
      from (select t.req_orderid, --������
                   t.req_saleserialid, --ǰ̨֧��������
                   t.charge_status, --����״̬
                   t.charge_finishmoney, --ʵ����ɽ��
                   t.req_userid, --�û�id������id
                   o.os_backurl, --��̨֪ͨ��ַ
                   o.os_key, --��֤��Կ
                   c.hf_serialid, --���ѳ�ֵ��¼��ˮ
                   c.hf_cancelstatus, --�������״̬
                   c.finish_money, --�������
                   c.req_trycount, --��������
                   c.req_errorcode --����������
              from OD_CANCELORDERCHARGE c,
                   od_orderinfo         t,
                   CH_ORDERSOURCEINFO   o
             where c.hf_orderid = t.req_orderid
               and t.req_ordersource = o.os_id
               and c.hf_cancelstatus in (0, 3) -- ��ֵ�ɹ���ʧ��(2��״̬)
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
create or replace procedure hf.SP_OD_PREPAREORDERINFO(v_online_id    in NUMBER, -- ����ID
                                                  v_mobilenum    in VARCHAR2, -- �ֻ�����
                                                  v_chargeamount in NUMBER, -- ��ֵ���
                                                  v_paymount     in NUMBER, -- �������
                                                  v_orderid      in VARCHAR2, -- ������
                                                  v_ordersource  in VARCHAR2, -- ������Դ
                                                  v_agentid      in VARCHAR2, -- ������ID(�û�ID)
                                                  v_ordertime    in VARCHAR2, -- ����ƽ̨����ʱ��
                                                  v_mark         in VARCHAR2, -- �����ֶ�
                                                  v_receivetime  in VARCHAR2, --�յ�ƽ̨����ʱ���
                                                  v_reqorderid   out VARCHAR2, -- ��ֵ������Ϣ������
                                                  v_result       out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ���ѳ�ֵ     ����Ԥ�µ�
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��weisd
  ������ڣ�2011-6-23
  ������ݣ����뻰�ѳ�ֵ����Ԥ�µ� �������ƣ�SP_OD_CREATEORDERINFO
            ���ش������, ����״̬Ϊ:1
            Ȼ���ٸ���ǰ̨�ķ�������SP_OD_SENDORDERINFO
            v3 2011.6.24 ���ͬһ�ֻ��ţ�ͬһ��ֵ���1�����ڲ����ظ��ύ
            v4 2011.7.7  ���һ������ʱ��receivetime
                         ��Ӳ�����֤��Ϣ,������Ե��û�δ��֤�������Ӧ��ֵ�����Ƿ�����
  ******************************************************************************/

 AS
  l_isp_id            NUMBER; -- ��Ӫ�̱�ʶ
  l_province_id       NUMBER; -- ����ʡ��ID
  l_city_code         VARCHAR2(4); -- ������������
  l_product_id        NUMBER; -- ��ƷID
  l_product_splitflag NUMBER; -- ��Ʒ�ִα�ʶ
  l_product_type      NUMBER; -- ��Ʒ����(ҵ������)
  l_order_id          VARCHAR2(30); -- ���ѳ�ֵ������Ϣ�еĶ�����(����)
  l_seq_result        VARCHAR2(30); -- �����ŷ�����
  l_po_exist          VARCHAR2(4); -- ��ʱ����,�����յ���Ϣ��ʹ��
  l_count1            NUMBER; -- ���ܴ���
  l_count2            NUMBER; -- ���ܿ���
  l_count3            NUMBER; -- ������Դ
  l_product_waittime  NUMBER(10); -- ��ʼ�ȴ�ʱ���������������ȴ��೤ʱ�俪ʼ������λΪ��
  l_product_delaytime NUMBER(10); -- ������ʱ������λΪ����
  l_content           NUMBER(10, 2); --��Ʒ����
  l_minmoney          NUMBER(10, 2); --��Ʒ��С���
  l_maxmoney          NUMBER(10, 2); --��Ʒ�����
  l_chk_money         VARCHAR2(4); -- ��ʱ������ֵ���
  l_moblie_type       NUMBER; -- �ж��Ƿ��ǹ̻�
  l_moblie_trycount   number; --�Ƿ�1�������ظ���ֵ
  l_begin_sysdate     date; -- ����ϵͳʱ���ʱ��,�����ж�ִ�в���ʱ���Ƿ�ʱ
  l_insert_count      number;--��������漰����������
  l_phone_valid       VARCHAR2(4); -- �ֻ�������֤���
  l_v_product_type    number(1);--��ʱ�洢���ܲ�Ʒ����
  l_p_product_type      NUMBER(1);--���ܵĲ�Ʒ����
  l_p_product_ispid      number(1); --������Ӫ��
	l_p_product_provinceid number(2); --����ʡ��
	l_p_product_citycode   varchar2(10);--���ܳ���

begin
  v_result     := '1111';
  v_reqorderid := '';
  l_count1     := 0;
  l_count2     := 0;
  l_po_exist   := '';
  l_chk_money  := '';
  l_phone_valid := '0000';--��ʼ�ֻ�����ͨ������ֹ�Է��ֻ���ֵ����
  --����ʱ�䣬��ֹ�µ������ʱ�����
  if v_receivetime is null then 
     l_begin_sysdate := sysdate;
  else 
     l_begin_sysdate := to_date(v_receivetime,'yyyy-mm-dd hh24:mi:ss');
  end if;

  -- ��ѯ������Դ�Ƿ���ڻ����: ���� 1030
  select count(1)
    into l_count3
    from ch_ordersourceinfo t
   where t.os_id = v_ordersource
     and t.os_status = '0'
     and rownum = 1;

  -- ��ѯ���ܴ��ڻ����״̬: �����ڷ��� 1040 �����÷���1050
  select count(1), nvl(sum(decode(t.product_status, 0, 1, null, 0, 0)), 0)
    into l_count1, l_count2
    from po_productonlineinfo t
   where t.online_id = v_online_id
     and t.order_source = v_ordersource
     and rownum = 1;

  -- ��ѯ��ֵ����Ƿ�����ܡ���Ʒ���ƥ��
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
    -- �����ݵ��쳣
     l_chk_money := '0010';
  end;

  -- �Ƿ��ǹ̻�����ʱ��֧��
  select instr(v_mobilenum,'-') into l_moblie_type from dual;

  if l_moblie_type is not null and l_moblie_type > 0 then
      l_v_product_type := 1;--�������ֻ���ֵ
  else
      l_v_product_type := 0;--���ֻ���ֵ
      
      IF (v_mobilenum  LIKE '%13800138000%') or (substr(v_mobilenum,1,1) = '0') THEN --���ֻ���ֵ����
          l_phone_valid := '0022';   --�ú��벻֧�ֳ�ֵ
      ELSIF length(v_mobilenum) <> 11 THEN 
          l_v_product_type := 1 ;--���ֻ���ֵ
          l_phone_valid := '0023';   --�ֻ���������
      ELSE 
          -- ��ѯ������Ϣ��: ��ѯ������Ϣ�� �����ڷ��� 1060
          begin
            SELECT isp_id, province_id, city_code
              into l_isp_id, l_province_id, l_city_code
              FROM sys_accsegment
             WHERE acc_segment = substr(v_mobilenum, 0, 7)
               and rownum = 1;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              -- �����ݵ��쳣
              l_po_exist := '0006';
          end;
      END IF;

  end if;

  -- �Ƿ�ͬһ���롢ͬһ��ֵ1�����ظ���ֵ
  select count(1)
    into l_moblie_trycount
    from od_orderinfo t
   where t.req_accnum = v_mobilenum
     and t.req_money = v_chargeamount
     and t.req_datetime >= sysdate - 1 / 1440
     and t.req_datetime <= sysdate;


  -- �����յ���¼
  -- �յ�ʱ�� ����ID ��ֵ�ֻ��� ��Ӫ�̱�ʶ ҵ������ ������ ������Դ �û�ID �Ƿ�֧�ִַ� ��Ʒ���
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
      -- Ψһ������ͻ
      v_result := '0007';
      ROLLBACK;
      return;
  end;

  -- �������ж�
  if l_moblie_trycount > 0 then
    -- �Ƿ��ظ���ֵ
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
    -- ������Դ������
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
      -- ��ʱ��֧�ֹ̻�
      v_result := '0012';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
  elsif l_phone_valid <> '0000'  then 
     --�ֻ�������֤����
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
      --��ֵ��������ܲ�Ʒ���Ͳ�һ��
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
    -- ������Ϣ������
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
        -- ���������������Ӫ�̲�һ��
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
          -- �ֻ���ʡ�������ʡ�ݲ�һ��
          v_result := '0020';
          update od_recvwebbill t
             set t.cre_errorcode = v_result,
                 t.pre_errorcode = v_result
           where t.req_orderid = v_orderid
             and t.order_source = v_ordersource;
          commit;
          RETURN;
          
        elsif to_number(l_p_product_citycode) != 0 and l_p_product_citycode <> l_city_code then          
          -- �ֻ��ų�������ܳ��в�һ��
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
    -- ���ܲ�����
    v_result := '0004';
    update od_recvwebbill t
       set t.cre_errorcode = v_result ,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  elsif l_count2 = 0 then
    -- ���ܴ���,������
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
    --��ֵ�������ܡ���Ʒ����
    v_result := l_chk_money;
    update od_recvwebbill t
       set t.cre_errorcode = v_result,
           t.pre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;
    commit;
    RETURN;
  end if;

  -- ��Ʒ���ֻ��Ų�ƥ��: ��ѯ������Ʒ������Ϣ ��ƥ�䷵�� 1020
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
      -- �����ݵ��쳣,��ƥ��
      v_result := '0002';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      return;
  end;

  -- ���붩����ֵ��¼
  --     �ж϶����ŵ�ΨһԼ��,�쳣���� ���� 1011
  --     ����ɹ�����쳣 ���� 1000
  begin
    -- ���ɶ�����
    sp_hf_generateorderid(l_seq_result, l_order_id);
    if l_seq_result = '1111' then
      -- ���������ɴ���
      v_result := '1111';
      update od_recvwebbill t
         set t.cre_errorcode = v_result,
             t.pre_errorcode = v_result
       where t.req_orderid = v_orderid
         and t.order_source = v_ordersource;
      commit;
      RETURN;
    end if;

    -- �����¼
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
       charge_begintime, -- Ҫ��ʼ����ʱ��
       charge_linetime,
       �� -- Ҫ�����ʱ��
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
       1, --Ԥ�µ�
       sysdate,
       to_date(v_ordertime, 'YYYY-MM-DD HH24:MI:SS'),
       sysdate + l_product_waittime / (24 * 60 * 60), -- ����
       sysdate + l_product_waittime / (24 * 60 * 60) +
       (l_product_delaytime) / (24 * 60), -- �ӷ���
       l_product_splitflag,
       v_paymount,
       v_mark,
       l_product_type
   from od_recvwebbill r
   where r.req_orderid = v_orderid
     and r.order_source = v_ordersource
     and sysdate < (l_begin_sysdate + 1 / 1440);

   l_insert_count := sql%rowcount;--�Ƿ����ɹ�

   if l_insert_count = 1  then
          v_result     := '0000';
          v_reqorderid := l_order_id;
   else
          v_result     := '0016';
   end if;

    --  �����յ����еĶ�����
    update od_recvwebbill t
       set t.hf_orderid = v_reqorderid,
           t.cre_errorcode = v_result
     where t.req_orderid = v_orderid
       and t.order_source = v_ordersource;

    commit;

  EXCEPTION
    WHEN dup_val_on_index THEN
      -- Ψһ��Լ��
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
create or replace procedure hf.SP_OD_RECHARGEFAILURE(v_hfserialid      in VARCHAR2, -- ������ˮ��
                                                  v_channelserialid in VARCHAR2, -- ������ˮ��
                                                  v_sendserialid    in VARCHAR2, -- ���͸��Է���ˮ��
                                                  v_dealtime        in VARCHAR2, -- ����ʱ��
                                                  v_errorcode       in VARCHAR2, -- ������
                                                  v_result          out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �������ػ��ѳ�ֵʧ�ܴ���
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-5-5
  ������ݣ�(1)  �޸Ļ��ѳ�ֵ��¼��״̬Ϊ��ʧ�ܡ�����¼���ʱ�䣬������ȣ�
            (2)  �ж϶����Ƿ���Լ�����ֵ��
            (3)  ������Ժ�ƥ�������Ŀɳ�ֵ�����������µĻ��ѳ�ֵ��¼�����ж��γ�ֵ��
            (4)  ��������ԣ��޸Ļ��Ѷ������յ����е�״̬�������Ѽ������Ϣ��
            (5)  �޸�����ʧ�ܼ������������Ϣ
  
            v2 weisd 5.26 ����ֶ� sendserialid ���͸��Է�����ˮ
            v3 weisd 6.3 ��ֵʧ�� ��������ٳ�ֵ ���޸�״̬ʹ֮�ܼ�����ֵ
            v4 weisd 6.17 ���һ�ʳ�ֵʧ�ܻ���������8�Σ����ж��Ƿ����Ѿ�ʧ�ܵģ���ʱ�䲻��10�����ڵ�
  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- ������ˮ�Ŵ���
  l_hf_status          NUMBER(2); -- ���ѳ�ֵ״̬
  l_hf_orderid         VARCHAR2(30); -- ���Ѷ�����
  l_hf_acchannel       NUMBER(10); -- ����ID
  l_time               NUMBER(10); -- �Ƿ���Լ�����ֵ��ʱ���,�������
  l_req_money          NUMBER(10, 2); -- �������
  l_charge_finishmoney NUMBER(10, 2); -- ʵ�ʵ��˽��
  l_warninfo           VARCHAR2(200); -- ������Ϣ
  l_ch_failcount       NUMBER; -- ����ʧ�ܼ���
  l_req_trycount       NUMBER; -- ������ֵ����
  l_before_failtime    date; --�ϴ�ʧ��ʱ��
  l_product_delaytime  NUMBER(10); --���ܵ���ʱ��
  l_charge_linetime    date; --���Ƶ���ʱ��

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  -- ��ѯ������ˮ���ڻ��ѳ�ֵ���ױ����Ƿ���ڻ�ɴ���
  -- ��ǰ����״̬ 0���ɹ� 1���ȴ� 2�����ڴ��� 3��ʧ�� 4����ʱ  9�����δ֪�����˹����
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- ������ˮ��Ϊ������ʱ�Ĵ���
  if l_od_fullnot_count = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '�������س�ֵʧ��,�����ѳ�ֵ��¼�л�����ˮ��' || v_hfserialid || '������';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬״̬Ϊ�ɹ�ʱ�Ĵ���
  if l_hf_status = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '���ڳɹ�״̬�����������س�ֵʧ����Ϣ!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬״̬Ϊ���ֳɹ�ʱ�Ĵ���
  if l_hf_status = 6 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid ||
                  '���ڲ��ֳɹ�״̬�����������س�ֵʧ����Ϣ!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬״̬Ϊʧ��ʱ�Ĵ���
  if l_hf_status = 3 then
    v_result := '0000';
    RETURN;
  end if;

  begin
    -- ������ˮ��״̬Ϊ�ȴ�ʱ����
    if l_hf_status = 1 then
      -- �򱨾�������ӱ�����¼
      l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid ||
                    '���ڵȴ�״̬�����������س�ֵʧ����Ϣ!';
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    end if;
  
    -- ���»��ѳ�ֵ��¼��
    update od_fullnote t
       set t.hf_status        = 3,
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.hf_errorcode     = v_errorcode,
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid
    -- t.trycount         = decode(t.trycount, null, 0, t.trycount) + 1 �����������
     where t.hf_serialid = v_hfserialid;
  
    -- ��ѯ�����������Ϣ
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
  
    -- �ж϶������Ƿ����
    if l_hf_orderid is not null then
      -- �ж϶����Ƿ���Լ�����ֵ,����8��,���Ӵ�����ȡ�����Ǳ�
      if l_time >= 0 or l_req_trycount >= 8 then
        -- ���ܼ�����ֵ,�޸Ķ���״̬: 4 ʧ��δ�˿�״̬
        update od_orderinfo t
           set t.charge_status     = 4,
               t.req_errorcode     = v_errorcode,
               t.charge_finishtime = sysdate
         where t.req_orderid = l_hf_orderid;
        -- �����յ���Ϣ�ķ���������
        update od_recvwebbill t
           set t.cnf_errorcode = v_errorcode
         where t.hf_orderid = l_hf_orderid;
      
      else
      
        if l_product_delaytime <= 5 then
          --5�������ڵ�
          if (l_charge_linetime >= sysdate + 1 / 960) then
            -- 1.5�ֺ�����
            UPDATE od_orderinfo t
               SET t.charge_status     = 2,
                   t.product_slchannel = 3,
                   t.charge_begintime  = sysdate + 1 / 1440 --��һ����
             where t.req_orderid = l_hf_orderid;
          else
            update od_orderinfo t
               set t.charge_status     = 4,
                   t.req_errorcode     = v_errorcode,
                   t.charge_finishtime = sysdate
             where t.req_orderid = l_hf_orderid;
            -- �����յ���Ϣ�ķ���������
            update od_recvwebbill t
               set t.cnf_errorcode = v_errorcode
             where t.hf_orderid = l_hf_orderid;
          end if;
        
        else
          --�ж϶�����������ʱ�����뵱ǰʱ������
          --�����5����֮�ڵģ�������״̬��Ϊʧ�ܣ����ٳ�ֵ
          --������5�������ϵģ���û�г�������ʱ��-5���ӣ��ӳ�orderinfo.charge_begintimeʱ��3���ӣ��ȴ���һ��ƥ������
          if (l_charge_linetime >= sysdate + 4 / 1440) then
            -- 4�ֺ�����
            UPDATE od_orderinfo t
               SET t.charge_status     = 2,
                   t.product_slchannel = 3, --������ԭ��ĳ���ԭ��,�Ա��´���ȡ����ͬ�������ٴγ�ֵ
                   t.charge_begintime  = sysdate + 1 / 480
             where t.req_orderid = l_hf_orderid;
          else
            update od_orderinfo t
               set t.charge_status     = 4,
                   t.req_errorcode     = v_errorcode,
                   t.charge_finishtime = sysdate
             where t.req_orderid = l_hf_orderid;
            -- �����յ���Ϣ�ķ���������
            update od_recvwebbill t
               set t.cnf_errorcode = v_errorcode
             where t.hf_orderid = l_hf_orderid;
          end if;
        end if;
      
      end if;
    
    end if;
    -- �޸�����ʧ�ܼ������������Ϣ
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
create or replace procedure hf.SP_OD_RECHARGEPARTSUCC(v_hfserialid      in VARCHAR2, -- ������ˮ��
                                                  v_channelserialid in VARCHAR2, -- ������ˮ��
                                                  v_sendserialid    in VARCHAR2, -- ���͸��Է���ˮ��
                                                  v_dealamount      in NUMBER, -- ʵ�ʳ�ֵ���
                                                  v_dealtime        in VARCHAR2, -- ����ʱ��
                                                  v_errorcode       in VARCHAR2, -- ������
                                                  v_result          out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �������ػ��ѳ�ֵ'���ֳɹ�'����
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��weisd
  ������ڣ�2011-6
  ������ݣ�(1) �޸Ļ��ѳ�ֵ��¼��״̬Ϊ�����ֳɹ�������¼���ʱ�䣬ʵ�ʴ�������γ�ֵ���ɱ�����������ʱ�䣬���ʱ��ȣ�
            (2) �޸Ļ��Ѷ������յ����е�״̬�������Ϣ��
            (3) �޸Ķ�����Դ������Ϣ�е�ǰ�ߴ�ϺͲ��ߴ�ϱ���
            (4) �޸�������Ϣ���еĵ��ս�����

  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- ������ˮ�Ŵ���
  l_hf_status          NUMBER(2); -- ���ѳ�ֵ״̬
  l_hf_orderid         VARCHAR2(30); -- ���Ѷ�����
  l_hf_acchannel       NUMBER(10); -- ����ID
  l_req_money          NUMBER(10,2); -- �������
  l_charge_finishmoney NUMBER(10,2); -- ʵ�ʵ��˽��
  l_warninfo           VARCHAR2(200); -- ������Ϣ
  l_hf_cost            NUMBER(5, 2); -- �����ɱ�
  l_dealamount         NUMBER(10,2); --��ʱʵ�ʳ�ֵ���
  l_old_finishamount     NUMBER(10,2); --�Ѿ����ֳɹ��Ľ��

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  l_dealamount := v_dealamount; --�滻ԭ����

  -- ��ѯ������ˮ���ڻ��ѳ�ֵ���ױ����Ƿ���ڻ�ɴ���
  -- ��ǰ����״̬ 0���ɹ� 1���ȴ� 2�����ڴ��� 3��ʧ�� 4����ʱ 6�����ֳɹ�  9�����δ֪�����˹����
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- ������ˮ��Ϊ������ʱ�Ĵ���
  if l_od_fullnot_count = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '�������س�ֵ���ֳɹ�,�����ѳ�ֵ��ˮ��' || v_hfserialid || '������';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬״̬Ϊ�ɹ���ʧ��
  if l_hf_status = 0 or l_hf_status = 3 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '���ڳɹ���ʧ��״̬:'|| l_hf_status ||',���������س�ֵ���ֳɹ���Ϣ:'||v_errorcode;
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ���ֳɹ������Ч,����Ϊ0�� ���ֳɹ����봫���
  if l_dealamount is null or l_dealamount = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid ||'�������س�ֵ���ֳɹ�,���ǲ��ֳ�ֵ�������:'||l_dealamount;
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

      -- ��ʱֻ����һ�β��ֳɹ�
  if l_hf_status = 6 then
     select t.finish_money
         into l_old_finishamount
     from od_fullnote t
     where t.hf_serialid = v_hfserialid;

     if l_dealamount < l_old_finishamount  then
         -- �򱨾�������ӱ�����¼
        l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '���ڲ���״̬:'|| l_hf_status ||',���������س�ֵ���ֳɹ���Ϣ,���ؽ���ԭ���С,���Ϊ:'||l_dealamount;
        sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
        v_result := '0000';
        commit;
        RETURN;
     elsif l_dealamount = l_old_finishamount  then
       null;
       RETURN;
     else
       l_dealamount :=  l_dealamount -  l_old_finishamount;--ԭ���Ѿ��ӵĲ��ټ���
     end if;
  end if;


  begin
    -- ������ˮ��״̬Ϊ�ȴ�ʱ����
    if l_hf_status = 1 then
      -- �򱨾�������ӱ�����¼
      l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '���ڵȴ�״̬�����������س�ֵ���ֳɹ���Ϣ';
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    end if;

    -- ��ѯ�������������Ϣ: ������,�������,ʵ�ʵ��˽��
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode(d.req_money, null, 0, d.req_money),
           decode(d.charge_finishmoney, null, 0, d.charge_finishmoney)
      into l_hf_orderid, l_hf_acchannel, l_req_money, l_charge_finishmoney
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    -- ������Ϣ�в�ѯ���ɱ�
    select t.channel_cost
      into l_hf_cost
      from ch_channelinfo t
     where t.channel_id = l_hf_acchannel;

    if l_hf_cost is null then
      l_hf_cost := 1;
    end if;

    if l_dealamount is null then
         l_dealamount := 0; --����Ϊ�պ�0��ǰ���Ѿ��жϷ���
     end if;
    -- ���»��ѳ�ֵ��¼��
    update od_fullnote t
       set t.hf_status        = 6, --���ֳɹ�
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.hf_errorcode     = v_errorcode,
           t.finish_money     = decode(v_dealamount, -- ��β��ֳɹ�ֻ���ô�������
                                       null,
                                       0,
                                       v_dealamount),
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid,
           t.hf_cost          = v_dealamount * (l_hf_cost/100) --  ���ӳɱ�����
     where t.hf_serialid = v_hfserialid;

    -- �����Ŵ���
    if l_hf_orderid is not null then
      if (l_dealamount + l_charge_finishmoney) > l_req_money then
        -- ʵ�ʵ��˽����ڶ������,�޸Ķ���״̬Ϊ�ɹ�
        update od_orderinfo t
           set t.charge_status      = 0,
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) -- ���ﲻ�� //+ 1
         where t.req_orderid = l_hf_orderid;

        -- �򱨾�������ӱ�����¼
        l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '�ж������Ϊ:' ||
                      l_req_money || 'Ԫ,��ǰʵ�ʵ��˽��' || l_charge_finishmoney ||
                      ',�������ֳɹ����ֵ' || l_dealamount || 'Ԫ,�˴γ�ֵ��ʵ�ʵ��˽���Ѵ��ڶ������!';
        sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
      elsif (l_dealamount + l_charge_finishmoney) < l_req_money then
        -- ʵ�ʵ��˽��С�ڶ������,�ۼӽ��
        -- �����Ƿ���Ҫ��Ӳ��ֳɹ� �������Ҫ��ô��
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

    -- �޸Ķ�����Դ������Ϣ�е�ǰ�ߴ�ϺͲ��ߴ�ϱ���: �ݲ���

    -- �޸�������Ϣ���еĵ��ս�����
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
create or replace procedure hf.SP_OD_RECHARGESUCCESS(v_hfserialid      in VARCHAR2, -- ������ˮ��
                                                  v_channelserialid in VARCHAR2, -- ������ˮ��
                                                  v_sendserialid    in VARCHAR2, -- ���͸��Է���ˮ��
                                                  v_dealtime        in VARCHAR2, -- ����ʱ��
                                                  v_dealamount      in NUMBER, -- ʵ�ʳ�ֵ���
                                                  v_result          out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �������ػ��ѳ�ֵ�ɹ�����
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-5-4
  ������ݣ�(1) �޸Ļ��ѳ�ֵ��¼��״̬Ϊ���ɹ�������¼���ʱ�䣬ʵ�ʴ�������γ�ֵ���ɱ�����������ʱ�䣬���ʱ��ȣ�
            (2) �޸Ļ��Ѷ������յ����е�״̬�������Ϣ��
            (3) �޸Ķ�����Դ������Ϣ�е�ǰ�ߴ�ϺͲ��ߴ�ϱ���
            (4) �޸�������Ϣ���еĵ��ս�����

            v2 weisd 5.26 ����ֶ� sendserialid ���͸��Է�����ˮ
            v3 weisd 6.8  ����һ����ʱ l_dealamount, �������治����������ô�����޸Ķ�����ʱ�� ʵ����ɽ��������
                 �����������⣺ if l_hf_cost is not null then  ��ɳɱ� ���� 1
  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- ������ˮ�Ŵ���
  l_hf_status          NUMBER(2); -- ���ѳ�ֵ״̬
  l_hf_orderid         VARCHAR2(30); -- ���Ѷ�����
  l_hf_acchannel       NUMBER(10); -- ����ID
  l_req_money          NUMBER(10,2); -- �������
  l_charge_finishmoney NUMBER(10,2); -- ʵ�ʵ��˽��
  l_warninfo           VARCHAR2(200); -- ������Ϣ
  l_hf_cost            NUMBER(5, 2); -- �����ɱ�
  l_dealamount         NUMBER(10,2); --��ʱʵ�ʳ�ֵ���

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  l_dealamount := v_dealamount; --�滻ԭ����

  -- ��ѯ������ˮ���ڻ��ѳ�ֵ���ױ����Ƿ���ڻ�ɴ���
  -- ��ǰ����״̬ 0���ɹ� 1���ȴ� 2�����ڴ��� 3��ʧ�� 4����ʱ  9�����δ֪�����˹����
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- ������ˮ��Ϊ������ʱ�Ĵ���
  if l_od_fullnot_count = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '�������س�ֵ�ɹ�,�����ѳ�ֵ��ˮ��' || v_hfserialid || '������';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- ������ˮ�ŵ�ǰ״̬Ϊ�ɹ�ʱ�Ĵ���
  if l_hf_status = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '���ڳɹ�״̬�����������س�ֵ�ɹ���Ϣ';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  begin
    -- ������ˮ��״̬Ϊ�ȴ�ʱ����
    if l_hf_status = 1 then
      -- �򱨾�������ӱ�����¼
      l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '���ڵȴ�״̬�����������س�ֵ�ɹ���Ϣ';
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    end if;

    -- ��ѯ�������������Ϣ: ������,�������,ʵ�ʵ��˽��
    select decode(t.hf_orderid, null, '', t.hf_orderid),
           t.hf_acchannel,
           decode(d.req_money, null, 0, d.req_money),
           decode(d.charge_finishmoney, null, 0, d.charge_finishmoney)
      into l_hf_orderid, l_hf_acchannel, l_req_money, l_charge_finishmoney
      from od_fullnote t, od_orderinfo d
     where t.hf_serialid = v_hfserialid
       and t.hf_orderid = d.req_orderid
       and rownum = 1;

    -- ������Ϣ�в�ѯ���ɱ�
    select t.channel_cost
      into l_hf_cost
      from ch_channelinfo t
     where t.channel_id = l_hf_acchannel;

    if l_hf_cost is null then
      l_hf_cost := 1;
    end if;

    if l_dealamount is null then
          select t.full_money  --����û�������ѯ��ֵ��¼��� ֻ��ȡ���
            into l_dealamount
          from od_fullnote t where t.hf_serialid = v_hfserialid;
     end if;
    -- ���»��ѳ�ֵ��¼��
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
           t.hf_cost          = l_dealamount * (l_hf_cost/100) --  ���ӳɱ����� 6.22�޸�
     where t.hf_serialid = v_hfserialid;



    -- �����Ŵ���
    if l_hf_orderid is not null then
      if (l_dealamount + l_charge_finishmoney) > l_req_money then
        -- ʵ�ʵ��˽����ڶ������,�޸Ķ���״̬Ϊ�ɹ�
        update od_orderinfo t
           set t.charge_status      = 0,
               t.charge_finishmoney = l_charge_finishmoney + l_dealamount,
               t.req_trycount       = decode(t.req_trycount,
                                             null,
                                             0,
                                             t.req_trycount) -- ���ﲻ�� //+ 1
         where t.req_orderid = l_hf_orderid;

        -- �򱨾�������ӱ�����¼
        l_warninfo := '���ѳ�ֵ��ˮ��:' || v_hfserialid || '�ж������Ϊ:' ||
                      l_req_money || 'Ԫ,��ǰʵ�ʵ��˽��' || l_charge_finishmoney ||
                      ',�����ɹ����ֵ' || l_dealamount || 'Ԫ,�˴γ�ֵ��ʵ�ʵ��˽���Ѵ��ڶ������!';
        sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
      elsif (l_dealamount + l_charge_finishmoney) < l_req_money then
        -- ʵ�ʵ��˽��С�ڶ������,�ۼӽ��
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

    -- �޸Ķ�����Դ������Ϣ�е�ǰ�ߴ�ϺͲ��ߴ�ϱ���: �ݲ���

    -- �޸�������Ϣ���еĵ��ս�����
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
create or replace procedure hf.SP_OD_RECHARGETIMEOUT(v_hfserialid      in VARCHAR2, -- ������ˮ��
                                                  v_channelserialid in VARCHAR2, -- ������ˮ��
                                                  v_sendserialid    in VARCHAR2, -- ���͸��Է���ˮ��
                                                  v_dealtime        in VARCHAR2, -- ����ʱ��
                                                  v_errorcode       in VARCHAR2, -- ������
                                                  v_result          out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �������ػ��ѳ�ֵ�����ʱ
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-5-10
  ������ݣ� (1) ��ǰʱ�����С�ڶ���Ҫ�����ʱ�䣬��ȴ��������ػ���������ֵ�����ѯ���̣�
             (2) ��ǰʱ��������ڵ��ڶ���Ҫ�����ʱ�䣬���߽��δ֪�˹��������̣�

             v2 weisd 5.26 ����ֶ� sendserialid ���͸��Է�����ˮ
                ��Ӵ�����
  ******************************************************************************/

 AS
  l_od_fullnot_count NUMBER; -- ������ˮ�Ŵ���
  l_hf_status        NUMBER(2); -- ���ѳ�ֵ״̬
  l_hf_orderid       VARCHAR2(30); -- ���Ѷ�����
  l_hf_acchannel     NUMBER(10); -- ����ID
  l_time             NUMBER(10); -- �Ƿ���Լ�����ֵ��ʱ���,�������
  l_warninfo         VARCHAR2(200); -- ������Ϣ

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  -- ��ѯ������ˮ���ڻ��ѳ�ֵ���ױ����Ƿ���ڻ�ɴ���
  -- ��ǰ����״̬ 0���ɹ� 1���ȴ� 2�����ڴ��� 3��ʧ�� 4����ʱ  9�����δ֪�����˹����
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- ������ˮ��Ϊ������ʱ�Ĵ���
  if l_od_fullnot_count = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '�������س�ֵ��ʱ,�����ѳ�ֵ��¼�л�����ˮ��' || v_hfserialid || '������';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬Ϊ�ɹ�ʱ�Ĵ���
  if l_hf_status = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '���ڳɹ�״̬�����������س�ֵ��ʱ!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

    -- ������ˮ��״̬Ϊ���ֳɹ�ʱ�Ĵ���
  if l_hf_status = 6 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '���ڲ��ֳɹ�״̬�����������س�ֵ��ʱ!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬Ϊʧ��ʱ�Ĵ���
  if l_hf_status = 3 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '����ʧ��״̬�����������س�ֵ��ʱ!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬Ϊ��ʱ�Ĵ���
  if l_hf_status = 4 then
    v_result := '0000';
    commit;
    RETURN;
  end if;

  begin
    -- ������ˮ��״̬Ϊ�ȴ�ʱ����
    if l_hf_status = 1 then
      -- �򱨾�������ӱ�����¼
      l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '���ڵȴ�״̬�����������س�ֵ��ʱ!';
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    end if;

    -- ��ѯ�����������Ϣ
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

    -- ��ǰʱ�����С�ڶ���Ҫ�����ʱ��
    if l_time < 0 then
      -- ���»��ѳ�ֵ��¼��,״̬Ϊ4:��ʱ
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
      -- ���»��ѳ�ֵ��¼��,״̬Ϊ9:���δ֪
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
      -- ���»��Ѷ�����,״̬Ϊ8:���δ֪,��Ҫ�˹���Ԥ
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
create or replace procedure hf.SP_OD_RECHARGEUNKNOW( v_hfserialid      in VARCHAR2, -- ������ˮ��
                                                  v_channelserialid in VARCHAR2, -- ������ˮ��
                                                  v_sendserialid    in VARCHAR2, -- ���͸��Է���ˮ��
                                                  v_dealtime        in VARCHAR2, -- ����ʱ��
                                                  v_errorcode       in VARCHAR2, -- ������
                                                  v_result          out VARCHAR2 -- �������
                                                  )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �������ػ��ѳ�ֵ���δ֪����
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-5-9
  ������ݣ�(1) �޸Ļ��ѳ�ֵ��¼��״̬Ϊ�����δ֪���ȣ�
            (2) �޸Ļ��Ѷ������յ����е�״̬�������Ϣ��

            v2 weisd 5.26 ����ֶ� sendserialid ���͸��Է�����ˮ
  ******************************************************************************/

 AS
  l_od_fullnot_count   NUMBER; -- ������ˮ�Ŵ���
  l_hf_status          NUMBER(2); -- ���ѳ�ֵ״̬
  l_hf_orderid         VARCHAR2(30); -- ���Ѷ�����
  l_hf_acchannel       NUMBER(10); -- ����ID
  l_time               NUMBER(10); -- �Ƿ���Լ�����ֵ��ʱ���,�������
  l_req_money          NUMBER(10,2); -- �������
  l_charge_finishmoney NUMBER(10,2); -- ʵ�ʵ��˽��
  l_warninfo           VARCHAR2(200); -- ������Ϣ

begin
  v_result     := '1111';
  l_hf_orderid := '';
  l_warninfo   := '';

  -- ��ѯ������ˮ���ڻ��ѳ�ֵ���ױ����Ƿ���ڻ�ɴ���
  -- ��ǰ����״̬ 0���ɹ� 1���ȴ� 2�����ڴ��� 3��ʧ�� 4����ʱ  9�����δ֪�����˹����
  select count(1),
         nvl(sum(decode(t.hf_status, t.hf_status, t.hf_status, null, 1)), 1)
    into l_od_fullnot_count, l_hf_status
    from od_fullnote t
   where t.hf_serialid = v_hfserialid
     and rownum = 1;

  -- ������ˮ��Ϊ������ʱ�Ĵ���
  if l_od_fullnot_count = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '�������س�ֵδ֪���,�����ѳ�ֵ��¼�л�����ˮ��' || v_hfserialid || '������';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '1111';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬Ϊ�ɹ�ʱ�Ĵ���
  if l_hf_status = 0 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '���ڳɹ�״̬�����������س�ֵ���δ֪!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

    -- ������ˮ��״̬Ϊ���ֳɹ�ʱ�Ĵ���
  if l_hf_status = 6 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '���ڲ��ֳɹ�״̬�����������س�ֵ���δ֪!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬Ϊʧ��ʱ�Ĵ���
  if l_hf_status = 3 then
    -- �򱨾�������ӱ�����¼
    l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid || '����ʧ��״̬�����������س�ֵ���δ֪!';
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    v_result := '0000';
    commit;
    RETURN;
  end if;

  -- ������ˮ��״̬Ϊʧ��ʱ�Ĵ���
  if l_hf_status = 9 then
    v_result := '0000';
    RETURN;
  end if;

   begin
    -- ������ˮ��״̬Ϊ�ȴ�ʱ����
    if l_hf_status = 1 then
      -- �򱨾�������ӱ�����¼
      l_warninfo := '���ѳ�ֵ��¼�л�����ˮ��:' || v_hfserialid ||
                    '���ڵȴ�״̬�����������س�ֵ���δ֪!';
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    end if;

    -- ���»��ѳ�ֵ��¼��,״̬Ϊ9:���δ֪
    update od_fullnote t
       set t.hf_status        = 9,
           t.chfinish_Time    = to_date(v_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
           t.finish_time      = sysdate,
           t.hf_errorcode     = v_errorcode,
           t.channel_serialid = v_channelserialid,
           t.send_serialid    = v_sendserialid
           --,t.trycount         = decode(t.trycount, null, 0, t.trycount) + 1
     where t.hf_serialid = v_hfserialid;

    -- ��ѯ�����������Ϣ
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

    -- �ж϶������Ƿ����
    if l_hf_orderid is not null then
        -- �޸Ķ���״̬: 8 ��Ҫ�˹�����
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
create or replace procedure hf.SP_OD_REVERSE_DEAL(v_hforderid in VARCHAR2, -- ������
                                               v_result    out VARCHAR2 -- �������
                                               )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �ж϶����Ƿ���Գ���
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��liujiangtao
  ������ڣ�2011-5-4
  ������ݣ�(1) �ж϶����Ƿ���ڲ�״̬
            (2) ��ȡ����������ˮ��¼��״̬
            (3) ��ȡ�����Ŀɳ���״̬
            (4) ������Ӧ�������̨����
            
          v2  weisd �޸Ľ��������
  ******************************************************************************/
 AS
  l_count1 NUMBER; -- ��������
  l_count2 NUMBER; -- ����״̬
  l_count3 NUMBER; -- ������ˮ�������Ƿ�ɳ�ֵ

begin
  v_result := '0000';
  begin
  
    -- ��ѯ������Ϣ
    select count(1),
           nvl(sum(decode(t.charge_status, null, 3, t.charge_status)), 3)
      into l_count1, l_count2
      from od_orderinfo t
     where t.req_orderid = v_hforderid
       and rownum = 1;
  
    if l_count1 <= 0 then
      -- ����������
      v_result := '2025';
      RETURN;
    end if;
  
    if l_count2 != 0 then
      -- ����״̬��Ϊ�ɹ�
      v_result := '2026';
      RETURN;
    end if;
    
    --  ��ѯ������ˮ�������ɳ�ֵ
    SELECT COUNT(1)
      into l_count3
      from od_fullnote o, ch_channelinfo c
     where o.hf_orderid = v_hforderid
       and o.hf_acchannel = c.channel_id
       and o.hf_status = 0
       and c.channel_isreversed = 0
       and rownum = 1;
       
    if l_count3=0 then
       -- û���ҵ��ɳ���������Ϣ: �������������(һ�������������������)
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
CREATE OR REPLACE PROCEDURE HF.SP_OD_SEARCHREVERSE_BEGIN(v_pageNo    number, --��ѯҳ��
                                                      v_pageSize  number, --ÿҳ��ʾ��С
                                                      v_status    in VARCHAR2, --Ҫ��ȡ��״̬
                                                      v_resultCur out HF_CURSOR.CURSOR_TYPE) --��ȡ��¼����
 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-��ȡ��Ҫ��ѯ�������״̬δ֪��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-6-10
  �޸����ݣ�
  ����������v1����ѯ�������״̬δ֪��

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
    select hf_serialid, --������
           hf_orderid, --ǰ̨֧��������
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
  --close v_resultCur; -- ���ܹر�
END;
/

prompt
prompt Creating procedure SP_OD_SEARCHSTATUS_BEGIN
prompt ===========================================
prompt
CREATE OR REPLACE PROCEDURE HF.SP_OD_SEARCHSTATUS_BEGIN(v_result          out varchar2, --�����
                                                     v_processcount    out varchar2, --�������Ķ�����
                                                     v_hforderid       out varchar2, --�����ţ��Զ��Ÿ���
                                                     v_hfserialid      out varchar2, --������ˮ�ţ��Զ��Ÿ���
                                                     v_fullmoney       out varchar2, --��ֵ���Զ��Ÿ���
                                                     v_channelserialid out varchar2, --��ˮ
                                                     v_sendserialid    out varchar2, --��ˮ
                                                     v_ispid           out varchar2, --��Ӫ��ID
                                                     v_channelid       out varchar2, --����ID
                                                     v_accnum          out varchar2, --��ֵ����
                                                     v_provinceid      out varchar2, --
                                                     v_begintime       out varchar2, --
                                                     v_finishtime      out varchar2 --
                                                     ) --��ȡ��¼����
 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-��ȡ��Ҫ��ѯ��ֵ״̬��������Ϣ
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-08-02
  �޸����ݣ�
  ����������v1����ѯ����ʱ�ģ����� ʱ�䲻��������ʱ�䣬û������ѯ������
            v2:
            v3: Ϊ��ƥ��lvs���޸Ĳ�ѯsql ,���ڲ�ѯ1����֮���޷��ؽ����
                ����һ�����⣺���������8�����ڲ�ѯ֮�� �Է�û��Ӧ ��ô״̬�Ͳ���ı� ��
  
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
  l_status          number; --���ѳ�ֵ��¼״̬
  l_trycount        number;
  l_provinceid      number;
  l_begintime       date;
  l_finishtime      date;

  l_qm_status   number; --��ѯ״̬
  l_hf_status   number; -- �Ƿ�֪ͨ�ˣ��޸ģ���ֵ��¼
  l_qm_trycount number; -- ��ѯ����
  l_qm_count    number; -- �Ƿ���ڼ�¼
  l_qm_begintime date;-- ��ʼ��ѯʱ��

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
               and f.hf_status = 4 --Ϊ 4��ʱ
               and o.charge_status not in (0, 4, 5) --����״̬����Ϊ����״̬��
               and f.trycount < 8 --��ѯ���� v_maxcnt �̶���
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
      --ȡһ������
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
  
    --���ǳ�ʱ�����ڵ���8�ξͲ��ٲ�ѯ
    IF l_status <> 4 or l_trycount >= 8 THEN
      ROLLBACK;
      GOTO loopstart;
    END IF;
  
    -- �Ƿ���ڲ�ѯ��ʷ
    select count(1)
      into l_qm_count
      from OD_PHONECHARGEMONEY tp
     where tp.hf_serialid = l_hfserialid;
  
    if l_qm_count > 0 then
      select tp.qm_status, tp.hf_status, tp.qm_trycount,tp.qm_begintime 
        into l_qm_status, l_hf_status, l_qm_trycount,l_qm_begintime 
        from OD_PHONECHARGEMONEY tp
       where tp.hf_serialid = l_hfserialid;
      -- �Ƿ��Ѿ�֪ͨ���޸ģ����Ѽ�¼���Ƿ񳬹�8�Σ��Ƿ����ڲ�ѯ,���ǲ���1����֮�ڵ�
      if l_hf_status = 0 or l_qm_trycount >= 8 or (l_qm_status = 2 and l_qm_begintime > (sysdate - 1/1440) )  then
        ROLLBACK;
        GOTO loopstart;
      end if;
      --�޸�Ϊ���ڲ�ѯ���޸Ĵ���
      update OD_PHONECHARGEMONEY p
         set p.qm_begintime = sysdate,
             p.qm_status    = 2,
             p.qm_trycount  = p.qm_trycount + 1
       where p.hf_serialid = l_hfserialid;
    
    else
      --�����ѯ��¼��״̬Ϊ2
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
         2, --����ϵͳ�Զ���ѯ����Ϊ2
         1, --��ѯ����Ϊ 1
         0,
         l_ispid,
         1);
    
    end if;
  
    --����+1
    update OD_FULLNOTE f
       set f.trycount = f.trycount + 1
     where f.hf_serialid = l_hfserialid;
  
    --ȡ��һ������������1
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
      --ȡ��������ȡ��10���˳�
      EXIT;
    END IF;
  
  END LOOP;
  CLOSE cur_resultcur;

  --3����ֵ��¼ȡ�����
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
create or replace procedure hf.SP_OD_SENDORDERINFO(v_hforderid    in VARCHAR2, -- ����ƽ̨������
                                                v_online_id    in NUMBER, -- ����ID
                                                v_mobilenum    in VARCHAR2, -- �ֻ�����
                                                v_chargeamount in NUMBER, -- ��ֵ���
                                                v_paymount     in NUMBER, -- �������
                                                v_orderid      in VARCHAR2, -- ������
                                                v_ordersource  in VARCHAR2, -- ������Դ
                                                v_agentid      in VARCHAR2, -- ������ID(�û�ID)
                                                v_ordertime    in VARCHAR2, -- ����ƽ̨����ʱ��
                                                v_mark         in VARCHAR2, -- �����ֶ�
                                                v_result       out VARCHAR2 -- �������
                                                )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- ���ѳ�ֵԤ�µ�ת��Ϊ�����֧���ȴ�����
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��weisd
  ������ڣ�2011-6-23
  ������ݣ�Ԥ�µ�(SP_OD_PREPAREORDERINFO)�ɹ���
            ǰ̨ȷ����Ϻ��ٴ����󷢻���ֵ�����޸�Ϊ�����֧�����ȴ�����
            08.14 �޸ķ�����

  ******************************************************************************/

 AS

  l_hforderid_count    NUMBER(10); --�Ƿ���ڸû��Ѷ���
  l_now_hforder_status NUMBER(2); -- ��ǰ������״̬

begin
  v_result := '1111';

  -- ��ѯ���͹����Ķ������Ƿ��Ѵ��ڣ��Ƿ�����Ϣƥ��
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
  -- v_ordertime    in VARCHAR2, -- ����ƽ̨����ʱ��  ��ʱ��ƥ��
  if l_hforderid_count = 1 then

    BEGIN
      --ȡ��ǰ����
      select t.charge_status
        into l_now_hforder_status
        from od_orderinfo t
       where t.req_orderid = v_hforderid
         and t.req_saleserialid = v_orderid
         FOR UPDATE;
    EXCEPTION
      WHEN OTHERS THEN
        v_result := '0014'; --��ȡҪ�����Ķ���׼���޸�ʧ�ܣ��������ڱ�ʹ�ã�
        ROLLBACK;
    END;

    if l_now_hforder_status = 1 then
      update od_orderinfo t
         set t.charge_status = 2
       where t.req_orderid = v_hforderid
         and t.req_saleserialid = v_orderid;
         
      v_result := '0000';

    else

      v_result := '0015'; --��ǰ����״̬����Ԥ�µ����޷����з�������

    end if;

  else
    v_result := '0013'; --��Ԥ�µ����з�������ʱ���ѯ����ʧ��(���ݲ���ƥ���ѯ��������Ϊ1)
  end if;
  --  �����յ����еĶ�����
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
                                                    v_channelkey in VARCHAR2, --��Ҫ�޸ĵ�����id����
                                                    v_opttype    in number, --�޸�״̬
                                                    v_opttime    in VARCHAR2, --���͹�����ʱ��
                                                    v_optname    in VARCHAR2, --������id��name
                                                    v_errorcode  in VARCHAR2, --������
                                                    v_errormsg   in VARCHAR2, --������Ϣ
                                                    v_result     out VARCHAR2) --���ؽ������

 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-�޸���������״̬
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-6-22
  �޸����ݣ�
  ����������������Ϣ(���أ��Լ�Ӧ�÷��ͻ�������Ϣ)�޸���������״̬
            v_errormsg Ӧ��������
            v_channelkey ,key,key,key,key,key,������
  ************************************************************/

  l_channelid VARCHAR2(40); -- ����id
  l_count     NUMBER(10); -- ����
  l_status    NUMBER(2); -- ��״̬
  l_optuser   VARCHAR2(40);
  l_warninfo  VARCHAR2(200); -- ������Ϣ

  CURSOR channelid_split is
    select column_value
      from table(cast(myconvert(v_channelkey, ',') as t_vc))
     where column_value is not null;

BEGIN
  v_result := '1111';

  if v_opttype is null or v_optname is null then
    l_warninfo := '�޸�����״̬' || l_channelid || '��Ч,������ƥ��:' || l_status || ',' ||
                  v_resultno || ',' || v_opttype || v_opttime || ',' ||
                  v_optname || ',' || v_errorcode || ',' || v_errormsg;
  
    sp_sys_dberrorinfo_create(1, '����', '', l_warninfo, '', '');
    commit;
    v_result := '1111';
    return;
  end if;

  if v_resultno = '9988' then
    --�������͹����رյ�
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
           v_resultno||','||v_opttime || ',' || v_errorcode || ',' || v_errormsg, --���ܳ�������
           null,
           0);*/
           
      end if;
    
    end if;
  
  END LOOP;
  CLOSE channelid_split;
  --ѭ��ȫ���ɹ�
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
CREATE OR REPLACE PROCEDURE HF.SP_OD_UPDATEORDERNOTICESTATUS(v_hforderid       in VARCHAR2, --Ҫ��ȡ��״̬
                                                          v_reqsaleserialid in VARCHAR2, --ǰ̨֧��������
                                                          v_ecnoticeflag    in VARCHAR2, --Ҫ�޸ĵ�״̬
                                                          v_needagain       out VARCHAR2, --�Ƿ���Ҫ��֪ͨ�����֪ͨʧ��
                                                          v_result          out VARCHAR2) -- �������
 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-�޸Ķ�����֪ͨ״̬Ϊ�磺����֪ͨ,����֪ͨ�ɹ���֪ͨʧ�� ����v_ecnoticeflag �ж�
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-5-23
  �޸����ݣ�
  �����������޸Ķ�����֪ͨ״̬Ϊ�磺����֪ͨ,����֪ͨ�ɹ���֪ͨʧ�� ����֪ͨʧ�ܵģ������Ƿ���Ҫ��֪ͨ
                                    v_result Ϊ�޸�״̬�Ƿ��޸ĳɹ�
                                    v_needagain  ���ж�v_result Ϊ 00000 �ٸ���  22222 Ϊ��Ҫ��֪ͨ
            v2 weisd �����ĵ�Ҫ�� ��5�����ط�����ǰ���ػ������ط�������SP_OD_NEEDNOTICEORDER_BEGIN �а���
            v3 weisd 8.02 ��֧���������޸�Ϊ����֪ͨ��
  ************************************************************/
  l_count       NUMBER(10); -- ����
  l_noticestatus  NUMBER(4);--һ��temp״̬
BEGIN

  --v_status  ��ʱ����
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
        --��̨֪ͨ��ʶ��0��֪ͨ��1δ֪ͨ��2����֪ͨ,3֪ͨʧ��
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
            -- ��֧���������޸�Ϊ����֪ͨ��
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
create or replace procedure hf.SP_OD_UPDATEORDERSTATUS(v_hfserialid     in VARCHAR2, -- ������ˮ��
                                                    v_orderid        in varchar2, --������
                                                    v_fullnot_status in number, -- �޸ĺ�Ļ��ѳ�ֵ��¼״̬
                                                    v_order_status   in number, --�޸ĺ�Ķ���״̬
                                                    v_result         out VARCHAR2 -- �������
                                                    )

  /******************************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�- �޸Ļ��ѳ�ֵ��¼��Ͷ������м�¼��״̬
  ģ��汾��1.0
  ���뻷����ORACLE10g
  �����Ա��wjjava
  ������ڣ�2011-5-5
  ������ݣ����v_hfserialid<>null,������Ϊv_hfserialid��Ӧ�Ķ�����
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
CREATE OR REPLACE PROCEDURE HF.SP_OD_UPDATE_SEARCHSTATUS(v_hfserialid   in VARCHAR2, --������ˮ��
                                                      v_status       in NUMBER, --Ҫ�޸ĵ�״̬OD_PHONEC
                                                      v_chargestatus in VARCHAR2, --��ֵ���״̬
                                                      v_dealtime     in VARCHAR2, --��ֵ����ʱ��
                                                      v_dealamount   in NUMBER, --ʵ�ʳ�ֵ���
                                                      v_errorcode    in VARCHAR2, --������
                                                      v_restatus     out VARCHAR2, --��ǰ״̬OD_PHONEC
                                                      v_isupdate     out VARCHAR2, --�Ƿ�����˻���״̬
                                                      v_result       out VARCHAR2) -- �������
 is
  /************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1
  ģ�����ƣ����ݿ�ű�-�޸� OD_PHONECHARGEMONEY �в�ѯ���״̬
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��weisd
  �޸����ڣ�2011-6-3
  �޸����ݣ�
  �����������������ѯ��ʱ�ĳ�ֵ��¼�Ľ��״̬�޸ģ��ݲ��ṩOD_PHONE��1��4״̬�޸�
            ����Ҫ��ѯ�ĳ�ֵ��¼
            �Է��ؽ������ֵ�ɹ�����ֵʧ��
            v2:weisd �����ѯ���Ϊ���������ڴ���
                     ���䲿�ֳɹ�����
            v3:weisd ȥ����״̬�޸�Ϊ���ڲ�ѯ��ֻ�ڲ�ѯ������ʱ���޸�,
                     ���Ҿ�������ʱ����30���ѯ�ȵ�
  ************************************************************/
  l_count           NUMBER(10); -- ����
  l_status          NUMBER(2);
  l_isupdate        number(1); -- p���Ƿ��Ѿ������˻��ѱ�
  l_temp_isupdate   number(1); -- temp
  l_search_count    NUMBER(10); -- ��ѯ����
  l_hf_status       number(1); --ԭ��ֵ��¼״̬
  l_dealtime        varchar2(30); --����ʱ��
  l_dealamount      NUMBER(10, 2); --��ʱ������
  l_result          varchar2(10); --���ô洢������ʱ���
  l_channelserialid VARCHAR2(40); -- ������ˮ��
  l_sendserialid    VARCHAR2(40); -- ���͸��Է���ˮ��
  l_warninfo        VARCHAR2(200); -- ������Ϣ
  l_fullmoney       NUMBER(10, 2); --��ֵ��¼�ĳ�ֵ���
  l_hf_orderid      VARCHAR2(30); -- ���Ѷ�����
  l_time            NUMBER(10); -- �Ƿ���Լ�����ֵ��ʱ���,�������

BEGIN

  v_result        := '1111';
  l_result        := '2222';
  l_temp_isupdate := 1; --Ĭ��ֵ
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
    --��ʱ���ṩ����
    v_restatus := v_status;
    v_result   := '1111';
    return;
  end if;

  select count(1)
    into l_count
    from OD_PHONECHARGEMONEY p
   where p.hf_serialid = v_hfserialid;

  if l_count = 0 then
     -- û�м�¼�޷�������һ������
      l_warninfo := '��ѯ��ֵ��¼' || v_hfserialid || '�н�����ش���,�����޷���OD_PHONECHARGEMONEY��ȡ��' ||
                    v_hfserialid ||'��ѯ��¼';
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
      --���ǳ�ʱ��,���Ѿ����Ĺ�����״̬�� ����
      v_restatus := v_status;
      v_result   := '1111';
    return;
  end if;

  BEGIN
    --��ѯ����
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
    -- ��ѯ�����������Ϣ
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
      --����Ѿ�����8���� �����Ѿ� + 30�� >= Ҫ�����ʱ��
      -- ���»��ѳ�ֵ��¼��,״̬Ϊ9:���δ֪
      update od_fullnote t
         set t.hf_status     = 9,
             t.chfinish_time = to_date(l_dealtime, 'YYYY-MM-DD HH24:MI:SS'),
             t.finish_time   = sysdate,
             t.hf_errorcode  = v_errorcode
       where t.hf_serialid = v_hfserialid;
      -- ���»��Ѷ�����,״̬Ϊ8:���δ֪,��Ҫ�˹���Ԥ
      update od_orderinfo t
         set t.charge_status     = 8,
             t.charge_finishtime = sysdate,
             t.req_errorcode     = v_errorcode
       where t.req_orderid = l_hf_orderid;

       l_temp_isupdate := 0;

    end if;
        --��ѯʧ��,�޸Ĳ�ѯ״̬��
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
    -- ��ѯ�ɹ� ��ѯ���������ѯ��������ѯ״̬ ��ѯ������޸Ķ���״̬�����˽��
    select f.hf_status, f.channel_serialid, f.send_serialid, f.full_money
      into l_hf_status, l_channelserialid, l_sendserialid, l_fullmoney
      from OD_FULLNOTE f
     where f.hf_serialid = v_hfserialid;

    if l_hf_status = 4 and l_isupdate = 1 then

      --���ݲ�ѯ���������Ӧ״̬ ���¶���״̬�������ֵ״̬�Ѿ��� ����״̬��
      if v_chargestatus = '0' then
        --��ֵ�ɹ�

        SP_OD_RECHARGESUCCESS(v_hfserialid,
                              l_channelserialid,
                              l_sendserialid,
                              l_dealtime,
                              l_dealamount,
                              l_result); --���óɹ�����

      elsif v_chargestatus = '3' then
        --��ֵʧ��

        SP_OD_RECHARGEFAILURE(v_hfserialid,
                              l_channelserialid,
                              l_sendserialid,
                              l_dealtime,
                              v_errorcode,
                              l_result);

      elsif v_chargestatus = '6' then
            -- ���ֳɹ�
          SP_OD_RECHARGEPARTSUCC(v_hfserialid,
                                l_channelserialid,
                                l_sendserialid,
                                l_dealamount,
                                l_dealtime,
                                v_errorcode,
                                l_result);

      elsif v_chargestatus = '2' then
            -- �������ڴ��� ��������ֻ�޸Ĳ�ѯ״̬
         null;

      end if;

      if l_result = '0000' then

        l_temp_isupdate := 0; --����Ϊ��֪ͨ
        v_restatus      := v_chargestatus;
        v_isupdate      := '0';
        v_result        := '0000';

      elsif l_result = '1111' then
        l_warninfo := '��ѯ��ֵ��¼' || v_hfserialid || '�ɹ�,�޸ĳ�ֵ�����������״̬' ||
                      v_chargestatus || 'ʧ��' || l_result;
        sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
        v_restatus := v_chargestatus;
        v_isupdate := l_isupdate;
        v_result   := '3333'; --��ѯ����ֵ��¼����״̬�������޸����ݿⶩ��֮��ʧ��

      else
        l_warninfo := '��ѯ��ֵ��¼' || v_hfserialid ||
                      '�ɹ�,���ǳ�ֵ״̬�������ս��,���Գ�ֵ��¼���޸ģ�' || v_chargestatus ||
                      ',ֻ�޸Ĳ�ѯ����';
        sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
        v_restatus := v_chargestatus;
        v_isupdate := l_isupdate;
        v_result   := '0000'; --��ѯ����ֵ��¼����״̬�������޸����ݿⶩ��֮��ʧ��

      end if;

    else
      l_warninfo := '��ѯ��ֵ��¼' || v_hfserialid || '�ɹ�,���Ǽ�¼״̬��Ϊ��ʱ,����' ||
                    l_hf_status || ',l_isupdate:' || l_isupdate;
      sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
      --���ǳ�ʱ��,���Ѿ����Ĺ�����״̬�� ����
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
    l_warninfo := '��ѯ��ֵ��¼' || v_hfserialid || '�쳣,' || v_status || ',' ||
                  v_chargestatus || ',' || v_dealtime || ',' ||
                  v_dealamount || ',' || v_errorcode || ',' || l_hf_status || ',' ||
                  l_result;
    sp_sys_dberrorinfo_create(1, 'ϵͳ', '', l_warninfo, '', '');
    commit;
END;
/

prompt
prompt Creating procedure WEISD_TEST
prompt =============================
prompt
CREATE OR REPLACE PROCEDURE HF.WEISD_TEST(                v_Result       OUT VARCHAR2 --�����
                                                       )
/************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1.0
  ģ�����ƣ����ݿ�ű�-��ֵ����,�������������ɻ�����ˮ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-05-05
  �޸����ݣ�
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
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1.0
  ģ�����ƣ����ݿ�ű�-��ֵ����,�������������ɻ�����ˮ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-05-05
  �޸����ݣ�
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
CREATE OR REPLACE PROCEDURE HF.WEISD_TEST_SP2(                v_Result       OUT VARCHAR2 --�����
                                                       )
/************************************************************
  ��Ʒ���ƣ����ѳ�ֵϵͳ
  ��Ʒ�汾��V1.0
  ģ�����ƣ����ݿ�ű�-��ֵ����,�������������ɻ�����ˮ��
  ģ��汾��1.0.0.0
  ���뻷����ORACLE9i��ORACLE10g
  �޸���Ա��wjjava
  �޸����ڣ�2011-05-05
  �޸����ݣ�
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
