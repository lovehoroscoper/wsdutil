package com.test.mina.server.kongzhong;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.DateUtils;

/**
 * 
 * 
 */
public class MinaServerHandler_KONG_V2 extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(MinaServerHandler_KONG_V2.class);
	public static final String HEARTBEATREQUEST = "HEARTBEAT_REQUEST";

	public void sessionOpened(IoSession session) throws Exception {
		logger.info("有新连接接入");
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		logger.debug("one Clinet Disconnect(DB) !");
	}

	// 当前置发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		String msg = (String) message;
		logger.info("--------获取---------------[" + msg.length() + "][" + msg + "]");
		

		// String type = msg.substring(0, 4);
		String type = msg.substring(8, 12);
		String backmsg = "";

		String s10 = "iiiiiiiiii";
		String s20 = "QDxxxxxxxxxxxxxxxxxx";
		// String s40 = "gggggggggggggggggggggggggggggggggggggggg";
		String s40 = MessageRequest.fmtSupplyBlankToRight("", 40);

		int len_header = 0;
		int len_body = 0;
		String body = backmsg;

		if ("0100".equals(type)) {
			// 签到
			// 4
			// 20
			// 2
			// 40位
			// 004801010100LG2012082815530180005
			// 004801010100LG201208281553018000500000000kkkkkkkk
			String sno = msg.substring(12, 32);
			backmsg = "0101" + sno + "00" + s40;

			len_header = 4 + 4;
			len_body = 4 + 20 + 2 + 40;
			body = backmsg;

		} else if ("0110".equals(type)) {
			// 签退
			// 4
			// 20
			// 2
			// 40位
			String sno = msg.substring(12, 32);
			backmsg = "0111" + sno + "00" + s40;

			len_header = 4 + 4;
			len_body = 4 + 20 + 2 + 40;
			body = backmsg;

		} else if ("0200".equals(type)) {
			// 充值
			// 4
			// 20
			// 2
			// 10
			// 8
			// 6
			// 20
			// 40
			String trantime = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
			String tran_date = trantime.substring(0, 8);
			String tran_time = trantime.substring(8, 14);
			String sno = msg.substring(12, 32);
			String balance = "0000100000";
			backmsg = "0201" + sno + "00" + balance + tran_date + tran_time + s20 + s40;

			len_header = 4 + 4;
			// 4
			// 20
			// 2
			// 10
			// 8
			// 6
			// 20
			// 40位

			len_body = 4 + 20 + 2 + 10 + 8 + 6 + 20 + 40;
			body = backmsg;

		} else if ("0210".equals(type)) {
			// 冲正
			// 4
			// 20
			// 2
			// 8
			// 6
			// 20
			// 40位
			String trantime = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
			String tran_date = trantime.substring(0, 8);
			String tran_time = trantime.substring(8, 14);
			String sno = msg.substring(12, 32);
			backmsg = "0211" + sno + "00" + tran_date + tran_time + s20 + s40;

			// 4
			// 20
			// 2
			// 8
			// 6
			// 20
			// 40位
			len_header = 4 + 4;
			len_body = 4 + 20 + 2 + 8 + 6 + 20 + 40;
			body = backmsg;

		} else if ("0300".equals(type)) {
			// 4
			// 20
			// 2
			// 8
			// 6
			// 20
			// 40位
			String trantime = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
			String tran_date = trantime.substring(0, 8);
			String tran_time = trantime.substring(8, 14);
			String sno = msg.substring(12, 32);
			String balance = "00000000000000500000";
			backmsg = "0301" + sno + "00" + tran_date + tran_time + balance + s40;
			//
			// 4
			// 20
			// 2
			// 8
			// 6
			// 20
			// 40位

			len_header = 4 + 4;
			len_body = 4 + 20 + 2 + 8 + 6 + 20 + 40;
			body = backmsg;

		} else if ("0310".equals(type)) {

			// 4
			// 20
			// 2
			// 2
			// 2
			// 20
			// 8
			// 6
			// 40位

			String trantime = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
			String tran_date = trantime.substring(0, 8);
			String tran_time = trantime.substring(8, 14);
			String sno = msg.substring(12, 32);
			backmsg = "0311" + sno + "00" + "01" + "01" + s20 + tran_date + tran_time + s40;

			// 4
			// 20
			// 2
			// 2
			// 2
			// 20
			// 8
			// 6
			// 40位

			len_header = 4 + 4;
			len_body = 4 + 20 + 2 + 2 + 2 + 20 + 8 + 6 + 40;
			body = backmsg;

		} else {

			backmsg = "no";
		}

		// Thread.sleep(Integer.valueOf(sleep) * 1000);
		// Thread.sleep(10 * 1000);
		Thread.sleep(2 * 1000);

		String header = MessageRequest.getMsgHeader((len_header + len_body) + "");
		String returnmsg = header + body;

		logger.info("返回[" + returnmsg.length() + "][" + returnmsg + "]");
		session.write(returnmsg);

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

		logger.info("--------经过handler----------exceptionCaught-----");

		super.exceptionCaught(session, cause);
	}

}
