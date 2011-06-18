package com.test.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 接收从渠道接入网关返回的消息
 * @author：wangjiang    
 * @since�?011-4-28 上午10:41:47 
 * @version:
 */
public class MinaDBCoreGWHandler2 extends IoHandlerAdapter{
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("客户端有异常 : ");
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("客户端发送消息成功: ");
	}

	private static Log logger = LogFactory.getLog(MinaDBCoreGWHandler2.class);
	
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("客户端 打开incomming client(DB) : " + session.getRemoteAddress());
	}
	// 当一个客户端关闭�?
	@Override
	public void sessionClosed(IoSession session) {
		System.out.println("客户端关闭 one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达�?
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String s = (String) message;
		System.out.println("已收到服务器返回信息:" +  s);
		

	}

}
