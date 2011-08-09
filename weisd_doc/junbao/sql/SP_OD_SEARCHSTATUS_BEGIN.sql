CREATE OR REPLACE PROCEDURE SP_OD_SEARCHSTATUS_BEGIN(v_pageNo    number, --查询页码
                                                     v_pageSize  number, --每页显示大小
                                                     v_status    in VARCHAR2, --要获取的状态
                                                     v_maxcnt    number, --查询次数阀值
                                                     v_resultCur out HF_CURSOR.CURSOR_TYPE) --获取记录集合
 is
  /************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本-获取需要查询充值状态的数据信息
  模块版本：1.0.0.0
  编译环境：ORACLE9i，ORACLE10g
  修改人员：weisd
  修改日期：2011-5-26
  修改内容：
  功能描述：v1：查询出超时的，并且 时间不超过限制时间，没超过查询次数，
            v2 weisd 修改 补充正在查询的，但是已经1个小时都还没结果的

  ************************************************************/
  l_pageNo   number(10);
  l_pageSize number(10);
  l_status   VARCHAR2(10);
BEGIN

  l_status := v_status;
  if v_pageSize is null or v_pageSize = 0 then

    l_pageSize := 10;

  else

    l_pageSize := v_pageSize;

  end if;

  OPEN v_resultCur FOR
  ---下次查询时间 需要补充
    select hf_serialid, --订单号
           hf_orderid, --前台支付订单号
           channel_serialid,
           send_serialid,
           hf_status,
           hf_ispid,
           hf_acchannel,
           req_accnum
      from (select f.hf_serialid,
                   f.hf_orderid,
                   f.channel_serialid,
                   f.send_serialid,
                   f.hf_status,
                   f.hf_ispid,
                   f.hf_acchannel,
                   f.req_accnum,
                   c.channel_pri
              from OD_FULLNOTE f, OD_ORDERINFO o, CH_CHANNELINFO c
             where f.hf_orderid = o.req_orderid
               and f.hf_acchannel = c.channel_id
               and f.hf_status = to_number(l_status) --为 4超时
               and o.charge_status not in (0,4,5) --订单状态不能为最终状态的
               and f.trycount < 8  --查询限制 v_maxcnt 固定了
               -- and o.charge_linetime > sysdate --必须小于要求完成时间 在查询结果中有限制
               ---这里需要补充一个充值阀值----
               and not exists
                   (select 1
                            from OD_PHONECHARGEMONEY tp
                           where tp.hf_serialid = f.hf_serialid
                             and (tp.qm_status = 2
                                 or
                                 tp.hf_status = 0
                                 or
                                 tp.qm_trycount > 8
                                 )
                          )
             order by c.channel_pri , o.charge_linetime asc, o.charge_finishtime asc
            )
     where rownum <= l_pageSize;
  --close v_resultCur; -- 不能关闭
END;
