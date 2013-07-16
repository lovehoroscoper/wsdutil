package com.test.mina.server;

import java.net.SocketAddress;

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
		String req = (String) message;
		
		long l = session.getId();
//		session.get
		
		ClientCacheHelp.getInstance().putClentSession("8001", "", session);
		
		//
		logger.info(" 服务器收到消息--------------------：" + ClientCacheHelp.getInstance().getClientCacheMap());
//		logger.info(" 服务器收到消息--------------------：" + req);
//		//
//		
//		SocketAddress ip = session.getRemoteAddress();
//		logger.info(" 服务器收到消息--------------------：" + ip.toString());
//		
//		// // session.write("HEARTBEAT_RESPONSE");
//		StringBuffer sb1 = new StringBuffer();
////		StringBuffer sb2 = new StringBuffer();
//		String s1 = "33333333333333333333";
////		String s2 = "22222222222222222222q";
//		for (int i = 0; i < 5; i++) {
//			sb1.append(s1);
////			sb2.append(s2);
//		}
//		
//		sb1.append("Q");
//		logger.info(sb1.length());
////		logger.info(sb2.length());
//		session.write(sb1.toString());
//		Thread.sleep(10);
//		session.write(sb1.toString());
		
//		session.write(sb2.toString());
		// session.close(false);

	}
}
