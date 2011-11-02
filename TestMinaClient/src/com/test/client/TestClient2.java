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
//		ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.2.183", 6005));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.2.232", 9999));
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.94", 9001));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.2.175", 6003));

		cf.awaitUninterruptibly();

		IoSession session = cf.getSession();

		String ss = "";
		
		Random random = new Random();
		String serialid = "1234567890123456";
        String number = new Integer(random.nextInt(10)).toString();
        String req="222222";
        String req3="333333";

        //   渠道修改
        //yibaimi
        //  String req2 = "comm=8001&version=1.0&onlineid=100&agentid=weisd&ordersource=2&orderid=aa3aaa445aa59a7&mobilenum=15201386006&chargeamount=50&payamount=0&ordertime=20110906181119&mark=test&receivetime=20110907100520";
          //esales
//        String req2 = "comm=8001&version=1.0&onlineid=106998&agentid=weisd111111111111111&ordersource=1&orderid=aaaaa59a722222222322&mobilenum=15201386011&chargeamount=50.0&payamount=50.0&ordertime=20110907095350&mark=test&receivetime=20110907101230";
        String req2 = "comm=8002&version=1.0&onlineid=106998&agentid=weisd111111111111111&ordersource=1&orderid=aaaaa59a722222222322&mobilenum=15201386011&chargeamount=50.0&payamount=50.0&ordertime=20110907095350&mark=test&receivetime=20110907101230&hforderid=JB011109071010532368";
//        String req2 = "comm=8001&version=1.0&onlineid=106998&agentid=DE201107261509020281&ordersource=1&orderid=OD201109070956068442&mobilenum=15201386007&chargeamount=50.0&payamount=49.5&ordertime=20110907095604&mark=test&receivetime=20110907095604";
        
//        String channel = "comm=8009&version=1.0&resultno=9988&errorcode=9988&errormsg=关闭关闭关闭关闭关闭关闭关闭关闭关闭关闭关闭关闭&channelkey=,7001,7002,&optname1=weisd&opttype=1&opttime=20110622161616";
//        String channel = "HEARTBEAT_REQUEST222";
        
//        String channel = "comm=8101&version=1.0&resultno=0000&hfserialid=HF011106211532374832&channelserialid=1111&sendserialid=23232323&dealtime=&dealamount=5000";

//        map.put("", hfserialid);
//		map.put("", channelserialid);
//		map.put("", sendserialid);			
//		map.put("", dealtime);
//		map.put("", dealamount);
//		map.put("resultno", "");

        
//		session.write(req);
//		session.write(req3);
		session.write(req2);
//		session.close(false);
		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}


}
