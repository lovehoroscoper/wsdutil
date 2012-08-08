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
public class TestClientCore_Web8001 {

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
			
//			String mobilenum = "186517701" + c;// 电话号码江苏
//			String onlineid = "108720";
//			String agentid = "DE201108091428260543";
//			String orderid = ordertime + i;
//			String chargeamount = "1";
//			String payamount = "1.2";
//			String ordersource = "2";
			
//			String mobilenum = "186517701" + c;// 电话号码江苏
//			String onlineid = "108721";//浙江
//			String agentid = "DE201108091428260543";
//			String orderid = ordertime + i;
//			String chargeamount = "1";
//			String payamount = "1.1";
//			String ordersource = "2";
			
			String mobilenum = "155100712" + c;// 北京联通
			String onlineid = "108740";
			String agentid = "PHONE";
			String orderid = ordertime + c;
			String chargeamount = "1";
			String payamount = "1.1";
			String ordersource = "2";
			
			String req = "comm=8001&version=1.0&onlineid=" + onlineid + "&agentid=" + agentid + "&ordersource=" + ordersource + "&orderid=" + orderid + "&mobilenum=" + mobilenum + "&chargeamount=" + chargeamount
					+ "&payamount=" + payamount + "&ordertime=" + ordertime + "&mark=test";
			
			ConnectFuture cf = null;
			
				cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
//				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
//			if(a == 1){
//				cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
//				a = 0;
//			}else{
//				cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
//				a = 1;
//			}
			
			System.out.println(req);
				
			cf.awaitUninterruptibly();
			IoSession session = cf.getSession();
			session.write(req);
			
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}

	}

	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
}
