package com.weisd.dll.test4;
/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-19 下午4:59:45
 */
public class Test222 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

			TestDll td = new TestDll(); 
			System.out.println("result= " + TestDll.LoadCdsFromFile("E:\\code_sdk\\C++\\wylt.cds"));
	}

}
