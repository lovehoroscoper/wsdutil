package com.weisd.dll.test2;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;
import org.xvolks.jnative.Type;
import org.xvolks.test.callbacks.TestCallback;

public class UserCall2 {
	/**
	 * return 转换成功的字节数
	 */
	static JNative Something = null;
	static Pointer pointer;

	public String getSomething(String pSrc, Pointer pTar, int MaxCount) throws NativeException, IllegalAccessException {
		String filePath_library = "E:\\code_sdk\\C++\\wylt.cds";
		String filePath_img = "http://bbs.ent.163.com/bbs/checkcode.jsp?1201359681906";
		try {
			Something = new JNative("AntiVC.dll", "LoadCdsFromFile"); // 常量DLL_NAME的值为User32.dll
			Something.setRetVal(Type.LONG); // 指定返回参数的类型
			int i = 0;
			Something.setParameter(i++, Type.STRING, "" + filePath_library);
			Something.invoke(); // 调用方法
			Long load_res = Long.parseLong(Something.getRetVal());
			if (1 == load_res.intValue()) {// 加载成功
				
				
					pTar = new Pointer(MemoryBlockFactory.createMemoryBlock(36));
					Something = new JNative("AntiVC.dll", "GetVcodeFromURL");
					// 利用org.xvolks.jnative.JNative 来装载
					// SCReader.dll，并利用其SCHelp_HexStringToBytes方法
					Something.setRetVal(Type.INT);
					// 指定返回参数的类型
					int j = 0;
					String aa = "";
					Something.setParameter(j++, Type.LONG, "0");
					Something.setParameter(j++, Type.STRING, "" + filePath_img);
					Something.setParameter(j++, pTar);
					Something.invoke(); // 调用方法
					String sss = Something.getRetVal();
					System.out.println("调用的DLL文件名为：" + Something.getDLLName());
					System.out.println("调用的方法名为：" + Something.getFunctionName());
					// 传值
					Something.invoke();// 调用方法
					String ss =  Something.getRetVal();
				
				return "";
			} else {
				System.out.println("加载是别库失败");
				return "-1";
			}
		} finally {
			if (Something != null)
				Something.dispose(); // 记得释放
		}

	}

	public Pointer creatPointer() throws NativeException {
		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(36));
		pointer.setIntAt(0, 36);
		return pointer;
	}

	public static void main(String[] args) throws NativeException, IllegalAccessException {
		UserCall2 uc = new UserCall2();
		String result = uc.getSomething("0FFFFF", uc.creatPointer(), 100);
		System.err.println("转换成功的字节数为：" + result);
//		try {
//			TestCallback.runIt();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
