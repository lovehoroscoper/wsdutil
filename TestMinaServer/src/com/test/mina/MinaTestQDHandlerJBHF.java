package com.test.mina;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.CheckUtil;
import com.junbao.hf.utils.common.DateUtils;
import com.junbao.hf.utils.common.HttpSignServiceV2;

/**
 * 根据手机号码结尾做判断 如果是0则成功，1失败 其他正在处理
 * 
 * @author Administrator
 * 
 */
public class MinaTestQDHandlerJBHF extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(MinaTestQDHandlerJBHF.class);

	public static int count = 1;

	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
//		充值：0成功、1失败、其他正在处理
//		查询：9成功、8失败、7正在处理、6部分成功，其他结果未知
		
		String req = (String) message;
		if (null == req || "".equals(req.trim()) || "HEARTBEAT_REQUEST".equals(req.trim())) {
			session.write("HEARTBEAT_RESPONSE");
		} else {
			logger.info("接收充值信息0则成功，1失败其他正在处理:" + req);
			String comm = CheckUtil.getSubStringStartsWith(req, "comm", "=", "&");
			String accnum = CheckUtil.getSubStringStartsWith(req, "accnum", "=", "&");
			String res = "";
			String resultno = "";
			if ("8001".equals(comm)) {
				String n2 = accnum.substring(accnum.length() - 1, accnum.length());
				if ("0".equals(n2)) {
					resultno = "0000";
				} else if ("1".equals(n2)) {
					resultno = "1006";
				} else {
					resultno = "2006";
				}
				String ss = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
				String channelserialid = "QD" + ss;
				String core_resultno = resultno;
				String core_errorcode = resultno;
				String core_resultmsg = "";
				String core_balance = "";
				String core_finishmoney = "";
				String core_sendserialid = "SD" + ss;
				res = MessageResponseDone.createResultMsg(req, channelserialid, core_resultno, core_errorcode, core_resultmsg, core_balance, core_finishmoney, core_sendserialid);
			} else if ("8002".equals(comm)) {
				String n3 = accnum.substring(accnum.length() - 1, accnum.length());
				String status = "2";
				if ("9".equals(n3)) {// 倒数2位
					resultno = "0000";
					status = "0";
				} else if ("8".equals(n3)) {
					resultno = "0000";
					status = "3";
				} else if ("7".equals(n3)) {
					resultno = "0000";
					status = "2";
				} else if ("6".equals(n3)) {
					resultno = "0000";
					status = "6";
				} else {
					resultno = "9980";
				}
				String core_resultno = resultno;
				String core_chargestatus = status;
				String core_finishmoney = "";
				String core_channelserialid = "";
				String core_sendserialid = "";
				String core_errorcode = resultno;
				String core_resultmsg = "";
				res = MessageResponseDone.createResultMsgSearch(req, core_resultno, core_chargestatus, core_finishmoney, core_channelserialid, core_sendserialid, core_errorcode, core_resultmsg);

			} else {
				res = "error";
			}
			
			Thread.sleep(1500);
			
			String messageDigest = HttpSignServiceV2.creatHttpParamLineString(res.trim(), "&", "verifystring", "123456");
			logger.info("返回信息:" + messageDigest);
			session.write(messageDigest);
		}
	}

	public String getFormatDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(new Date());
	}

	public String getFormatDate2() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(new Date());
	}

	public String result(String ss) {
		String end = ss.substring(ss.length() - 1, ss.length());
		int a = Integer.valueOf(end);
		String r = "";
		if (a == 0) {
			r = "0000";// <!-- 渠道返回充值成功 -->
		} else if (a == 1) {
			r = "1006";
		} else {
			r = "2006";
		}
		return r;
	}

}
