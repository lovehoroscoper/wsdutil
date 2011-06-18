package com.test.sometest;

import java.nio.channels.SelectionKey;

/**
 * @desc: 
 *
 *
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-6-18 下午03:44:48
 * @version:v1.0
 *
 */
public class TestLimitParam {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a = (SelectionKey.OP_READ
				| SelectionKey.OP_WRITE
				| SelectionKey.OP_CONNECT);
		
		int b = 2|1;
		System.out.println(a);
		System.out.println(b);

	}

}
