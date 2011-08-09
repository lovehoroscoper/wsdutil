CREATE OR REPLACE PROCEDURE SP_OD_NEEDNOTICEORDER_BEGIN(v_pageNo    number, --查询页码
                                                        v_pageSize  number, --每页显示大小
                                                        v_status    in VARCHAR2, --要获取的状态
                                                        v_resultCur out HF_CURSOR.CURSOR_TYPE) --获取记录集合
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
  ************************************************************/
  --l_queryList HF_CURSOR.CURSOR_TYPE;
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
  
    select req_orderid, --订单号
           req_saleserialid, --前台支付订单号
           charge_status, --订单状态
           req_money, --订单金额
           charge_finishmoney, --实际完成金额
           req_paymoney, --实际支付金额
           product_onlineid, --货架id
           req_productid, --商品id
           req_provinceid, --省份id
           req_citycode, --城市id
           req_accnum, --充值号码
           req_trycount, --充值次数
           req_ispid, --运营上id
           req_servicetype, --业务类型
           req_ordersource, --订单来源
           req_userid, --用户id代理商id
           os_backurl,
           os_key
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
                   o.os_key --验证密钥
              from od_orderinfo t, CH_ORDERSOURCEINFO o
             where t.req_ordersource = o.os_id
               and (t.charge_status = 0 or (t.charge_status in (4, 5, 6) and
                   t.charge_linetime <= sysdate))
               and (t.ec_notifyflag = 1 or --未通知
                   (t.ec_notifyflag = 3 and --通知失败
                   (t.ec_notifycount + 1) <= o.os_maxcnt and --小于阀值
                   (t.ec_notifytime + (1 / 1440)) <= sysdate)) --距离上次1分钟
             order by nvl(t.charge_finishtime, t.charge_begintime) asc,
                      t.req_trycount asc)
     where rownum <= l_pageSize;

  --v_resultCur := l_queryList;  
  --close v_resultCur; -- 不能关闭
END;
