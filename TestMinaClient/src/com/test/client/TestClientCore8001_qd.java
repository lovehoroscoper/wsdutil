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
public class TestClientCore8001_qd {

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

			int c = (int) (Math.random() * 100 + 10);
			if(c > 99){
				c = c - 10;
			}
			
			
//			String onlineid = "108844";//浙江衢州电信手机10元5分
//			String mobilenum = "057055555555";//商城
//			String chargeamount = "10";
//			String payamount = "10";
//			String ordersource = "5";
			
			
//			String mobilenum = "15201386005";// 电话号码江苏
//			String onlineid = "108788";
//			String chargeamount = "10";
//			String payamount = "10";
//			String ordersource = "5";
			
			String mobilenum = "0571849612" + c;// 电话号码江苏
			String onlineid = "108844";
			String chargeamount = "10";
			String payamount = "10";
			String ordersource = "5";
			
//			String mobilenum = "186517701" + c;// 电话号码江苏
//			String mobilenum = "057022222" + c;// 电话号码江苏
//			String onlineid = "108844";
//			String chargeamount = "1";
//			String payamount = "1";
//			String ordersource = "1";
			
			
			
			String agentid = "DE201108091428260543";
			String orderid = ordertime + i;

			
//			String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
//			
//			String req = "comm=8010&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
			
			String req = "comm=8001&version=1.0&agentid=DE201108091428260543&ordersource=5&hforderid=JB011202081623462853&hfserialid=" + orderid + "&accnum=" + mobilenum + "&amount=1000&channelid=10220001&ispid=2&servicetype=1&provinceid=12&citycode=0571&cardnum=&cardpwd=";
//			String req = "comm=8001&version=1.0&agentid=DE201108091428260543&ordersource=5&hforderid=JB011202081623462853&hfserialid=HF011202081625197913&accnum=057087801558&amount=1000&channelid=10220001&ispid=2&servicetype=1&provinceid=12&citycode=0570&cardnum=&cardpwd=";
//			String req = "comm=8001&version=1.0&agentid=DE201108091428260543&ordersource=5&hforderid=JB011202081623462853&hfserialid=HF011202081625197913&accnum=15301386005&amount=1000&channelid=10220001&ispid=2&servicetype=0&provinceid=12&citycode=0576&cardnum=&cardpwd=";
//			String req = "comm=8001&version=1.0&agentid=DE201108091428260543&ordersource=5&hforderid=JB011202081729082886&hfserialid=HF011202081730177958&accnum=18939778817&amount=1000&channelid=10220001&ispid=2&servicetype=0&provinceid=2&citycode=021&cardnum=&cardpwd=";
			
			ConnectFuture cf = null;
			
			if(a == 1){
				cf = connector.connect(new InetSocketAddress("172.25.25.164", 6005));
//				cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
				a = 0;
			}else{
//				cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
				cf = connector.connect(new InetSocketAddress("172.25.25.164", 6005));
//				cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
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
