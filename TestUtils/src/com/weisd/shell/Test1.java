package com.weisd.shell;

import java.io.IOException;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-29 上午10:41:32
 */
public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JavaShellUtil javaShellUtil = new JavaShellUtil();
		//参数为要执行的Shell命令，即通过调用Shell脚本sendKondorFile.sh将/temp目录下的tmp.pdf文件发送到192.168.1.200上
		try {
			int success = javaShellUtil.executeShell("sh /tmp/sendKondorFile.sh /temp tmp.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
