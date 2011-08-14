create or replace procedure SP_OD_SENDORDERINFO(v_hforderid    in VARCHAR2, -- 话费平台订单号
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

    else

      v_result := '0015'; --当前订单状态不是预下单，无法进行发货请求

    end if;

  else
    v_result := '0013'; --由预下单进行发送请求时候查询订单失败(根据参数匹配查询订单数不为1)
  end if;

  v_result := '0000';
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
