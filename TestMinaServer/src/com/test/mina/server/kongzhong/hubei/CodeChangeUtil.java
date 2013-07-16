package com.test.mina.server.kongzhong.hubei;

import java.io.UnsupportedEncodingException;

public class CodeChangeUtil {

	public static void main(String[] args) {

	}

	public static String int2Bytes4StringV1(String numStr, String charsetName) throws UnsupportedEncodingException {
		return int2Bytes4StringV1(Integer.valueOf(numStr), charsetName);
	}

	public static String int2Bytes4StringV1(int num, String charsetName) throws UnsupportedEncodingException {
		byte[] b = int2BytesV1(num);
		return new String(b, charsetName);
	}

	public static int bytes2IntByStringV1(String str, String charsetName) throws UnsupportedEncodingException {
		byte[] b = str.getBytes(charsetName);
		return bytes2IntV1(b);
	}

	/**
	 * int转换整行的byte[]
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] int2BytesV1(int num) {
		byte[] b = new byte[4];
		int byteNum = (40 - Integer.numberOfLeadingZeros(num < 0 ? ~num : num)) / 8;
		for (int n = 0; n < byteNum; n++) {
			b[3 - n] = (byte) (num >>> (n * 8));
		}
		return b;
	}

	/**
	 * 整行的byte[]转换成int
	 * 
	 * @param b
	 * @return
	 */
	public static int bytes2IntV1(byte[] b) {
		// byte[] b=new byte[]{1,2,3,4};
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	/**
	 * int转换整行的byte[]
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] int2BytesV2(int num) {
		// int mask = 0xff;
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	public static int byteToInt2(byte[] b) {
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < 4; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}

}