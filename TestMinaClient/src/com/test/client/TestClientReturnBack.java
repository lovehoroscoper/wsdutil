package com.test.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
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
public class TestClientReturnBack {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NioSocketConnector connector = new NioSocketConnector();
		
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		connector.setHandler(new MinaDBCoreReturnBackHandler());
		connector.setConnectTimeoutMillis(10000);
		connector.getSessionConfig().setWriteTimeout(5);
		ConnectFuture cf = connector.connect(new InetSocketAddress("172.25.28.94", 9001));
		cf.awaitUninterruptibly();
		IoSession session = cf.getSession();
		
		// 话费流水号
//		String hfserialid = CheckUtil.getSubString(message, "hfserialid", "=", "&");
//		// 渠道订单号
//		String channelserialid = CheckUtil.getSubString(message, "channelserialid", "=", "&");
//		// 发送给渠道方的流水号
//		String sendserialid = CheckUtil.getSubString(message, "sendserialid", "=", "&");
//		// 处理时间
//		String dealtime = CheckUtil.getSubString(message, "dealtime", "=", "&");
//		// 实际处理金额
//		String dealamount = CheckUtil.getSubString(message, "dealamount", "=", "&");
//		if(null != dealamount && !"".equals(dealamount.trim())){
//			dealamount = UtilRMB.fmtFen2YuanRsString(dealamount);
		String hfserialid = "HF011108051704056341";
		String channelserialid = "123456WEISD3";
		String sendserialid = "";
		String dealamount = "3000";
		
		String succ = "resultno=1006&comm=8101&version=1.0&hfserialid=" + hfserialid + 
				"&channelserialid=" + channelserialid + 
				"&sendserialid=" +sendserialid + 
				"&dealamount=" + dealamount;
		
		session.write(succ);
		
		
		
	}

}
