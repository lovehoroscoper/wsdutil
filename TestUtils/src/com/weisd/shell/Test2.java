package com.weisd.shell;

import java.io.IOException;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-29 上午10:50:04
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Process pid = null;
		// 执行Shell命令
		try {
			pid = Runtime.getRuntime().exec("dir");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
