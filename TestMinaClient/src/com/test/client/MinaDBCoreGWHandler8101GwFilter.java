package com.test.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;


public class MinaDBCoreGWHandler8101GwFilter extends IoHandlerAdapter{
	private static Log logger = LogFactory.getLog(MinaDBCoreGWHandler8101GwFilter.class);
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		
	//	logger.error("客户端有异常 : ",cause);
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		//System.out.println("客户端发送消息成功: ");
	}

	
	
	public void sessionOpened(IoSession session) throws Exception {
	//	System.out.println("客户端 打开incomming client(DB) : " + session.getRemoteAddress());
	}
	// 当一个客户端关闭�?
	@Override
	public void sessionClosed(IoSession session) {
	//	System.out.println("客户端关闭 one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达�?
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String s = (String) message;
		logger.info(s);	
		logger.info("----MinaDBCoreGWHandler8101GwFilter----已收到服务器返回信息:" +  s);	

		

	}

}
