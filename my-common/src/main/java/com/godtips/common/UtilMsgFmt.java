package com.godtips.common;

/**
 * 
 * @author Administrator
 *
 */
public class UtilMsgFmt {

	private static final String fmtZero = "000000000000000000000000000000000000000000000000000000000000";

	private static final String fmtStr = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";

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
