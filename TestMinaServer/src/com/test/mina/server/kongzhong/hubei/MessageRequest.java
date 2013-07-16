package com.test.mina.server.kongzhong.hubei;

import java.io.UnsupportedEncodingException;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@163.com
 * @version 创建时间：2012-8-27 上午10:19:10
 */
public class MessageRequest {


	private static final String fmtZero = "000000000000000000000000000000000000000000000000000000000000";

//	private static final String fmtStr = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
	private static final String fmtStr = "                                                            ";

	public static final int len_header = 8;// 消息头长度

	/**
	 * 
	 * @param msgLength
	 *            消息长度（字节）
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getMsgHeader(int msgLength) throws UnsupportedEncodingException {
		String version = "0101";// 与协议的版本同步
		StringBuffer sb = new StringBuffer();
		String new_len = CodeChangeUtil.int2Bytes4StringV1(msgLength,"gb2312");
		sb.append(new_len);// 长度
		sb.append(version);
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

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 60; i++) {
			sb.append("\0");
		}
		System.out.println("[" + sb.toString() + "]");
		System.out.println("[" + fmtStr + "]");
	}
}
