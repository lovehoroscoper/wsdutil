package com.weisd.dll.test1;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

/**
 * 加载识别库
 * @author Administrator
 *
 */
public class JNativeTest {


	
	public static void main(String[] args) throws NativeException, IllegalAccessException {
		String filePath = "E:\\code_sdk\\C++\\wylt.cds";
		//返回-1代表载入出错。
		String l = JNativeTest.loadLibrary(filePath);
		
		System.out.println(l);
	}
	
	/**
	 * 加载识别库
	 * @param parentHandle
	 * @param message
	 * @param caption
	 * @param buttons
	 * @return
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public static final String loadLibrary(String FilePath) throws NativeException, IllegalAccessException {
		JNative n = null;
		try {
			//Private Declare Function LoadCdsFromFile Lib "AntiVC.dll" (ByVal FilePath As String) As Long
			n = new JNative("Testddl.dll", "getMyCode"); // 常量DLL_NAME的值为User32.dll
			// 构造JNative时完成装载User32.dll,并且定位MessageBoxA方法
			n.setRetVal(Type.STRING); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.STRING, "5" );
			n.invoke(); // 调用方法
			return n.getRetVal();
		} finally {
			if (n != null)
				n.dispose(); // 记得释放
		}
	}

}
