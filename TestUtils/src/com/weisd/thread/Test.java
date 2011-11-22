package com.weisd.thread;
/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-4 下午4:57:59
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		try {
//			Thread.sleep((int)(Math.random() * 900 + 100));
//		} catch (InterruptedException e) {
//			System.out.println("进入处理时候睡眠1秒时间异常");
//		}
		
		for (int i = 0; i < 100; i++) {
			
//			System.out.println((int)(Math.random() * 100 + 100));
			System.out.println((int)(Math.random() * 300 + 100));
		}

	}

}
