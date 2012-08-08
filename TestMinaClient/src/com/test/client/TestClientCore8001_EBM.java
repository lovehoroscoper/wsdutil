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
public class TestClientCore8001_EBM {

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

		int a = 1;
		for (int i = 0; i < 50; i++) {
			
			String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
			int c = (int) (Math.random() * 100 + 10);
			if(c > 99){
				c = c - 10;
			}
			
			// 移动
			String mobilenum = "182672412" + c;// 电话号码
			String onlineid = "107784";
			String agentid = "WEISD_GUHUA";
			String orderid = ordertime + i;
			String chargeamount = "30";
			String payamount = "29.7";
			
			
			
			String ordersource = "2";
//			if(c < 20 ){
//				chargeamount = "10";
//				payamount = "9.6";
//			}else if(c < 50  && c >= 20){
//				chargeamount = "20";
//				payamount = "19.2";
//			}else if(c < 70 && c >= 50 ){
//				chargeamount = "30";
//				payamount = "28.8";
//			}else if(c < 90 && c >= 70 ){
//				chargeamount = "50";
//				payamount = "48";
//			}else{
//				chargeamount = "500";
//				payamount = "480";
//			}
//			if("10".equals(chargeamount)){
//				onlineid = "107618"; 
//			}else if("20".equals(chargeamount)){
//				onlineid = "107619"; 
//			}else if("30".equals(chargeamount)){
//				onlineid = "107620"; 
//			}else if("50".equals(chargeamount)){
//				onlineid = "107621"; 
//			}else if("500".equals(chargeamount)){
//				onlineid = "107760"; 
//			}else{
//				
//			}

			String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
			
			ConnectFuture cf = null;
			
			if(a == 1){
				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
				a = 0;
			}else{
				cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
				a = 1;
			}
			
			cf.awaitUninterruptibly();
			IoSession session = cf.getSession();
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
