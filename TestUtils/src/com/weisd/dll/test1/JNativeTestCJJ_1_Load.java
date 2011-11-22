package com.weisd.dll.test1;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

/**
 * 加载识别库
 * @author Administrator
 *
 */
public class JNativeTestCJJ_1_Load {


	
	public static void main(String[] args) throws NativeException, IllegalAccessException {
		String filePath = "E:\\code_sdk\\C++\\wylt.cds";
		//返回-1代表载入出错。
		Long l = JNativeTestCJJ_1_Load.loadLibrary(filePath);
		
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
	public static final Long loadLibrary(String FilePath) throws NativeException, IllegalAccessException {
		JNative n = null;
		try {
			//Private Declare Function LoadCdsFromFile Lib "AntiVC.dll" (ByVal FilePath As String) As Long
			n = new JNative("AntiVC.dll", "LoadCdsFromFile"); // 常量DLL_NAME的值为User32.dll
			// 构造JNative时完成装载User32.dll,并且定位MessageBoxA方法
			n.setRetVal(Type.LONG); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.STRING, "" + FilePath);
			n.invoke(); // 调用方法
			return Long.parseLong(n.getRetVal());
		} finally {
			if (n != null)
				n.dispose(); // 记得释放
		}
	}
	
//	public static final int messageBox(int parentHandle, String message, String caption, int buttons) throws NativeException, IllegalAccessException {
//		JNative n = null;
//		try {
//			n = new JNative("User32.dll", "MessageBoxA"); // 常量DLL_NAME的值为User32.dll
//			// 构造JNative时完成装载User32.dll,并且定位MessageBoxA方法
//			n.setRetVal(Type.INT); // 指定返回参数的类型
//			int i = 0;
//			n.setParameter(i++, Type.INT, "" + parentHandle);
//			n.setParameter(i++, Type.STRING, message);
//			n.setParameter(i++, Type.STRING, caption);
//			n.setParameter(i++, Type.INT, "" + buttons); // 指定位置上的参数类型和值
//			n.invoke(); // 调用方法
//			return Integer.parseInt(n.getRetVal());
//		} finally {
//			if (n != null)
//				n.dispose(); // 记得释放
//		}
//	}
//
//	public static void main(String[] args) throws NativeException, IllegalAccessException {
//		JNativeTestCJJ.messageBox(100, "Hello jnative", "jnativetest", 1);
//	}
}
