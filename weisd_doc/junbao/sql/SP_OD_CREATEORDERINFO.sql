create or replace procedure SP_OD_CREATEORDERINFO(v_online_id    in NUMBER, -- 货架ID
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
            
  /*        elsif to_number(l_p_product_citycode) != 0 and l_p_product_citycode <> l_city_code then          
            -- 手机号城市与货架城市不一致
              v_result := '0021';
              update od_recvwebbill t
                 set t.cre_errorcode = v_result 
               where t.req_orderid = v_orderid
                 and t.order_source = v_ordersource;
              commit;
              RETURN;*/
            
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
