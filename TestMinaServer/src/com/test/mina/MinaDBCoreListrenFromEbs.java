package com.test.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class MinaDBCoreListrenFromEbs {

	private static MinaDBCoreListrenFromEbs minaDBCoreListrenFromEbs;
	private static Log log = LogFactory.getLog(MinaDBCoreListrenFromEbs.class);

	private MinaDBCoreListrenFromEbs() {

	}

	public static MinaDBCoreListrenFromEbs getInstance() {
		if (minaDBCoreListrenFromEbs == null || minaDBCoreListrenFromEbs == null) {
			minaDBCoreListrenFromEbs = new MinaDBCoreListrenFromEbs();
		}
		return minaDBCoreListrenFromEbs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean startListener() {
		boolean isSuc = false;
		try {
			// 服务器端绑定的端�?
			int receiveServicePort = Integer.parseInt("9999");
//			int messageQueueMaxValue = Integer.parseInt("100");
//			int messageQueueMinValue = Integer.parseInt("1");
//			int messageTaskDealThreadNum = Integer.parseInt("10");
			SocketAcceptor acceptor = new NioSocketAcceptor();

			// 创建接收数据的过滤器
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

			TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("UTF-8"));
			textLineCodecFactory.setDecoderMaxLineLength(4000);
			chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));

			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

			acceptor.setHandler(new MinaDBCoreEbsHandler());
			acceptor.bind(new InetSocketAddress(receiveServicePort));
			log.info("*********Mina HfCoreDB server is Listing on:= " + receiveServicePort + "*********");

		} catch (IOException e1) {
			isSuc = false;
			e1.printStackTrace();
		}
		return isSuc;
	}

}
