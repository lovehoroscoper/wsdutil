package com.weisd.dll;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

public class JNativeTest {
    
    public static final int messageBox(int parentHandle, String message,
             String caption, int buttons) throws NativeException,
             IllegalAccessException {
          JNative n = null;
          try {
             n = new JNative("User32.dll", "MessageBoxA"); // 常量DLL_NAME的值为User32.dll
             // 构造JNative时完成装载User32.dll,并且定位MessageBoxA方法
             n.setRetVal(Type.INT); // 指定返回参数的类型
             int i = 0;
             n.setParameter(i++, Type.INT, "" + parentHandle);
             n.setParameter(i++, Type.STRING, message);
             n.setParameter(i++, Type.STRING, caption);
             n.setParameter(i++, Type.INT, "" + buttons); // 指定位置上的参数类型和值
             n.invoke(); // 调用方法
             return Integer.parseInt(n.getRetVal());
          } finally {
             if (n != null)
                n.dispose(); // 记得释放
          }
       }
    public static void main(String[] args) throws NativeException, IllegalAccessException{
        JNativeTest.messageBox(100,"Hello jnative", "jnativetest", 1);
    }
}
 

