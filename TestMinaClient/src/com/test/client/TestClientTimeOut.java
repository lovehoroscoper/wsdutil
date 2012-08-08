package com.test.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
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
public class TestClientTimeOut {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory =  new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler2());
		connector.setConnectTimeoutMillis(15000);
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
//		cf.awaitUninterruptibly();
//		
//		IoSession session = cf.getSession();
//		StringBuffer sb = new StringBuffer();
//		sb.append("")
		String ss =		"HF011110250931268401";
	
		
		
		String[] a = ss.split(",");
		
		for (int i = 0; i < a.length; i++) {
			ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.79", 6003));
			cf.awaitUninterruptibly();
			
			IoSession session = cf.getSession();
			String req = "comm=8101&version=1.0&hfserialid=" + a[i] + "&channelserialid=weisdTest" + i + "&dealtime=2011-10-24 15:56:06&dealamount=10000&sendserialid=&resultno=0000";
			session.write(req);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("---------------------------------------");
		//session.close(false);
		
	}
	


}
