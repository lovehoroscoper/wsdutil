package com.test.mina;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 作为渠道测试地址服务器
 * @author Administrator
 *
 */
public class MinaTestQDHandler extends IoHandlerAdapter {


	private static Logger logger = Logger.getLogger(MinaTestQDHandler.class);

	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("incomming client(DB) : " + session.getRemoteAddress());
		
		Thread.sleep(10*1000);
		String res = "comm=8101&version=1.0&hfserialid=HF011110201723046470&channelserialid=CMC2011102100000033&dealtime=2011-10-21 00:36:06&dealamount=5000&sendserialid=12201110210036061812&resultno=2006";
		session.write(res);
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		logger.debug("one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为String:
		String req = (String) message;
		if(null == req || "".equals(req.trim()) || "HEARTBEAT_REQUEST".equals(req.trim())){
			session.write("HEARTBEAT_RESPONSE");
		}else{
			logger.info("接收充值信息,不做返回:"+req);
			
			Thread.sleep(20*1000);
			String res = "comm=8101&version=1.0&hfserialid=HF011110201723046470&channelserialid=CMC2011102100000033&dealtime=2011-10-21 00:36:06&dealamount=5000&sendserialid=12201110210036061812&resultno=2006";
			session.write(res);
		}
		
	}


	public String getFormatDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date());
	}

	public String result(String ss) {
		String end = ss.substring(ss.length() - 1, ss.length());
		int a = Integer.valueOf(end);
		String r = "";
		if (a == 0) {
			r = "0000";// <!-- 渠道返回充值成功 -->
		} else if (a == 1) {
			r = "2009";// key="0001"> <!-- 渠道返回充值失败 -->
		} else if (a == 2) {
			r = "2011";// key="0001"> <!-- 渠道返回充值失败 -->
		} else if (a == 3) {
			r = "1100";// key="0002"> <!-- 渠道返回充值未知-->
		} else {
			r = "1101";// key="0003"> <!-- 渠道返回充值超时 -->
		}
		return r;
	}
//	public String result(String ss) {
//		String end = ss.substring(ss.length() - 1, ss.length());
//		int a = Integer.valueOf(end);
//		String r = "";
//		if (a < 3) {
//			r = "0000";// <!-- 渠道返回充值成功 -->
//		} else if (3 <= a && a < 6) {
//			r = "0000";// key="0001"> <!-- 渠道返回充值失败 -->
//		} else if (6 <= a && a < 8) {
//			r = "0000";// key="0002"> <!-- 渠道返回充值未知-->
//		} else {
//			r = "0000";// key="0003"> <!-- 渠道返回充值超时 -->
//		}
//		return r;
//	}
}
