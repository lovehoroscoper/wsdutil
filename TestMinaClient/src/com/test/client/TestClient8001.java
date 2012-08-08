package com.test.client;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.commons.lang.time.DateUtils;
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
public class TestClient8001 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		File destDir = new File("D:\\http\\tempimg\\");
//		File[] a = destDir.listFiles();
//
//		for (int i = 0; i < a.length; i++) {
//			File f = a[i];
//			String filename = f.getName();
//			if (filename.endsWith(".jpg") || filename.endsWith(".html") || filename.endsWith(".bmp") ) {
//				f.delete();
//			}
//		}

		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("GBK"));
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		// 读写通道10秒内无操作进入空闲状态
		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(new MinaDBCoreGWHandler8001());
		connector.setConnectTimeoutMillis(15000);
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 5020));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.164", 5008));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.161", 9001));
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.79", 9001));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		
		
		for (int i = 20; i < 21; i++) {
			String accnum = "134020812" + i;
			String ss = getFormatDate(new Date(), "yyyyMMddHHmmss");
			String order = "JB" + getFormatDate(new Date(), "yyyyMMddHHmmss");
//			String hf = "HF" + getFormatDate(new Date(), "yyyyMMddHHmmss");
//			String hf = "HF011110181221179132";
			String hf = "HF00" + ss + i;
			
//			String req = "comm=8001&version=1.0&agentid=weisd&ordersource=2&" + "hforderid=" + order + "&hfserialid=" + hf  + "&accnum=" + accnum
//					+ "&amount=5000&channelid=7181&ispid=0&servicetype=0&provinceid=2&citycode=021&cardnum=&cardpwd=";
			String req = "agentid=DE201201301436041200&" +
					"chargeamount=100&comm=8001&mark=" +
					"&mobilenum=15201386011&onlineid=107042&" +
					"orderid=" + order + "&ordersource=1&ordertime=20120227160233&payamount=99.4&receivetime=&version=1.0&verifystring=f7d28d81411e6041d9828cbe1866f30a";
			
			session.write(req);
			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		
		try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
		
//		for (int i = 30; i < 40; i++) {
//			String accnum = "134020812" + i;
//			String ss = getFormatDate(new Date(), "yyyyMMddHHmmss");
//			String order = "JB" + getFormatDate(new Date(), "yyyyMMddHHmmss");
////			String hf = "HF" + getFormatDate(new Date(), "yyyyMMddHHmmss");
////			String hf = "HF011110181221179132";
//			String hf = "HF00" + ss + i;
//			
//			String req = "comm=8001&version=1.0&agentid=weisd&ordersource=2&" + "hforderid=" + order + "&hfserialid=" + hf  + "&accnum=" + accnum
//					+ "&amount=5000&channelid=7181&ispid=0&servicetype=0&provinceid=2&citycode=021&cardnum=&cardpwd=";
//			session.write(req);
//			
////			try {
////				Thread.sleep(1000);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//		}

	}

	public static String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
}
