package com.weisd.dll.test3;

import java.util.List;


import org.xvolks.jnative.misc.basicStructures.HWND;

import org.xvolks.jnative.util.User32;


public class MyEnumCallback extends DataPackageCallback{

	StringBuffer sb=new StringBuffer();
	public String getWindowEnumList(){
		return sb.toString();
	}
	/**
	 * MyEnumCallback要继承DataPackageCallback
	 * 这个方法得到回调参数的值
	 */
	@Override
	protected void ProcessCallbackDataPackage(List<Long> dataPackage) {
		// TODO Auto-generated method stub
	   
		for (Long key : dataPackage) {
			try {
				//System.err.println("Handle : " + key);
				String name = User32.GetWindowText(new HWND(key.intValue()));
			//	System.err.println("Name : " + name);
				if (name == null || name.length() == 0) {
				//	System.err.println("Skipping handle " + key);
				} else {
					sb.append(name).append("\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



}