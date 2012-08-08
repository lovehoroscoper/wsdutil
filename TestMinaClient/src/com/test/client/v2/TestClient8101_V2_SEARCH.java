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
public class TestClient8101_V2_SEARCH {


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

		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String  dateTime= simpleDate.format(new Date());
		//comm=8102&version=1.0&hfserialid=HF011111290300305745&channelserialid=CTC2011112900000016&dealtime=2011-11-29 03:00:27&dealamount=5000&sendserialid=12201111290300301603&resultno=2006&trantype=01&chargestatus=2
		
		String hfserialid = "HF011205151600075014";
		String sendserialid = "s_" + dateTime;
		String channelserialid = "c_" + dateTime;
		String dealtime = dateTime;
		String dealamount = "3000";
//		String resultno = "0000";
//		String resultmsg = "充值成功";
//		String chargestatus = "0";
//		String othererrorcode = "1002";
//		String resultno = "0000";
//		String resultmsg = "充值失败";
//		String chargestatus = "3";
//		String othererrorcode = "1002";
		String resultno = "1006";
		String resultmsg = "查询失败22";
		String chargestatus = "3";
		String othererrorcode = "9998";
		
		
		String req_old = "comm=8102&version=1.0&hfserialid=" + hfserialid + 
				"&channelserialid=" + channelserialid + 
				"&dealtime=" + dealtime + 
				"&dealamount=" + dealamount + 
				"&sendserialid=" + sendserialid + 
				"&resultno=" + resultno + "&trantype=01&chargestatus=" + chargestatus;
		
		String req_new = "comm=8102&version=1.0&hfserialid=" + hfserialid + 
				"&channelserialid=" + channelserialid + 
				"&dealtime=" + dealtime + 
				"&dealamount=" + dealamount + 
				"&sendserialid=" + sendserialid + 
				"&resultno=" + resultno + "&trantype=01&chargestatus=" + chargestatus +
				"&resultmsg=" + resultmsg +
				"&errorcode=" + othererrorcode;
		
		
//		String req_old = "comm=8102&version=1.0&hfserialid=" + hfserialid + 
//				"&sendserialid=" + sendserialid + 
//				"&channelserialid=" + channelserialid + 
//				"&dealtime=" + dealtime + 
//				"&dealamount=" + dealamount + 
//				"&resultno=" + resultno + 
//				"&resultmsg=" + resultmsg;
//		
//		String req_new = "comm=8101&version=1.0&hfserialid=" + hfserialid + 
//				"&sendserialid=" + sendserialid + 
//				"&channelserialid=" + channelserialid + 
//				"&dealtime=" + dealtime + 
//				"&dealamount=" + dealamount + 
//				"&resultno=" + resultno + 
//				"&resultmsg=" + resultmsg +
//				"&errorcode=" + othererrorcode;
		
		
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.61.151", 9001));
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
