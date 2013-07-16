create or replace procedure SP_OD_RECHARGEFAILURE(v_hfserialid      in VARCHAR2, -- 话费流水号
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
          if (l_charge_linetime <= sysdate + 1 / 960) then
            -- 1.5分后没到限制
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
            if (l_charge_linetime <= sysdate + 1 / 288) then
              -- 1.5分后没到限制
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
