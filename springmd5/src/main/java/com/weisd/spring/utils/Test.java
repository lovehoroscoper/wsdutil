package com.weisd.spring.utils;
/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-15 下午1:27:29
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Test t = new Test();
		String encodeName = "username,password,";
		String split = ",";
		t.getEncodeNameArry(encodeName, split);
	}

	private String[] getEncodeNameArry(String encodeName,String split){
		String[] arr = null;
		if(null != encodeName && !"".equals(encodeName.trim())){
			arr = encodeName.split("[,]");
		}
		return arr;
	}
}
