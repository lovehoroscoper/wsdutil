package com.test.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 话费充�?核心服务 1、接收来自收单平台的请求并处理（如：下单、结果查询�?充正�? 2、接收来自渠道接入网�?
 * 
 * @author：wangjiang
 * @since�?011-4-28 下午02:35:28
 * @version:1.0
 */
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

			TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
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
