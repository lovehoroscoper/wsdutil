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
	public static final String HEARTBEATREQUEST = "HEARTBEAT_REQUEST";
	private static final String HEARTBEATRESPONSE = "HEARTBEAT_RESPONSE";

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
//		String req = (String) message;
//		
//		logger.info(" 服务器收到消息--------------------：" + req);
//		
////		session.write("HEARTBEAT_RESPONSE");
//		session.write("技嘉科技");
		//session.close(false);

	}
}
