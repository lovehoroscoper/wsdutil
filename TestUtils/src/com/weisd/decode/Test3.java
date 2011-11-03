package com.weisd.decode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-10-28 下午1:38:01
 */
public class Test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String a = "0jg3xNZ/Ehrtwb+7I13P7RiAz9R5MzwIW7wJ41BAIq+Q+Cqolq+VlCkdF7CasPgFy4fFMCBk/EZS KZ+PGDOc/4ac2TGV2Q85WBRYLLODrk+xGR1vHDN4gxR1O3gHXRXIf+zVF9nAvRAJP5RK+1IZusqr zu05f0zZkS6kg5RvZ3g=$0.0.1";
String a = "0jg3xNZ/Ehrtwb+7I13P7RiAz9R5MzwIW7wJ41BAIq+Q+Cqolq+VlCkdF7CasPgFy4fFMCBk/EZS KZ+PGDOc/4ac2TGV2Q85WBRYLLODrk+xGR1vHDN4gxR1O3gHXRXIf+zVF9nAvRAJP5RK+1IZusqr zu05f0zZkS6kg5RvZ3g=$0.0.1";
		
		try {
			String ss = URLEncoder.encode(a, "UTF-8");
			System.out.println(ss);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
