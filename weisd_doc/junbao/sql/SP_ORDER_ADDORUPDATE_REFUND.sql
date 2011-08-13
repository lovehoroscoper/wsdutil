CREATE OR REPLACE PROCEDURE SP_ORDER_ADDORUPDATE_REFUND(v_opttype            in number, --操作类型
                                                        v_refundno           in varchar2, --退款序号
                                                        v_ordersno           in varchar2, --订单号
                                                        v_actualrefundamount in number, --实际退款金额已扣除手续费
                                                        v_refundcost         in number, --手续费
                                                        v_refundaccount      in VARCHAR2, --退款账户
                                                        v_refundreason       in varchar2, --退款说明
                                                        v_status             in number, --状态
                                                        v_opter              in varchar2, --操作人
                                                        v_errorcode          out VARCHAR2, --详细错误码
                                                        v_result             out VARCHAR2 --返回码
                                                        ) is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-订单退款/以及审批
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-6-30
  修改内容：
  功能描述：用于客服申请退款/财务审批退款
            v_opttype :
            0 ：申请退款
            2011.08.01 weisd 能退款的订单为：支付成功，充值成功
                       （但是用户反映失败的，故需要退款），只修改支付状态为退款
            08.10  退款金额 修改
  ************************************************************/
  --l_loginfo          VARCHAR2(1000); -- 日志信息
  l_order_count          NUMBER(10); --是否存在符合退款的订单号
  l_refund_count         NUMBER(10); --是否存在正在提交的退款申请
  l_orderamount          NUMBER(13, 3); -- 订单支付金额
  l_needrefundamount     NUMBER(13, 3); --订单应退金额
  l_realamount           NUMBER(13, 3); --已到帐金额
  l_tradesno             varchar2(20); --账户交易流水号
  l_sno_result           varchar2(20);
  l_already_refundamount NUMBER(13, 3); -- 订单已退款金额
  l_refundcost           NUMBER(13, 3); --手续费
  l_agentid              varchar2(20); --代理商账户ID
  l_status               number(1); --当前状态
  l_paystatus            number(1); --订单支付状态
  l_orderstatus          number(1); --订单充值状态
  l_actualrefundamount   NUMBER(13, 3); --实际退款金额
  l_tradeno              varchar2(40); --退款流水号
  l_balance              NUMBER(13, 3); --退款后的账户余额
  l_result               varchar2(10); -- 结果代码
  l_refundreason         varchar2(200);

