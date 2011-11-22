package com.weisd.String;

import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-2 下午2:05:13
 */
public class Test {
	private static Logger logger = Logger.getLogger(Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String returnBuf = "";
		// try {
		// returnBuf = new String("dwadwad".getBytes(), "UTF-8");
		// throw new UnsupportedEncodingException("333333");
		// } catch (UnsupportedEncodingException e) {
		// int a = 1/0;
		// } catch (Exception e) {
		// System.out.println(e);
		// }
		// String hf = "123";
		// System.out.println( Integer.valueOf(hf.substring(hf.length()-1,
		// hf.length())));
		// String hf = "123";

		// System.out.println(RandomStringUtils.random(2));

		// for (int i = 0; i < 100; i++) {
		// System.out.println((int)(Math.random() * 900 + 100));
		// }
		FileOutputStream output = null;
		IOUtils.closeQuietly(output);
		
		logger.info("222");
	}

}
