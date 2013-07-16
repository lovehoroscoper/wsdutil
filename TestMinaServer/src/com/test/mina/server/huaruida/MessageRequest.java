package com.test.mina.server.huaruida;


/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@163.com
 * @version 创建时间：2012-8-27 上午10:19:10
 */
public class MessageRequest {

	// 60个长度
	// 60个长度
	private static final String fmtZero = "000000000000000000000000000000000000000000000000000000000000";
	
	private static final String fmtStr = "                                                            ";
	public static final int len_header = 3 + 4 + 2 + 15 + 12 + 4 + 16 + 3 + 8;

	public static String getMsgHeader(String msgLength, String msgType, String msgSerialid, String checkCode, String respCode) {
		String synFrame = "HRD";// 同步帧 OctStr(3)
		String version = "22";// 版本 OctNum(2)
		String userId = "766110000000148";// 企业ID OctStr(15)
		String terminalId = "076661001996";// 终端ID OctNum(12)
		//String respCode = "000";// 响应码 OctNum(3)
		StringBuffer sb = new StringBuffer();
		sb.append(synFrame);
		sb.append(msgLength);// 长度
		sb.append(version);
		sb.append(userId);
		sb.append(terminalId);
		sb.append(msgType);// 报文类型
		sb.append(msgSerialid);// 流水号
		sb.append(respCode);
		sb.append(checkCode);// 校验码
		return sb.toString();
	}

	public static String getMacBlock(String msgType, String msgSerialid, String msgBody) {
		// 版本
		// 企业ID
		// 终端ID
		// 报文类型
		// 流水号
		// 报文体
		String version = "22";// 版本 OctNum(2)
		String userId = "766110000000148";// 企业ID OctStr(15)
		String terminalId = "076661001996";// 终端ID OctNum(12)
		// msgType报文类型
		// msgSerialid流水号
		// msgBody报文体
		StringBuffer sb = new StringBuffer();
		sb.append(version);
		sb.append(userId);
		sb.append(terminalId);
		sb.append(msgType);
		sb.append(msgSerialid);
		sb.append(msgBody);
		return sb.toString();
	}


	/**
	 * 报文采用ASCII字符串，定长数据项，左对齐右补空格，
	 * 
	 * @param msg
	 * @param len
	 * @return
	 */
	public static String fmtSupplyBlankToRight(String msg, int len) {
		String now_str = msg + fmtStr;
		return now_str.substring(0, len);
	}

	/**
	 * 金额为两位精度,右对齐左补‘0’
	 * 
	 * @param msg
	 * @param len
	 * @return
	 */
	public static String fmtSupplyZeroToLeft(String msg, int len) {
		String now_str = fmtZero + msg;
		return now_str.substring(now_str.length() - len, now_str.length());
	}


}
