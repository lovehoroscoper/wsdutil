package com.test.client;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;


public class MinaDBCoreReturnBackHandler extends IoHandlerAdapter{
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		//super.exceptionCaught(session, cause);
//		User u = (User)session.getAttribute("user");
////		u.getName();
//		System.out.println("客户端有异常--- : "+u.getName());
		System.out.println("客户端有异常time:" + new Date().getTime());
		System.out.println("客户端有异常 : ");
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		//super.messageSent(session, message);
		System.out.println("客户端发送消息成功: ");
	}

	private static Log logger = LogFactory.getLog(MinaDBCoreReturnBackHandler.class);
	
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("客户端 打开incomming client(DB) : " + session.getRemoteAddress());
//		session.write("HEARTBEAT_REQUEST");
//		long ll = session.getLastIoTime();
//		SocketAddress  s = session.getServiceAddress();
//		System.out.println("ll " + ll);
		
//		String ss = "comm=8103&version=1.0&onlineid=1&agentid=weisd&ordersource=1&zhorderid=HF011105241044530434&resultno=2029";
//		System.out.println("start time:" + new Date().getTime());
//		WriteFuture writeResult = session.write(ss);
//		writeResult.awaitUninterruptibly(5000);
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
		
		session.close(false);
		

	}

}
