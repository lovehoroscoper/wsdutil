package com.test.mina.server.online;

import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.CheckUtil;

/**
 * 
 * 
 */
public class MinaServerHandlerHUA extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(MinaServerHandlerHUA.class);
	public static final String HEARTBEATREQUEST = "HEARTBEAT_REQUEST";

	public void sessionOpened(IoSession session) throws Exception {
		logger.info("有新连接接入");
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		logger.debug("one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		String reqMsg = (String) message;
		logger.info("--------经过handler---------------[" + reqMsg.length() + "][" + reqMsg + "]");

		String appid = CheckUtil.getSubStringStartsWith(reqMsg, "appid", "=", "&");
		String loginstep = CheckUtil.getSubStringStartsWith(reqMsg, "loginstep", "=", "&");
		String backmsg = "";
		String resultno = "0000";
		String loginstatus = "";
		String imgpath = "";
		if("12".equals(loginstep)){
			imgpath = "http://reg.email.163.com/unireg/call.do?cmd=register.verifyCode&env=467982158687&t=1348204213053";
			imgpath = URLEncoder.encode(imgpath, "UTF-8");
			loginstatus = "no";
			Thread.sleep(3000);
		}else{
			loginstatus = "yes";
			Thread.sleep(3000);
		}
		
		backmsg = getLoginStr("8130", "0", loginstep, appid, resultno, loginstatus, imgpath);
		

		logger.info("返回[" + backmsg.length() + "][" + backmsg + "]");
		session.write(backmsg);

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

		logger.info("--------经过handler----------exceptionCaught-----");

		super.exceptionCaught(session, cause);
	}

	private String getLoginStr(String comm, String logintype, String loginstep, String appid, String resultno, String loginstatus, String imgpath) {
		StringBuffer sb = new StringBuffer();
		sb.append("").append("comm").append("=").append(comm);
		sb.append("&").append("logintype").append("=").append(logintype);
		sb.append("&").append("loginstep").append("=").append(loginstep);
		sb.append("&").append("appid").append("=").append(appid);
		sb.append("&").append("resultno").append("=").append(resultno);
		sb.append("&").append("loginstatus").append("=").append(loginstatus);
		sb.append("&").append("imgpath").append("=").append(imgpath);
		return sb.toString();
	}
}
