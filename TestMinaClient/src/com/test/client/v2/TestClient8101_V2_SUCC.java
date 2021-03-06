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

import com.test.client.v2.handler.MinaDBCoreGWHandler8101_V2;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient8101_V2_SUCC {


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
		connector.setHandler(new MinaDBCoreGWHandler8101_V2());
		connector.setConnectTimeoutMillis(15000);

//		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
//		String  dateTime= simpleDate.format(new Date());
		String  dateTime= "20120607160305";
		//comm=8101&version=1.0&hfserialid=&zhorderid=&errorcode=10000000&resultno=0000&channelserialid=0000000315196824&acntbalance=2122730&dealtime=2011-11-29 12:25:33
		//comm=8101&version=1.0&hfserialid=HF011111290025435440&sendserialid=&channelserialid=                    &dealtime=2011-11-29 00:25:44&dealamount=1000&resultno=0000&resultmsg=交易成功
		String hfserialid = "HF011206070929497002";
		String sendserialid = "s_" + dateTime;
		String channelserialid = "c_" + dateTime;
		String dealtime = dateTime;
		String dealamount = "10000";
		String resultno = "0000";
		String resultmsg = "充值成功";
		String othererrorcode = "0000";
		
		String req_old = "comm=8101&version=1.0&hfserialid=" + hfserialid + 
				"&sendserialid=" + sendserialid + 
				"&channelserialid=" + channelserialid + 
				"&dealtime=" + dealtime + 
				"&dealamount=" + dealamount + 
				"&resultno=" + resultno + 
				"&resultmsg=" + resultmsg;
		
		String req_new = "comm=8101&version=1.0&hfserialid=" + hfserialid + 
				"&sendserialid=" + sendserialid + 
				"&channelserialid=" + channelserialid + 
				"&dealtime=" + dealtime + 
				"&dealamount=" + dealamount + 
				"&resultno=" + resultno + 
				"&resultmsg=" + resultmsg +
				"&errorcode=" + othererrorcode;
		
		
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.123", 9001));
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.151", 9001));
//		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.25.162", 9012));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		
//		session.write(req_old);
		session.write(req_new);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---------------------------------------");
	}

}
