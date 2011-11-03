package com.weisd.decode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-10-27 下午4:20:06
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String s1 = "0jg3xNZ/Ehrtwb+7I13P7RiAz9R5MzwIW7wJ41BAIq+Q+Cqolq+VlCkdF7CasPgFy4fFMCBk/EZS KZ+PGDOc/4ac2TGV2Q85WBRYLLODrk/V6XEs56zTknfw631a918z21DQWbTujKvFBfINiQtfo9qr 5uHm5cKrnHVlZavH9iY=$0.0.1";
		String s2 = "0jg3xNZ%2FEhrtwb%2B7I13P7RiAz9R5MzwIW7wJ41BAIq%2BQ%2BCqolq%2BVlCkdF7CasPgFy4fFMCBk%2FEZS%0D%0AKZ%2BPGDOc%2F4ac2TGV2Q85WBRYLLODrk%2FV6XEs56zTknfw631a918z21DQWbTujKvFBfINiQtfo9qr%0D%0A5uHm5cKrnHVlZavH9iY%3D%240.0.1";

		try {
			String ss = URLDecoder.decode(s2, "UTF-8");
			System.out.println(ss);
			System.out.println(s1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

}
