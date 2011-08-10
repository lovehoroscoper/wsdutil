CREATE OR REPLACE PROCEDURE SP_M_MANUALDEALORDER_LOCK(v_opt_type       IN NUMBER, --操作类型 0：锁定 1：解锁
                                                       v_isjudge_status in number, --是否判断状态 0：判断 1：不判断
                                                       v_hf_serialid    IN VARCHAR2,
                                                       v_opt_userid     IN VARCHAR2,
                                                       v_res            OUT VARCHAR2,
                                                       v_desc           OUT VARCHAR2,
                                                       v_lock_userid    OUT VARCHAR2) AS
  l_count        NUMBER;
  l_order_status NUMBER;
  /******************************************************************************
  产品名称：话费充值系统
  产品版本：V1
  模块名称：数据库脚本- 人工订单锁定
  模块版本：1.0
  编译环境：ORACLE10g
  添加人员：wjjava
  功能描述：锁定当前处理的订单，防止多人同时处理同一笔订单
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
    RETURN;
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
