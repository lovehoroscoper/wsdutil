package com.weisd.dll;
/*
* TestCallback.java
* 实现Callback接口
*/
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.util.Callback;

public class TestCallback implements Callback/*实现此接口*/ {
	public TestCallback() {
    }

	@Override
	public int callback(long[] values) {
      System.out.println("参数数量：" + values.length);
      if (values == null) {
          System.err.println("callback ret " + 3);
          return 3;
      } else if (values.length == 6) {
          System.err.println("执行回调event！");
          return 1;
      } else {
          System.err.println("参数数量是" + values.length);
          return 2;
      }
	}

	@Override
	public int getCallbackAddress() throws NativeException {
      System.out.println("java回调函数地址");
      return JNative.createCallback(6/*回调函数参数数目*/, this);
	}
	
//    public int callback(long[] values) {
//        System.out.println("参数数量：" + values.length);
//        if (values == null) {
//            System.err.println("callback ret " + 3);
//            return 3;
//        } else if (values.length == 6) {
//            System.err.println("执行回调event！");
//            return 1;
//        } else {
//            System.err.println("参数数量是" + values.length);
//            return 2;
//        }
//    }
//
//    /**
//     * java回调函数地址
//     */
//    @Override
//    public int getCallbackAddress() throws NativeException {
//        System.out.println("java回调函数地址");
//        return JNative.createCallback(6/*回调函数参数数目*/, this);
//    }
}

