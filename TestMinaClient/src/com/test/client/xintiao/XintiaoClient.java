package com.test.client.xintiao;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

/**
 * @desc:
 * 
 * http://mina.apache.org/report/trunk/xref/org/apache/mina/filter/keepalive/KeepAliveFilter.html
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-31 下午11:02:51
 * @version:v1.0
 * 
 */
public class XintiaoClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NioDatagramConnector connector = new NioDatagramConnector();
//		connector.setConnectTimeoutMillis(60000L);
		connector.setConnectTimeoutMillis(15000L);//weisd
		connector.setConnectTimeoutCheckInterval(10000);
		connector.setHandler(new MyIoHandlerAdapter());

		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addLast("keep-alive", new HachiKeepAliveFilterInMina());// 心跳
		// chain.addLast("toMessageTyep", new MyMessageEn_Decoder());
		// 设定这个过滤器将一行一行(/r/n)的读取数据
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));

//		chain.addLast("logger", new LoggingFilter());
//		ConnectFuture connFuture = connector.connect(new InetSocketAddress("127.0.0.1", 9999));
		ConnectFuture connFuture = connector.connect(new InetSocketAddress("127.0.0.1", 9999));
		connFuture.awaitUninterruptibly();
		IoSession session = connFuture.getSession();
		// 发送消息长整型 1000
		IoBuffer buffer = IoBuffer.allocate(8);
		buffer.putLong(1000);
		buffer.flip();
//		session.write(buffer);
		session.write(buffer).awaitUninterruptibly();//weisd
		System.out.println(2);
		// 关闭连接
		session.getCloseFuture().awaitUninterruptibly();
		System.out.println(3);
		connector.dispose();
	}

}

class MyIoHandlerAdapter extends IoHandlerAdapter {
	private static Log logger = LogFactory.getLog(MyIoHandlerAdapter.class);

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

		logger.info(" clint   super.exceptionCaught(session, cause);");
		System.exit(0);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		logger.info(" clint   super.messageReceived(session, message);");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info(" clint   super.messageSent(session, message);");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info(" clint   super.sessionOpened(session);");
	}
}