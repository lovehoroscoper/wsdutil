package com.test.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * 
 */
public class MinaServerHandlerGwFilter extends IoHandlerAdapter {


	private static Logger logger = Logger.getLogger(MinaServerHandlerGwFilter.class);
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
		logger.info("--------经过handler---------------" + (String)message);
		logger.info("comm=8001&onlineid=107620&version=1.0&verifystring=c241031a4c6c52dba916e0aa881f5e07");
		logger.info((String)message);

//		session.write("comm=8101&onlineid=107620&resultno=0000");
		session.write("comm=8101&onlineid=107620&resultno=00001&verifystring=f50d64da0217635620e2ea0b9d1bf2d3");
		session.close(false);

	}
	
	
}
