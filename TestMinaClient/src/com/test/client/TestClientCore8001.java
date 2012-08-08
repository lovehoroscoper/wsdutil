package com.test.client;

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

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClientCore8001 {

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
		connector.setHandler(new MinaDBCoreGWHandler8001());
		connector.setConnectTimeoutMillis(15000);

		
		String accnum = "18651770155";
		
		int a = 1;
		for (int i = 0; i < 1; i++) {
			
			String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
//			String mobilenum = "11111111111";// 电话号码
//			String mobilenum = "12201386005";// 电话号码
//			String mobilenum = "15201386005";// 电话号码
//			String mobilenum = "15984991111";// 电话号码
			
//			int c = (int) (Math.random() * 5000 + 1000);
//			
//			if(c > 9999){
//				c = c - 1000;
//			}
			int c = (int) (Math.random() * 100 + 10);
			if(c > 99){
				c = c - 10;
			}
			
			String mobilenum = "152013860" + c;// 电话号码
			String onlineid = "108116";
			String agentid = "DE20=110809=142826=0543";
			String orderid = ordertime + i;
			String chargeamount = "30";
			String payamount = "28.8";
			String ordersource = "2";
			if("4".equals(ordersource)){
//				onlineid = "108286";
				onlineid = "108701";//q
			}else{
				onlineid = "107620";
			}
			
//			String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
//			String req = "agentid=DE201108091428260543&comm=8002&hforderid=&onlineid=&orderid=WD201202171336580&ordersource=5&receivetime=20120217133818&version=1.0&verifystring=794ad5a1d1bfcabbf4390295480e7280";
			String req = "agentid=  &comm=8002&hforderid=&onlineid=&orderid=WD201202171336580&ordersource=5&receivetime=20120217134512&version=1.0&verifystring=65685fe44c664b7f46495c78444eacfa";
			
			ConnectFuture cf = null;
			
			if(a == 1){
//				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
				cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
				a = 0;
			}else{
//				cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
				cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
				a = 1;
			}
			
			cf.awaitUninterruptibly();
			IoSession session = cf.getSession();
			System.out.println(req);
			session.write(req);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
}
