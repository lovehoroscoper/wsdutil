package com.test.client.ka.qd;

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

import com.test.client.MinaDBCoreGWHandler8001;

/**
 * 直接发送渠道socket
 * 
 * @author Administrator
 * 
 */
public class TestClient8001_Web_2_Heart {

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

		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9996));

		for (int i = 0; i < 1; i++) {
			String ordertime = getFormatDate(new Date(), "yyyyMMddHHmmss");
			int c = (int) (Math.random() * 100 + 10);
			if (c > 99) {
				c = c - 10;
			}
			String mobilenum = "186517701" + c;// 电话号码江苏
			String orderid = "TJB" + ordertime + i;
			String hfserialid = "THF" + ordertime + i;
			String chargeamount = "10";
			String ispid = "0";
			String cardnum = "nuordertime";
			String cardpwd = "";
			String req = "HEARTBEAT_REQUEST";
//			String req = "23232";

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
