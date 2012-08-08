package com.test.client.jbcz.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaHandler8001_JBCZ extends IoHandlerAdapter {

	// private static Logger logger =
	// Logger.getLogger(MinaHandler8001_JBCZ.class);
	private static Logger logger = Logger.getLogger("verifycorelog");

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	public void sessionOpened(IoSession session) throws Exception {
	}

	// 当一个客户端关闭�?
	@Override
	public void sessionClosed(IoSession session) {
	}

	// 当前置发送的消息到达�?
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String s = (String) message;
		logger.info("已收到返回:" + s);
		// boolean validFlag = HttpSignServiceV2.validResponseString(s.trim(),
		// "&", "verifystring", "123456");
		// logger.info("验证是否通过:" + validFlag);

	}

}
