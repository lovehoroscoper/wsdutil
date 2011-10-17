package com.weisd.String;
/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-16 上午11:51:20
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a = 3 ;
		int b = 3;
		
//		if(a != 1 && a != 2  && b != 1  && b != 2){
		if((a != 1 && a != 2)  || ( b != 1  && b != 2)){
			System.out.println("22222");
		}else{
			System.out.println("111");
		}

	}

}
