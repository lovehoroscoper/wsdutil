package com.weisd.dll.test5;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-22 下午12:38:30
 */
public class Test {

	private static int index;

	public Test() {


	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try {
			JNative n = new JNative("KeyCodeDll.dll", "IInitKeyCode");
			n.setRetVal(Type.INT); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.INT, "0");
			n.invoke(); // 调用方法
			index = Integer.parseInt(n.getRetVal());
			System.out.println("初始化:" + index);
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		Test t = new Test();
		String filePath_img = "E:\\temp.jpg";
		JNative n = null;
		StringBuffer sb = new StringBuffer();
		try {
			n = new JNative("KeyCodeDll.dll", "IGetCode");
			n.setRetVal(Type.INT); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.INT, "" + filePath_img);
			n.setParameter(i++, Type.PSTRUCT, sb.toString());
			n.setParameter(i++, Type.STRING, index + "");
			n.invoke(); // 调用方法
			int res = Integer.parseInt(n.getRetVal());
			System.out.println("调用结果:" + res);
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (n != null)
				try {
					n.dispose();
				} catch (NativeException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} // 记得释放
		}
	}

}
