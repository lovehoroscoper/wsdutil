package com.test.vo;
/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@163.com
 * @version 创建时间：2012-8-28 下午2:44:56
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		String type = "00001111".substring(4, 8);
		
//		String sno = "000011112222xxxxxxxxxxxxxxxxxxxx".substring(12, 32);
//		String sno = "000011112222xxxxxxxxxxxxxxxxxxxx".substring(12, 32);
		
//		System.out.println(sno);
		
		String str = "004801010100LG201208281553018000500000000kkkkkkkk";
		
		String newStr = str.substring(12, 32);
		
		System.out.println(newStr);

	}

}
