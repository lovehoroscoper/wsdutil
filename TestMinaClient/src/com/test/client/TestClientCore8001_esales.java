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
public class TestClientCore8001_esales {

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
		for (int i = 0; i < 3; i++) {
			
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
			
//			//浙江
//			String mobilenum = "057187801558";
//			String onlineid = "108844";
//			String chargeamount = "10";
//			String payamount = "10";
//			String ordersource = "5";
			//上海固话
//			String mobilenum = "02187801558";
//			String onlineid = "108929";
//			String chargeamount = "10";
//			String payamount = "10";
//			String ordersource = "5";
			//上海固话
//			String mobilenum = "18939778817";
//			String onlineid = "108929";
//			String chargeamount = "10";
//			String payamount = "10";
//			String ordersource = "5";
			
			String mobilenum = "15868471746";
			String onlineid = "106959";
			String chargeamount = "20";
			String payamount = "22";
			String ordersource = "1";
			
			
			
//			String mobilenum = "186517701" + c;// 电话号码江苏
//			String mobilenum = "057022222" + c;// 电话号码江苏
//			String onlineid = "108844";
//			String chargeamount = "1";
//			String payamount = "1";
//			String ordersource = "1";
			
			
			
			String agentid = "DE201108091428260543";
			String orderid = ordertime + i;

			
			String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
			
//			String req = "comm=8010&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
//					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
			
			ConnectFuture cf = null;
			
			if(a == 1){
				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
//				cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
				a = 0;
			}else{
//				cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
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
