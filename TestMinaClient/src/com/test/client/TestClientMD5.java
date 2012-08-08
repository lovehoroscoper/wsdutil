package com.test.client;

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

import com.hisunsray.commons.res.Config;
import com.junbao.cgw.mina.filter.VerifyGwMsgFilter;

public class TestClientMD5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String configFilePath = "D:\\junbao_newpro\\hfcore3\\resource\\global.properties";
		Config.setConfigResource(configFilePath);

		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));

		// add 2012.01.10 msgValidFilter 必须在获取消息方式myChin的后面
		VerifyGwMsgFilter msgDigestGwFilter = new VerifyGwMsgFilter();
		chain.addAfter("myChin", "msgDigestGwFilter", msgDigestGwFilter);

		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler8101GwFilter());
		// connector.setConnectTimeoutMillis(15000);

		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 6003));
		cf.awaitUninterruptibly();

		IoSession session = cf.getSession();
		// comm=8001&onlineid=107620&version=1.0&verifystring=c241031a4c6c52dba916e0aa881f5e07
		String req = "comm=8001&version=1.0&onlineid=107620";

		session.write(req);

	}

}
