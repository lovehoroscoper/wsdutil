package com.weisd.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-5 下午2:17:46
 */
public class TestPut {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map map = new HashMap();
		map.put("/xrpc/public_req/filesvr/filemd5", "2222");
		
		System.out.println(map.get("/xrpc/public_req/filesvr/filemd5"));
		
	}

}
