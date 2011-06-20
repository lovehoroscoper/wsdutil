package com.test.mina;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.CheckUtil;

/**
 * 
 * @author liujiangtao
 * 
 */
public class MinaDBCoreEbsHandler extends IoHandlerAdapter {
	// @Override
	// public void exceptionCaught(IoSession session, Throwable cause) throws
	// Exception {
	// // TODO Auto-generated method stub
	// super.exceptionCaught(session, cause);
	// logger.info("super.exceptionCaught(session, cause); : " );
	//
	// }
	//
	// @Override
	// public void messageSent(IoSession session, Object message) throws
	// Exception {
	// // TODO Auto-generated method stub
	// super.messageSent(session, message);
	// logger.info("super.messageSent(session, message); " );
	// }

	private static Logger logger = Logger.getLogger(MinaDBCoreEbsHandler.class);

	public void sessionOpened(IoSession session) throws Exception {
		logger.info("incomming client(DB) : " + session.getRemoteAddress());
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		logger.info("one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为String:
		String req = (String) message;
		if (null == req) {

		} else {
			boolean isFlag = req.contains("resultno=");
			boolean isComm = req.contains("comm=");
			if (isFlag) {
				logger.info("客户端处理消息成功发送回执");
			} else if(isComm){
				Thread.sleep(5000);
				// 结果编码

				// 话费流水号
				String hfserialid = CheckUtil.getSubString(req, "hfserialid", "=", "&");
				String accnum = CheckUtil.getSubString(req, "accnum", "=", "&");
				String comm = CheckUtil.getSubString(req, "comm", "=", "&");
				
				// 渠道订单号
				String channelserialid = hfserialid + "_1";
				// 发送给渠道方的流水号
				String sendserialid = hfserialid + "_2";

				// 处理时间
				String dealtime = getFormatDate();
				// 实际处理金额
				//String dealamount = "50";
				String amount = CheckUtil.getSubString(req, "amount", "=", "&");

	

				
				logger.info("【核心】发来的消息：" + req);
				String resultno = result(accnum);
				
				StringBuilder res = new StringBuilder();
				String rsComm = "";
				if("8001".equals(comm)){
					rsComm = "8101";
					
					res.append("comm=");
					res.append(rsComm);
					res.append("&");
					res.append("version=");
					res.append("1.0");
					res.append("&");
					res.append("resultno=");
					res.append(resultno);
					res.append("&");
					res.append("hfserialid=");
					res.append(hfserialid);
					res.append("&");
					res.append("channelserialid=");
					res.append(channelserialid);
					res.append("&");
					res.append("sendserialid=");
					res.append(sendserialid);
					res.append("&");
					res.append("dealtime=");
					res.append(dealtime);
					res.append("&");
					res.append("dealamount=");
					res.append(amount);
					
				}else if("8002".equals(comm)){
					rsComm = "8102";
					String chargestatus = "";
					if("15505711115".equals(accnum)){
						resultno = "0000";
						chargestatus = "0";
					}else if("15505711116".equals(accnum)){
						resultno = "0000";
						chargestatus = "3";
					}else{
						resultno = "1111";
						chargestatus = "";
					}
					

					
					res.append("comm=");
					res.append(rsComm);
					res.append("&");
					res.append("version=");
					res.append("1.0");
					res.append("&");
					res.append("resultno=");
					res.append(resultno);
					res.append("&");
					res.append("hfserialid=");
					res.append(hfserialid);
					res.append("&");
					res.append("dealtime=");
					res.append(dealtime);
					res.append("&");
					res.append("dealamount=");
					res.append(amount);
					res.append("&");
					res.append("chargestatus=");
					res.append(chargestatus);
					res.append("&");
					res.append("trantype=");
					res.append("01");
				}
				


				String resss = res.toString();
				
				session.write(resss);
				logger.info("处理返回信息：" + resss);
			}else{
				 logger.info("客户端发来无效的信息：");
			}

			// logger.info("已收从前置发来的消息,存入后队列数量：" + msgQueue.getCount());
			 logger.info("已收从前置发来的消息："+req);
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
