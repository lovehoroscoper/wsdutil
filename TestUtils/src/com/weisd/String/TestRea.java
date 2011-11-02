package com.weisd.String;
/**
 * @desc: 
 *
 *
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-9-5 下午5:12:34
 * @version:v1.0
 *
 */
public class TestRea {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String xpath = "/xrpc/public_req//filesvr/fileflag";
		String xpath = "";
		String newKey = xpath.replaceAll("[/]", "-");
		
		System.out.println(newKey);

	}

}
