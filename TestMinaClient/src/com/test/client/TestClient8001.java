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
public class TestClient8001 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File destDir = new File("D:\\http\\");
		File[] a = destDir.listFiles();

		for (int i = 0; i < a.length; i++) {
			File f = a[i];
			String filename = f.getName();
			if (filename.endsWith(".jpg") || filename.endsWith(".html")) {
				f.delete();
			}
		}

		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler8001());
		connector.setConnectTimeoutMillis(15000);
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 5020));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		String order = "JB011110250931268401";
		String hf = "HF011110250931268401";
		String accnum = "18651770155";
		for (int i = 0; i < 20; i++) {
			String req = "comm=8001&version=1.0&agentid=weisd&ordersource=2&" + "hforderid=" + order + "&hfserialid=" + hf + i + "&accnum=" + accnum
					+ "&amount=3000&channelid=7181&ispid=0&servicetype=0&provinceid=20&citycode=0754&cardnum=&cardpwd=";
			session.write(req);
		}

	}

}
