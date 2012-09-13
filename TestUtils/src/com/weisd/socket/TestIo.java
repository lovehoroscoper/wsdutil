package com.weisd.socket;

import java.nio.ByteBuffer;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2012-9-13 下午10:02:16
 * @version v1.0
 */
public class TestIo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ByteBuffer buffer = ByteBuffer.allocate(5 * 1024);

		System.out.println(buffer.capacity());
	}

}
