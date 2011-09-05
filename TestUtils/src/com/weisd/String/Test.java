package com.weisd.String;

import java.io.UnsupportedEncodingException;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-2 下午2:05:13
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String returnBuf = "";
		try {
			returnBuf = new String("dwadwad".getBytes(), "UTF-8");
			throw new UnsupportedEncodingException("333333");
		} catch (UnsupportedEncodingException e) {
			int a = 1/0;
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
