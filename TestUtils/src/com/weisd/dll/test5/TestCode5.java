package com.weisd.dll.test5;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.AbstractBasicData;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

import com.weisd.dll.test2.TestCode;

/**
 * SystemTime
 * 
 * typedef struct _SYSTEMTIME { WORD wYear; WORD wMonth; WORD wDayOfWeek; WORD
 * wDay; WORD wHour; WORD wMinute; WORD wSecond; WORD wMilliseconds; }
 * SYSTEMTIME,
 */
public class TestCode5 extends AbstractBasicData<TestCode5> {

	public static int index;

	public short wYear;
	public short wMonth;
	public short wDayOfWeek;
	public short wDay;
	public short wHour;
	public short wMinute;
	public short wSecond;
	public short wMilliseconds;
	public StringBuffer sb;
	
	static {
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
	}

	// /**
	// * 分配内存，并返回指针
	// */
	// public Pointer createPointer() throws NativeException {
	// pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(getSizeOf()));
	// return pointer;
	// }

	@Override
	public Pointer createPointer() throws NativeException {
		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(20));
		sb = new StringBuffer();
//		pointer.setMemory(sb.toString());
		pointer.setMemory(new byte[4]);
		return pointer;
	}

	/**
	 * 内存大小
	 */
	public int getSizeOf() {
		return 8 * 2;
	}

	/**
	 * 获取通过内存指针解析出结果
	 */
	public TestCode5 getValueFromPointer() throws NativeException {

		System.out.println(pointer);
		
		byte[] b = pointer.getMemory();
		
//		pointer.
		//成功获取返回结果
		System.out.println(new String(b).trim());

		String s = this.getValueAsString();

		Pointer p = this.getPointer();

		System.out.println(new String());

		// this.getNextByte();

		wYear = getNextShort();
		wMonth = getNextShort();
		wDayOfWeek = getNextShort();
		wDay = getNextShort();
		wHour = getNextShort();
		wMinute = getNextShort();
		wSecond = getNextShort();
		wMilliseconds = getNextShort();
		return this;
	}

	public TestCode5() throws NativeException {
		super(null);
		createPointer();
	}

	public String toString() {
		return wYear + "/" + wMonth + "/" + wDay + " at + " + wHour + ":" + wMinute + ":" + wSecond + ":" + wMilliseconds;
	}

	public static void GetSystemTime() throws NativeException, IllegalAccessException {

		TestCode5 systemTime = new TestCode5();
		String filePath_img = "http://upay.10010.com/web/EsfWeb/VerifyCode.action";
//		String filePath_img = "e:\\temp.jpg";
		JNative n = null;
		try {
			n = new JNative("KeyCodeDll.dll", "IGetCode");
			n.setRetVal(Type.INT); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.STRING, "" + filePath_img);
			n.setParameter(i++, systemTime.getPointer());
			n.setParameter(i++, Type.INT, index+"");
			n.invoke(); // 调用方法
			int res = Integer.parseInt(n.getRetVal());
			System.out.println("调用结果:" + res);
		} catch (Exception e) {
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

		systemTime.getValueFromPointer();

	}

	public static void main(String[] args) throws NativeException, IllegalAccessException {
		// System.err.println();
		for (int i = 0; i < 1; i++) {
			
			GetSystemTime();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
