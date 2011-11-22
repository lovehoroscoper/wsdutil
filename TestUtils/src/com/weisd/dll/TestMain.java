package com.weisd.dll;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;

public class TestMain {
	public TestMain() {
	}

	public static void main(String[] args) {
		try {
			JNative termcallback = new JNative("mydll", "OpenDrive");
			TestCallback _mycallback = new TestCallback();
			termcallback.setRetVal(Type.INT);
			termcallback.setParameter(0, _mycallback.getCallbackAddress()); /* 回调函数地址作为参数传递到dll */
			// 调用getCallbackAddress后要调用该行代码
			System.err.println(JNative.releaseCallback(_mycallback));// 测试打印返回值发现是false，怀疑问题是不是在这里
			termcallback.setParameter(1, 0);
			termcallback.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