BEGIN

  if v_opttype = 0 then
  
    if v_refundcost is null then
      l_refundcost := 0;
    else
      l_refundcost := v_refundcost;
    end if;
  
    if v_ordersno is null or v_actualrefundamount is null or
       v_actualrefundamount <= 0 or v_refundaccount is null or
       v_status is null then
      v_errorcode := '0001'; --参数验证失败
      v_result    := '1111';
      ROLLBACK;
      return;
    end if;
  
    select count(1)
      into l_order_count
      from SALE_ORDERINFO t
     where t.ordersno = v_ordersno
       and t.paystatus = 2
       and t.orderstatus = 1; -- 2 
  
    if l_order_count != 1 then
      v_errorcode := '0002'; --没有找到该订单或者该订单暂时不符合退款状态
      v_result    := '1111';
      ROLLBACK;
      return;
    end if;
  
    select count(1)
      into l_refund_count
      from sale_refund t
     where t.ordersno = v_ordersno
       and t.status = 0;
  
    -- weisd 多并发是否可以保证
    if l_refund_count > 0 then
      v_errorcode := '0003'; --该订单已经存在正在申请记录,请先处理然后再申请
      v_result    := '1111';
      ROLLBACK;
      return;
    end if;
  
    --查询订单信息
    select t.orderamount,
           nvl(t.realamount, 0),
           decode(sign(to_char(nvl(t.orderamount, 0) - nvl(t.realamount, 0),
                    'FM999999999999990.0099')),
              1,
              to_char(nvl(t.orderamount, 0) - nvl(t.realamount, 0),
                      'FM999999999999990.0099'),
              to_char(t.orderamount, 'FM999999999999990.0099')),
           t.agentid --代理商账户与id一致
      into l_orderamount, l_realamount, l_needrefundamount, l_agentid
      from SALE_ORDERINFO t
     where t.ordersno = v_ordersno;
  
    select nvl(sum(t.actualrefundamount + t.factorage), 0) --实际退款+手续费
      into l_already_refundamount
      from sale_refund t
     where t.ordersno = v_ordersno
       and t.status = 1; --已存在退款的
  
    l_sno_result := '0000';
    SP_ORDER_GENERATE_REFUNDSNO(l_sno_result, l_tradesno);
  
    -- weisd 这里是否还需要判断 已经退款了的金额
    if (v_actualrefundamount + l_refundcost) > l_needrefundamount   then 
      v_errorcode := '0009'; -- 申请退款金额+ 手续费 > 最大能退款金额
      v_result    := '1111';
      ROLLBACK;
      return;    
    elsif (v_actualrefundamount + l_refundcost + l_already_refundamount) > l_orderamount then
      v_errorcode := '0004'; --退款金额 + 手续费 + 已到帐金额 + 已退款 > 支付金额
      v_result    := '1111';
      ROLLBACK;
      return;
    elsif l_agentid <> v_refundaccount then
      v_errorcode := '0006'; --退款账户与订单代理商ID不一致
      v_result    := '1111';
      ROLLBACK;
      return;
    elsif l_sno_result <> '0000' then
      v_errorcode := '0005'; --生成退款流水失败
      v_result    := '1111';
      ROLLBACK;
      return;
    else
    
      insert into SALE_REFUND
        (refundno,
         ordersno,
         orderamount,
         refundamount,
         factorage,
         applicationdate,
         applicant,
         actualrefundamount,
         refundreason,
         status)
      values
        (l_tradesno,
         v_ordersno,
         l_orderamount,
         l_needrefundamount,
         l_refundcost,
         sysdate,
         v_opter,
         v_actualrefundamount,
         v_refundreason,
         0);
    end if;
    v_result := '0000';
  
  elsif v_opttype = 1 then
    --财务审核
    if v_refundno is null or v_ordersno is null or v_status is null or v_status not in (1, 2) then
      v_errorcode := '0001'; --参数验证失败
      v_result    := '1111';
      ROLLBACK;
      return;
    end if;
  
    select count(1)
      into l_refund_count
      from sale_refund t
     where t.refundno = v_refundno
       and t.ordersno = v_ordersno;
  
    if l_refund_count != 1 then
      v_errorcode := '0006'; --找不到该记录的申请情况
      v_result    := '1111';
      ROLLBACK;
      return;
    end if;
  
    select t.status, t.actualrefundamount, t.factorage, t.refundreason
      into l_status, l_actualrefundamount, l_refundcost,l_refundreason
      from sale_refund t
     where t.refundno = v_refundno
       and t.ordersno = v_ordersno
       for update;
  
    select o.paystatus, o.agentid, o.orderstatus 
      into l_paystatus, l_agentid, l_orderstatus 
      from SALE_ORDERINFO o
     where o.ordersno = v_ordersno
      for update; 
  
    if l_status != 0 or l_paystatus != 2 or l_orderstatus != 1 then
      v_errorcode := '0007'; --申请记录状态或者订单支付状态,充值状态不匹配
      v_result    := '1111';
      ROLLBACK;
      return;
    end if;
  
    if v_refundreason is not null then 
       l_refundreason := v_refundreason;
    end if;
  
    update SALE_REFUND r
       set r.status     = v_status,
           r.approver   = v_opter,
           r.refundreason = l_refundreason,
           r.refundtime = sysdate
     where r.refundno = v_refundno
       and r.ordersno = v_ordersno;
  
    if v_status = 1 then
      --退款成功后
      update SALE_ORDERINFO o
         set o.paystatus = 5
       where o.ordersno = v_ordersno; --时间不修改
    
      -- 调用退款接口
      sp_acc_refund(v_refundno,
                    l_agentid,
                    v_ordersno,
                    null,
                    l_actualrefundamount,
                    l_refundcost,
                    sysdate,
                    l_tradeno,
                    l_balance,
                    l_result);
    
      if l_result = '0000' then
      
        v_result := '0000';
      
      else
        v_errorcode := '0008'; --调用退款接口失败
        v_result := '1111';
        ROLLBACK;
      end if;
    
    elsif v_status = 2 then
    
      v_result := '0000';
    
    end if;
  
  else
  
    v_errorcode := '0100'; --暂时不支持的操作
    v_result    := '1111';
    ROLLBACK;
    return;
  
  end if;

  commit;

EXCEPTION
  WHEN OTHERS THEN
    v_errorcode := v_errorcode;
    v_result := '1111';
    ROLLBACK;
   -- 防止一种情况 
   --l_result = 0000 但是最后时候失败了
END;
