package com.test.client;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient8002_V2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {



		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler8002());
		connector.setConnectTimeoutMillis(15000);
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		String req = "agentid=DE201108091428260543&comm=8002&hforderid=&onlineid=&orderid=SC201202141719091&ordersource=5&receivetime=20120214172623&version=1.0&verifystring=35d778d619d8f6900a7db90c2bfcee53";
		session.write(req);


	}

}
