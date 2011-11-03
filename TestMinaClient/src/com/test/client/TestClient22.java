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
public class TestClient22 {


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
			ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
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
	
	/**
	 * @param args
	 */
	public static void main_2(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory =  new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler2());
		connector.setConnectTimeoutMillis(15000);
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.164", 5002));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9731));
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.161", 9999));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.122", 6010));

		cf.awaitUninterruptibly();
		
		

		IoSession session = cf.getSession();
//
////		String req = "comm=8001&version=1.0&hfserialid=JB011105301302347908&accnum=13588771142&amount=1000&provinceid=1&ispid=1";
//	//	String req = "comm=8001&version=1.0&hfserialid=JB011105301302347990&accnum=13989853714&amount=1000&provinceid=1&ispid=1";
//	//	String req2 = "comm=8001&version=1.0&hfserialid=JB011105301302347991&accnum=15024467050&amount=1000&provinceid=1&ispid=1";
//
////		session.write(req);
////		session.write(req2);
//		String req3 = "comm=8001&version=1.0&onlineid=107034&agentid=DE201108091429020545&ordersource=1&orderid=OD201108121415004600&mobilenum=13516151450&chargeamount=100.0&payamount=98.8&ordertime=20110812141500&mark=&receivetime=20110814101400";
////		String req3 = "comm=8001&hfserialid=JB011105301302247876&accnum=13888898996&payamount=11&ispid=1&citycode=0871";
//		
//		//core
////		String hfs = "";
////		String money = "";
////		String res = "";
////		
////		 hfs = "HF011108311324569653";
////		 money = "5000";
////		 res = "1009";
////		 hfs = "HF011108311152429635";
////		 money = "10000";
////		 res = "1006";
////		String hfs = "HF011108311055179616";
////		String money = "5000";
////		String res = "0000";
		
//		String hfs = "HF011109021612011380";
//		String money = "5000";
//		String res = "0000";
//		String req3 = "comm=8101&version=1.0&hfserialid=" +hfs+ "&channelserialid=&sendserialid=&dealtime=&dealamount=" + money+ "&resultno=" +res + "&resultmsg=";
////		
//		String req = "comm=8101&version=1.0&hfserialid=HF011110201723046470&channelserialid=CMC2011102100000033&dealtime=2011-10-21 00:36:06&dealamount=5000&sendserialid=12201110210036061812&resultno=2006";
		String req = "comm=8101&version=1.0&hfserialid=HF011110201723046470&channelserialid=GGF&dealtime=2011-10-21 00:36:06&dealamount=5000&sendserialid=&resultno=2006";
		
		session.write(req);
//		session.write(req3);
		
		
//		IoSession session = cf.getSession();
//		for (int i = 100; i < 101; i++) {
//			String req3 = "comm=8001&version=1.0&hfserialid=HF011135306303135" + i + "&accnum=13588749" + i + "&amount=1000&provinceid=1&ispid=1";
//			session.write(req3);
//			session.close(false);
//		}
		System.out.println("---------------------------------------");
		
	}

}
