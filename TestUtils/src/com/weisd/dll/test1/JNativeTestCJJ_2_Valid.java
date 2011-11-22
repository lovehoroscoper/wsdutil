package com.weisd.dll.test1;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

/**
 * 加载识别库
 * 
 * @author Administrator
 * 
 */
public class JNativeTestCJJ_2_Valid {

	public static void main(String[] args) throws NativeException, IllegalAccessException {
		String filePath_library = "E:\\wylt.cds";
		String filePath_img = "E:\\temp.jpg";
		// 返回-1代表载入出错。
		String l = JNativeTestCJJ_2_Valid.validCode(filePath_library, filePath_img);

		System.out.println(l);
	}

	// Private Declare Function GetVcodeFromURL Lib "AntiVC.dll" (ByVal
	// CdsFileIndex As Long,ByVal ImgURL As String,ByVal Vcode As String) As
	// Boolean
	// 函数功能说明：使用CdsFileIndex对应的识别库，识别一个从指定网址下载的图像文件，成功返回True，否则返回False。该函数内置了文件下载功能。
	// 参数说明：
	// CdsFileIndex ：整数型，识别库索引，用于确定这个图像文件对应哪个识别库。
	// ImgURL ：图像文件所在网址。
	// Vcode ：文本型，返回的验证码字符串，使用该参数前需要将一个足够长的空白字符串赋值给它。

	/**
	 * 加载识别库
	 * 
	 * @param parentHandle
	 * @param message
	 * @param caption
	 * @param buttons
	 * @return
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public static final String validCode(String filePath_library, String filePath_img) throws NativeException, IllegalAccessException {
		JNative n = null;
		
		try {
			n = new JNative("AntiVC.dll", "LoadCdsFromFile"); // 常量DLL_NAME的值为User32.dll
			n.setRetVal(Type.LONG); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.STRING, "" + filePath_library);
			n.invoke(); // 调用方法
			Long load_res = Long.parseLong(n.getRetVal());
			String temp = "";
			if (1 == load_res.intValue()) {// 加载成功
				
				n = new JNative("AntiVC.dll", "GetVcodeFromFile");
				
				n.setRetVal(Type.INT); // 指定返回参数的类型
//				n.setRetVal(Type.STRING); // 指定返回参数的类型
				int j = 0;
//				char[] aa = new char[7];
				 StringBuffer szDisplayName = new StringBuffer(10);   
				n.setParameter(0, Type.LONG, "1");
				n.setParameter(1, Type.STRING, filePath_img);
				n.setParameter(2, Type.STRING, temp);
				n.invoke(); // 调用方法
				String sss = n.getRetVal();
				System.out.println("sss = " + sss + "|temp = " + temp);
				return "";
			} else {
				System.out.println("加载是别库失败");
				return "-1";
			}
		} finally {
			if (n != null)
				n.dispose(); // 记得释放
		}
	}

//	public void getTest(OutString ss){
//		
//	}
	
//	public static final int messageBox(int parentHandle, String message, String caption, int buttons) throws NativeException, IllegalAccessException {
//	JNative n = null;
//	try {
//		n = new JNative("User32.dll", "MessageBoxA"); // 常量DLL_NAME的值为User32.dll
//		// 构造JNative时完成装载User32.dll,并且定位MessageBoxA方法
//		n.setRetVal(Type.INT); // 指定返回参数的类型
//		int i = 0;
//		n.setParameter(i++, Type.INT, "" + parentHandle);
//		n.setParameter(i++, Type.STRING, message);
//		n.setParameter(i++, Type.STRING, caption);
//		n.setParameter(i++, Type.INT, "" + buttons); // 指定位置上的参数类型和值
//		n.invoke(); // 调用方法
//		return Integer.parseInt(n.getRetVal());
//	} finally {
//		if (n != null)
//			n.dispose(); // 记得释放
//	}
//}
}
