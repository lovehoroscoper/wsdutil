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

		String ss = "";
		
		Random random = new Random();
		String serialid = "1234567890123456";
        String number = new Integer(random.nextInt(10)).toString();
        String req="";


		session.write(req);
		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}


}
