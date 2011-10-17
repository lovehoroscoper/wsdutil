package com.weisd.inttest;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-9-29 下午12:55:53
 */

public class Test {
	public static void main(String[] args) {

		Integer x = 100;
		Integer y = x;
		System.out.println("x.hashCode():" + x.hashCode());
		x++;// 在这里创建一个新的对象，因为Interger类不可改变，故只能创建一个新的类
		// hash code
		// 与原来的不同，这也就说明创建了一个新的对象
		System.out.println("(x++)x.hashCode():" + x.hashCode());
		System.out.println(x == y);

		Character c1 = 'c';
		Character c2 = c1;
		c1++;
		System.out.println(c1 == c2);

		String s1 = "abc";
		String s2 = s1;
		s1 += "d";
		System.out.println(s1 == s2);
	}

}
