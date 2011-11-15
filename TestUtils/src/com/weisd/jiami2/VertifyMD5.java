package com.weisd.jiami2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Title: VertifyMD5.java</p>
 * <p>Description: </p>
 * <p>Copyright: XXXXXXXXXXXXXXXXXXX(c) 2011</p>
 * <p>Company: XXXXXXXXXXXXXXXXXXXXXXXXXXXX</p>
 * @author Liuhh(jxausea@gmail.com)
 * @date 2011-5-13
 * @version 2.0
 *
 */
public class VertifyMD5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	String aaa = "abc";
	String mac128byte = MD5Encode(aaa,"");
	System.out.println("md5加密结果32 bit------------->:"+mac128byte);
	}
	/**
	 * md5加密产生，产生128位（bit）的mac
	 * 将128bit Mac转换成16进制代码
	 * @param strSrc
	 * @param key
	 * @return
	 */
	public static String MD5Encode(String strSrc, String key) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes("UTF8"));

			String result = "";
			byte[] temp;
			temp = md5.digest(key.getBytes("UTF8"));
			
			System.out.println("temp--------->temp:"+temp.length);
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString(
						(0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}

			return result;

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
