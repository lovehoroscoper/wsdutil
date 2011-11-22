package com.weisd.dll;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;
import org.xvolks.jnative.Type;
import org.xvolks.test.callbacks.TestCallback;

public class UserCall {
	/**
	 * return 转换成功的字节数
	 */
	static JNative Something = null;
	static Pointer pointer;

	public String getSomething(String pSrc, Pointer pTar, int MaxCount) throws NativeException, IllegalAccessException {

		try {
			if (Something == null) {
				pTar = new Pointer(MemoryBlockFactory.createMemoryBlock(36));
				Something = new JNative("SCReader.DLL", "SCHelp_HexStringToBytes");
				// 利用org.xvolks.jnative.JNative 来装载
				// SCReader.dll，并利用其SCHelp_HexStringToBytes方法
				Something.setRetVal(Type.INT);
				// 指定返回参数的类型
			}
			int i = 0;
			Something.setParameter(i++, pSrc);
			Something.setParameter(i++, pTar);
			Something.setParameter(i++, MaxCount);
			System.out.println("调用的DLL文件名为：" + Something.getDLLName());
			System.out.println("调用的方法名为：" + Something.getFunctionName());
			// 传值
			Something.invoke();// 调用方法
			return Something.getRetVal();
		} finally {
			if (Something != null) {
				Something.dispose();// 释放
			}
		}
	}

	public Pointer creatPointer() throws NativeException {
		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(36));
		pointer.setIntAt(0, 36);
		return pointer;
	}

	public static void main(String[] args) throws NativeException, IllegalAccessException {
		UserCall uc = new UserCall();
		String result = uc.getSomething("0FFFFF", uc.creatPointer(), 100);
		System.err.println("转换成功的字节数为：" + result);
		try {
			TestCallback.runIt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
