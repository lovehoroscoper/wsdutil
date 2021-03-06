/**   
 * @Title: HeartBeatClientHandler.java 
 * @Package com.underdark.March 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Minsc Wang ys2b7_hotmail_com   
 * @date 2010-3-14 下午03:21:13 
 * @version V0.9.0 
 */
package com.test.client.xintiao;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: HeartBeatServerHandler
 * @Description: 心跳测试客户端消息处理
 * @author Minsc Wang ys2b7_hotmail_com
 * @date 2011-3-7 下午02:49:14
 * 
 */
public class HeartBeatClientHandler extends IoHandlerAdapter {

	private static final Logger LOG = LoggerFactory
			.getLogger(HeartBeatClientHandler.class);

	private static final String START = "心跳消息测试开始";

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		LOG.info("捕获异常");
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		super.messageReceived(session, message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOG.info("session被关闭");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		LOG.info("session正空闲" + status.toString());
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOG.info("session连接被打开");
		//LOG.info("leaf:  " + START);
		//session.write(START);
	}
}
