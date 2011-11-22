package com.weisd.dll.jawn.t1;

import org.jawin.COMException;
import org.jawin.FuncPtr;
import org.jawin.ReturnFlags;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-21 上午9:22:23
 */
public class Test3 {

	/**
	 * @param args
	 * @throws COMException
	 */
	public static void main(String[] args) throws COMException {
		FuncPtr msgbox = new FuncPtr("user32.dll", "messageboxw"); // 获得函数指针
		msgbox.invoke_I(0, "hello from a dll", "from jawin", 0, ReturnFlags.CHECK_FALSE);

	}
}
