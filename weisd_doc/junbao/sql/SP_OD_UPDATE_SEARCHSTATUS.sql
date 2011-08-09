CREATE OR REPLACE PROCEDURE SP_OD_UPDATE_SEARCHSTATUS(v_hfserialid   in VARCHAR2, --话费流水号
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
  ************************************************************/
  l_count           NUMBER(10); -- 条数
  l_status          NUMBER(2);
  l_provinceid      number(2);
  l_finishtime      DATE;
  l_ispid           number(1);
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

  v_result        := '11111';
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

  if v_status in (1, 4) then
    --暂时不提供功能
    v_restatus := v_status;
    v_result   := '11111';
    return;
  end if;

  select count(1)
    into l_count
    from OD_PHONECHARGEMONEY p
   where p.hf_serialid = v_hfserialid;

  if l_count = 0 then
    --插入查询记录，状态为2
    select s.province_id, f.finish_time, f.hf_ispid
      into l_provinceid, l_finishtime, l_ispid
      from OD_FULLNOTE f, sys_accsegment s
     where f.hf_serialid = v_hfserialid
       and s.acc_segment = substr(f.req_accnum, 0, 7);

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
      (v_hfserialid,
       l_provinceid,
       l_finishtime,
       sysdate,
       null,
       2, --都是系统自动查询，故为2
       0,
       0,
       l_ispid,
       1);
    commit;
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

  if v_status = 2 then
    --修改为正在查询，修改次数
    update OD_PHONECHARGEMONEY p
       set p.qm_begintime = sysdate,
           p.qm_status    = v_status,
           p.qm_trycount  = p.qm_trycount + 1
     where p.hf_serialid = v_hfserialid;

    update OD_FULLNOTE f
       set f.trycount = f.trycount + 1
     where f.hf_serialid = v_hfserialid;

    v_restatus := v_status;
    v_isupdate := l_isupdate;
    v_result   := '00000';
    commit;
    return;
  end if;

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

    if l_search_count >= 8 or l_time >= 0 then
      --如果已经到达8次了 或者已经 >= 要求完成时间
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
    v_result   := '00000';
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
        v_result        := '00000';

      elsif l_result = '1111' then
        l_warninfo := '查询充值记录' || v_hfserialid || '成功,修改充值结果处理最终状态' ||
                      v_chargestatus || '失败' || l_result;
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
        v_restatus := v_chargestatus;
        v_isupdate := l_isupdate;
        v_result   := '33333'; --查询到充值记录最终状态，但是修改数据库订单之类失败

      else
        l_warninfo := '查询充值记录' || v_hfserialid ||
                      '成功,但是充值状态不是最终结果,不对充值记录做修改：' || v_chargestatus ||
                      ',只修改查询次数';
        sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
        v_restatus := v_chargestatus;
        v_isupdate := l_isupdate;
        v_result   := '00000'; --查询到充值记录最终状态，但是修改数据库订单之类失败

      end if;

    else
      l_warninfo := '查询充值记录' || v_hfserialid || '成功,但是记录状态不为超时,而是' ||
                    l_hf_status || ',l_isupdate:' || l_isupdate;
      sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
      --不是超时的,且已经更改过订单状态的 返回
      v_restatus := v_status;
      v_isupdate := l_isupdate;
      v_result   := '00000';
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
    v_result   := '11111';
    ROLLBACK;
    --v_status||v_chargestatus||v_dealtime||v_dealamount||v_errorcode||l_hf_status||l_result
    l_warninfo := '查询充值记录' || v_hfserialid || '异常,' || v_status || ',' ||
                  v_chargestatus || ',' || v_dealtime || ',' ||
                  v_dealamount || ',' || v_errorcode || ',' || l_hf_status || ',' ||
                  l_result;
    sp_sys_dberrorinfo_create(1, '系统', '', l_warninfo, '', '');
    commit;
END;
