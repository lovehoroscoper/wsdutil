package com.test.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * @desc:
 * 
 * 
 * 
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Map map = new HashMap();
		
			
		
		
		NioSocketConnector connector = new NioSocketConnector();

		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		connector.setHandler(new MinaDBCoreGWHandler2());
		connector.setConnectTimeoutMillis(15000);
		ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.2.183", 6005));

		cf.awaitUninterruptibly();

		IoSession session = cf.getSession();

		String ss = "comm=8001&version=1.0&onlineid=1&agentid=weisd&ordersource=1&orderid=JB00002&mobilenum=13064524444&chargeamount=100.00&payamount=100.00&ordertime=201105311802&mark=";
		
		Random random = new Random();
		String serialid = "1234567890123456";
        String number = new Integer(random.nextInt(10)).toString();
        String req="comm=8001&provinceid=20&hfserialid="+serialid+"&ispid=2&goodsnum=1&amount=5000&accnum=1899892115"+number+"&traderterm=13388888555&signcode=";//联通充值


		session.write(req);
		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		
//		Map map = new HashMap();
//		
//		for (int i = 0; i < 200; i++) {
//			
//			
//			
//			NioSocketConnector connector = new NioSocketConnector();
//			
//			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
//			TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
//			textLineCodecFactory.setDecoderMaxLineLength(4000);
//			chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
//			connector.setHandler(new MinaDBCoreGWHandler2());
//			connector.setConnectTimeoutMillis(15000);
//			// ConnectFuture cf = connector.connect(new
//			// InetSocketAddress("192.168.1.111", 80));
//			// ConnectFuture cf = connector.connect(new
//			// InetSocketAddress("192.168.2.201", 9001));
//			ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 9999));
//			// ConnectFuture cf = connector.connect(new
//			// InetSocketAddress("61.135.169.105", 9000));
//			
//			cf.awaitUninterruptibly();
//			
//			IoSession session = cf.getSession();
//			
//			String ss = "comm=8001&version=1.0&onlineid=1&agentid=weisd&ordersource=1&orderid=JB00002&mobilenum=13064524444&chargeamount=100.00&payamount=100.00&ordertime=201105311802&mark=";
////		session.write(ss);
//			map.put(i, connector);
//		}
//		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

}
