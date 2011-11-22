package com.weisd.dll.test4;

public class TestDll {
	static {
		System.loadLibrary("AntiVC");// 载入dll
	}

	public native static String LoadCdsFromFile(String i);// 函数声明

}
