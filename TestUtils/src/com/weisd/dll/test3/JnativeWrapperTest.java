//package com.weisd.dll.test3;
//
//import javax.swing.JOptionPane;
//
//import org.xvolks.jnative.JNative;
//import org.xvolks.jnative.Type;
//import org.xvolks.jnative.exceptions.NativeException;
//import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;
//import org.xvolks.jnative.util.Callback;
//import org.xvolks.jnative.util.User32;
//
//public class JnativeWrapperTest extends JNative {
//
//	static String DLL_NAME = "Reader_dll";
//	JnaWrapper n = null;
//
//	/**
//	 * @JNA_TRANS表明这个方法在JnativeWrapper容器中 由JnativeWrapper自动关闭jnative连接
//	 * @return int
//	 */
//	@JNA_TRANS
//	public int readerInit() {
//
//		int rev = -1;
//		try {
//			n = super.i.initJna(DLL_NAME, "ReaderInit");
//			n.setRetVal(Type.INT); // 指定返回参数的类型
//			int i = 0;
//			n.setParameterInt(i++, 0);
//			// 指定串口
//			n.setParameterString(i++, "com1");
//			// 波特率
//			n.setParameterInt(i++, 9600);
//			// 指定usb口
//			n.setParameterString(i++, "usb1"); // 指定位置上的参数类型和值
//			// 调用方法
//			// 0 成功 -1 失败
//			rev = Integer.parseInt(n.getRetVal());
//			if (rev == 0) {
//				System.out.println("==============初始化识读头的串口成功==============");
//			} else if (rev == -1) {
//				System.out.println("==============初始化识读头的串口失败==============");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return rev;
//	}
//
//	/**
//	 * @JNA_TRANS表明这个方法在JnativeWrapper容器中 由JnativeWrapper自动关闭jnative连接
//	 * @return boolean
//	 */
//
//	@JNA_TRANS
//	public boolean EnumWindows(Callback lpEnumFunc, int lParam)
//			throws NativeException, IllegalAccessException {
//		n = super.initJna("User32.dll", "EnumWindows");
//		n.setRetVal(Type.INT);
//
//		n.setParameterCallback(0, lpEnumFunc.getCallbackAddress());
//		n.setParameterInt(1, lParam);
//		;
//		return !"0".equals(n.getRetVal());
//	}
//
//	public void testCallback() {
//		try {
//			User32.MessageBox(0, "Callback Demo", "JNative", 0);
//		} catch (NativeException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		MyEnumCallback c = new MyEnumCallback();
//
//		try {
//			MemoryBlockFactory.createMemoryBlock(1).dispose();
//			if (EnumWindows(c, 0)) {
//				System.err.println("EnumWindows suceeded");
//			} else {
//				System.err.println("EnumWindows failed");
//			}
//			System.err.println("getAvailableCallbacks "
//					+ JNative.getAvailableCallbacks());
//			JNative.releaseCallback(c);
//			System.err.println(c.getCallbackDataPackage());
//			User32.MessageBox(0, c.getWindowEnumList(), "窗体枚举(EnumWindows)", 0);
//			JOptionPane.showMessageDialog(null, c.getWindowEnumList(),
//					"窗体枚举(EnumWindows)", JOptionPane.OK_OPTION);
//
//		} catch (NativeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	private interface MyFactory {
//		public Object newInstance(int a, char[] b, String d);
//	}
//
//	public static void main(String[] args) {
//		/**
//		 * JnativeWrapperTest 要继承JnativeBase 通过JnaWrapperProxy返回一个代理实例
//		 */
//		 JnativeWrapperTest
//		 test=(JnativeWrapperTest)JnaWrapperHandler.getJnaWrapperProxy(JnativeWrapperTest.class);
//		// test.readerInit();
//		  test.testCallback();
//
//	}
//
//}