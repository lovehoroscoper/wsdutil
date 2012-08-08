package com.test.client.v2;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.junbao.hf.utils.common.HttpSignServiceV2;
import com.junbao.hf.utils.common.KeyedDigestMD5;
import com.test.client.v2.handler.MinaDBCoreGWHandler8003;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient8003_CZ {


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
		connector.setHandler(new MinaDBCoreGWHandler8003());
		connector.setConnectTimeoutMillis(15000);

		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
		String  dateTime= simpleDate.format(new Date());
		
		String comm = "8003";
		String agentid = "DE201107261541540282";
		String ordersource = "1";
		String amount = "100";
		String mobilenum = "";
		String saleserialid = "ECZ" + dateTime;
		String orderid = "";
		String hforderid = "";
		
		mobilenum = "15201382440";
		orderid = "OD201204230949511664";
		hforderid = "JB011204230949511624";


//		String comm = "8003";
//		String agentid = "DE201107261541540282";
//		String ordersource = "1";
//		String mobilenum = "15201381000";
//		String amount = "100";
//		String orderid = "OD201204161534191484";
//		String hforderid = "JB011204161534201443";
//		String saleserialid = "ECZ" + dateTime;
		
//		String comm = "8003";
//		String agentid = "DE201107261541540282";
//		String ordersource = "1";
//		String mobilenum = "15201381220";
//		String amount = "100";
//		String orderid = "OD201204161534411486";
//		String hforderid = "JB011204161534421445";
//		String saleserialid = "ECZ" + dateTime;
		
//		String comm = "8003";
//		String agentid = "DE201107261541540282";
//		String ordersource = "1";
//		String mobilenum = "15201381000";
//		String amount = "100";
//		String orderid = "OD201204161641461499";
//		String hforderid = "JB011204161641461458";
//		String saleserialid = "ECZ" + dateTime;
//		
//		mobilenum = "15201381000";
//		hforderid = "JB011204161644441462";
//		orderid = "OD201204161644431503";
		
//		mobilenum = "15201382220";
//		hforderid = "JB011204171810581518";
//		orderid = "OD201204171810581558";
		
		
		String req_new = "comm=" + comm +
				"&agentid=" + agentid + 
				"&ordersource=" + ordersource + 
				"&mobilenum=" + mobilenum + 
				"&amount=" + amount + 
				"&orderid=" + orderid + 
				"&hforderid=" + hforderid + 
				"&saleserialid=" + saleserialid;
		
		
		String req_new_bb = HttpSignServiceV2.creatHttpParamLineString(req_new, "&", "verifystring", "123456");
		
		
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.79", 9001));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.151", 9001));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		session.write(req_new_bb);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---------------------------------------");
	}

}
