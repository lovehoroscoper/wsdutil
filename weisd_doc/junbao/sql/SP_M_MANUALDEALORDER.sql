create or replace procedure SP_M_MANUALDEALORDER(v_dealType    number, --处理方式 0：充值成功 1：充值失败 2：再次充值
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
begin
  v_result := '0000';
  begin
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
