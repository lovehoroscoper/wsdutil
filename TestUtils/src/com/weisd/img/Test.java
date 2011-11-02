package com.weisd.img;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-10-29 下午2:18:54
 * @version v1.0
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		File file = new File("E:\\gaeProject\\TestUtils\\result3\\33.jpg");
		File file = new File("E:\\gaeProject\\TestUtils\\img3\\1.jpg");
		try {
			AutoImage t = new AutoImage(file);
			
			System.out.println(t.getValidatecode(4));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
