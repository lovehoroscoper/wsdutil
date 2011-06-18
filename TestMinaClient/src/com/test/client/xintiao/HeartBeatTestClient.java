/**   
 * @Title: HeartBeatTestClient.java 
 * @Package com.underdark.March 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Minsc Wang ys2b7_hotmail_com   
 * @date 2010-3-14 下午03:17:27 
 * @version V0.9.0 
 */
package com.test.client.xintiao;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: HeartBeatTestClient
 * @Description: MINA心跳测试客户端,仅供测试 client空闲时每隔N秒向服务器发送心跳包，如返回超时，发出提示
 * @author Minsc Wang ys2b7_hotmail_com
 * @date 2011-3-7 下午02:49:14
 * 
 */
public class HeartBeatTestClient {

	private static final Logger LOG = LoggerFactory
			.getLogger(HeartBeatTestClient.class);

	private static final int PORT = 9999;
	/** 30秒后超时 */
	private static final int IDELTIMEOUT = 30;
	/** 15秒发送一次心跳包 */
	private static final int HEARTBEATRATE = 15;
	/** 心跳包内容 */
	private static final String HEARTBEATREQUEST = "HEARTBEAT_REQUEST";
	private static final String HEARTBEATRESPONSE = "HEARTBEAT_RESPONSE";
	
//	private static final String IPADDRESS = "192.168.2.201";
	private static final String IPADDRESS = "127.0.0.1";

	private static NioSocketConnector connector;
	private static IoHandler handler = new HeartBeatClientHandler();

	public static void main(String[] args) {
		connector = new NioSocketConnector();
//		connector.getFilterChain().addLast("log", new LoggingFilter());
		connector.getFilterChain().addLast("code",
				new ProtocolCodecFilter(new TextLineCodecFactory()));
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
				IDELTIMEOUT);
		/** 主角登场 */

		KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
//		KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepAliveRequestTimeoutHandlerImpl();
//		KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
//				IdleStatus.BOTH_IDLE, heartBeatHandler);
		KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory);
		/** 是否回发 */
		heartBeat.setForwardEvent(true);
		/** 发送频率 */
		heartBeat.setRequestInterval(HEARTBEATRATE);
		connector.getSessionConfig().setKeepAlive(true);
		connector.getFilterChain().addLast("heartbeat", heartBeat);

		/** *********** */
		connector.setHandler(handler);
		connector.connect(new InetSocketAddress(IPADDRESS, PORT));
		LOG.info("客户端已连接上！");
	}

	/***
	 * @ClassName: KeepAliveMessageFactoryImpl
	 * @Description: 内部类，实现心跳工厂
	 * @author Minsc Wang ys2b7_hotmail_com
	 * @date 2011-3-7 下午04:09:02
	 * 
	 */
	private static class KeepAliveMessageFactoryImpl implements
			KeepAliveMessageFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getRequest
		 * (org.apache.mina.core.session.IoSession)
		 */
		@Override
		public Object getRequest(IoSession session) {
			LOG.info("返回预设语句" + HEARTBEATREQUEST);
			/** 返回预设语句 */
			return HEARTBEATREQUEST;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getResponse
		 * (org.apache.mina.core.session.IoSession, java.lang.Object)
		 */
		@Override
		public Object getResponse(IoSession session, Object request) {
			LOG.info("得到返回");
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isRequest
		 * (org.apache.mina.core.session.IoSession, java.lang.Object)
		 */
		@Override
		public boolean isRequest(IoSession session, Object message) {
			LOG.info("是否是心跳包: " + message);
			if(message.equals(HEARTBEATREQUEST))
				return true;
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isResponse
		 * (org.apache.mina.core.session.IoSession, java.lang.Object)
		 */
		@Override
		public boolean isResponse(IoSession session, Object message) {
			LOG.info("是否是心跳包: " + message);
			if(message.equals(HEARTBEATRESPONSE))
				return true;
			return false;
		}

	}

	/***
	 * @ClassName: KeepAliveRequestTimeoutHandlerImpl
	 * @Description: 当心跳超时时的处理，也可以用默认处理 这里like
	 *               KeepAliveRequestTimeoutHandler.LOG的处理
	 * @author Minsc Wang ys2b7_hotmail_com
	 * @date 2011-3-7 下午04:15:39
	 * 
	 */
	private static class KeepAliveRequestTimeoutHandlerImpl implements
			KeepAliveRequestTimeoutHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler#
		 * keepAliveRequestTimedOut
		 * (org.apache.mina.filter.keepalive.KeepAliveFilter,
		 * org.apache.mina.core.session.IoSession)
		 */
		@Override
		public void keepAliveRequestTimedOut(KeepAliveFilter filter,
				IoSession session) throws Exception {
			((Logger) LOG).info("心跳超时！");
		}

	}
}
