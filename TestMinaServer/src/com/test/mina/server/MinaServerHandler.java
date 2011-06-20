package com.test.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * 
 */
public class MinaServerHandler extends IoHandlerAdapter {
	private static Logger logger = Logger.getLogger(MinaServerHandler.class);

	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("incomming client(DB) : " + session.getRemoteAddress());
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		logger.debug("one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String req = (String) message;
		logger.info(" 服务器收到消息--------------------：" + req);
		
		session.write("服务器已经收到消息，现在返回客户端");

	}
}
