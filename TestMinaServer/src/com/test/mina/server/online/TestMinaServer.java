package com.test.mina.server.online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.test.mina.server.MinaServerHandler;

public class TestMinaServer {

	private static Logger logger = Logger.getLogger(MinaServerHandler.class);

	public static void main(String[] args) {
		TestMinaServer s = new TestMinaServer();
		s.startListener();
	}

	public boolean startListener() {
		int port = 9992;
		boolean isSuc = false;
		try {
			SocketAcceptor acceptor = new NioSocketAcceptor();
			acceptor.setReuseAddress(true);
			acceptor.getSessionConfig().setReadBufferSize(1028 * 2);
			acceptor.getSessionConfig().setWriteTimeout(10);

			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

			TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("UTF-8"));
			textLineCodecFactory.setDecoderMaxLineLength(4000);
			chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));

			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
			acceptor.setHandler(new MinaServerHandlerHUA());
			acceptor.bind(new InetSocketAddress(port));

			logger.info("服务器启动" + port + ")");
		} catch (IOException e1) {
			isSuc = false;
			logger.error("启动()异常。", e1);
		}
		return isSuc;
	}

}
