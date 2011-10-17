package com.weisd.decode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-22 上午11:48:24
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String aa = "%E5%85%A8%E5%9B%BD%E7%A7%BB%E5%8A%A8%E6%89%8B%E6%9C%BA10%E5%85%835%E5%88%86%E9%92%9F%E5%AE%9E%E6%97%B6%E5%85%85%E5%80%BC";
		String bb = "107622.20E";
		try {
			String ss = URLDecoder.decode(bb, "UTF-8");
			System.out.println(ss);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
