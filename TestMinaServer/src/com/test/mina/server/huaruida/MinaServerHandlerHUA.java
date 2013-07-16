package com.test.mina.server.huaruida;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.DateUtils;
import com.junbao.hf.utils.common.StringUtils;

/**
 * 
 * 
 */
public class MinaServerHandlerHUA extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(MinaServerHandlerHUA.class);
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

		String reqMsg = (String) message;
		logger.info("--------经过handler---------------[" + reqMsg.length() + "][" + reqMsg + "]");

		String type = reqMsg.substring(36, 40);
		String backmsg = "";
		// 签到请求/响应包 0100/0101
		// 心跳测试请求/响应包 0110/0111（心跳消息没有消息体）
		// 充值请求/响应包 0200/0201
		// 查询请求/响应包 0210/0211


		String macBlock = "";
		String msgLength = "";
		String msgHeader = "";

		String res_msgtype = "";
		String uniquesno = reqMsg.substring(40, 56);
		String checkCode = "";
		String msgBody = "";
		String respCode = "";
		
		Thread.sleep(2 * 1000);
		
		if ("0100".equals(type)) {

			// login
			// HRD0091227661100000001480766610019960100120906095835000100000000000123456781234567890123456

			msgBody = "82288E46A4C26AB64D4A9588";// MAC

			// macBlock = MessageRequest.getMacBlock(QdInterfaceCode.HEART,
			// taskBean.getUniqueSno(), msgBody);

			res_msgtype = "0101";

			FileReader fr = new FileReader("D:\\log\\testqd\\login.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = StringUtils.getStringFromEmpty(br.readLine());
//			respCode = "000";
			respCode = line;
			

			
		} else if ("0110".equals(type)) {
			
			//心跳
			msgBody = "";// MAC
			
			FileReader fr = new FileReader("D:\\log\\testqd\\heart.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = StringUtils.getStringFromEmpty(br.readLine());
//			respCode = "000";
			respCode = line;
			
			
			
			
			res_msgtype = "0111";
			
			macBlock = MessageRequest.getMacBlock(res_msgtype, uniquesno, msgBody);
			logger.info("macBlock:" + macBlock);
			checkCode = DesCbcBiz.getMAC(DesCbcBiz.getInstance().getKey(), macBlock);
			
			logger.info("checkCode:" + checkCode);

		} else if ("0200".equals(type)) {
			
			//充值
			msgBody = "";// MAC
			
//			String hrd = returnMsg.substring(0, 3);
//			String len_msg = returnMsg.substring(3, 7);
//			String version = returnMsg.substring(7, 9);
//			String userId = returnMsg.substring(9, 24);
//			String terminal_id = returnMsg.substring(24, 36);
//			String type = returnMsg.substring(36, 40);
//			String uniqueSno = returnMsg.substring(40, 56);
//			String re_resultno = returnMsg.substring(56, 59);
//			String qd_checkcode = returnMsg.substring(59, 67);
			//--body
			String accnum = reqMsg.substring(67, 78);
			String citycode = reqMsg.substring(78, 82);
			String trantime = reqMsg.substring(82, 94);
			trantime = DateUtils.getFormatCurrDate("yyMMddHHmmss");// 交易时间12
			String finishmoney = reqMsg.substring(94, 104);
			String accnumtype = reqMsg.substring(104, 106);
			String re_channelserialid = reqMsg.substring(106, 132);
			
//			
////			缴费号码	OctNum(11)	所需查询的号码，不足右补空格
////			区号	OctNum(4)	当地区号，不足右补空格
////			交易时间	OctNum(12)	返回充值请求的交易时间, 格式：YYMMDDHHMMSS
////			交易金额	OctNum(10)	缴费金额，单位分，不足左补0
////			流水号	OctNum(26)	流水号（该笔交易唯一标识，冲正时需要使用）。
////			用户余额	OctNum(10)	缴费号码金额，单位分，不足左补0，如：0000005000代表50RMB.
////			代理商余额	OctNum(10)	缴费号码金额，单位分，不足左补0，如：0000005000代表50RMB.
			StringBuffer sb = new StringBuffer();
			sb.append(accnum);
			sb.append(citycode);
			sb.append(trantime);
			sb.append(finishmoney);
			sb.append(re_channelserialid);
			sb.append("0000005000");
			sb.append("00000095000");

			msgBody = sb.toString();
			
			FileReader fr = new FileReader("D:\\log\\testqd\\8001.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = StringUtils.getStringFromEmpty(br.readLine());
//			respCode = "000";
			respCode = line;
			
			
			
			res_msgtype = "0201";
			
			macBlock = MessageRequest.getMacBlock(res_msgtype, uniquesno, msgBody);
			logger.info("macBlock:" + macBlock);
			checkCode = DesCbcBiz.getMAC(DesCbcBiz.getInstance().getKey(), macBlock);
			
			logger.info("checkCode:" + checkCode);

			// 冲正请求/响应包 0220/0221
		} else if ("0220".equals(type)) {

			
			//充值
			msgBody = "";// MAC
			
			//--header
//			String hrd = reqMsg.substring(0, 3);
//			String len_msg = reqMsg.substring(3, 7);
//			String version = reqMsg.substring(7, 9);
//			String userId = reqMsg.substring(9, 24);
//			String terminal_id = reqMsg.substring(24, 36);
//			//String type = reqMsg.substring(36, 40);
//			String uniqueSno = reqMsg.substring(40, 56);
//			String re_resultno = returnMsg.substring(56, 59);
//			String qd_checkcode = returnMsg.substring(59, 67);
			//--body
			String re_channelserialid = reqMsg.substring(67, 93);
			String trantime = reqMsg.substring(93, 105);
			trantime = DateUtils.getFormatCurrDate("yyMMddHHmmss");// 交易时间12
			
//			流水号	OctNum(26)	充值响应时返回的流水号
//			交易时间	OctNum(12)	返回冲正请求的交易时间, 格式：YYMMDDHHMMSS

			StringBuffer sb = new StringBuffer();
			sb.append(re_channelserialid);
			sb.append(trantime);

			msgBody = sb.toString();
			
			FileReader fr = new FileReader("D:\\log\\testqd\\8003.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = StringUtils.getStringFromEmpty(br.readLine());
//			respCode = "000";
			respCode = line;
			
			
			
			res_msgtype = "0221";
			
			macBlock = MessageRequest.getMacBlock(res_msgtype, uniquesno, msgBody);
			logger.info("macBlock:" + macBlock);
			checkCode = DesCbcBiz.getMAC(DesCbcBiz.getInstance().getKey(), macBlock);
			
			logger.info("checkCode:" + checkCode);
			
		} else {

			backmsg = "no";
		}


		
		int len_body = msgBody.length();

		msgLength = MessageRequest.fmtSupplyZeroToLeft((MessageRequest.len_header + len_body) + "", 4);
		
		macBlock = MessageRequest.getMacBlock(res_msgtype, uniquesno, msgBody);
		logger.info("macBlock:" + macBlock);
		checkCode = DesCbcBiz.getMAC(DesCbcBiz.getInstance().getKey(), macBlock);
		
		logger.info("checkCode:" + checkCode);
		
		
		FileReader fr = new FileReader("D:\\log\\testqd\\checkcode.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = StringUtils.getStringFromEmpty(br.readLine());
		//checkCode = line;
		
		
		msgHeader = MessageRequest.getMsgHeader(msgLength, res_msgtype, uniquesno, checkCode,respCode);
		backmsg = msgHeader + msgBody;

	
		
		if (!"0100".equals(type)) {
			
//			session.close(true);
			session.close(false);
			
			return;
		}else{
//			backmsg = "HRD00912208012000000140011080000111801011209141907440001000BF7C79F982288E46A4C26AB64D4A9588";
		}
		logger.info("返回[" + backmsg.length() + "][" + backmsg + "]");
		session.write(backmsg);

	}



	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

		logger.info("--------经过handler----------exceptionCaught-----");

		super.exceptionCaught(session, cause);
	}

}
