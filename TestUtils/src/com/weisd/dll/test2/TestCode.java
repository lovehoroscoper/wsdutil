package com.weisd.dll.test2;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
 import org.xvolks.jnative.exceptions.NativeException;
 import org.xvolks.jnative.misc.basicStructures.AbstractBasicData;
 import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;
 
 
  /**
  * SystemTime
  * 
  * typedef struct _SYSTEMTIME {
  *     WORD wYear;
  *     WORD wMonth;
  *     WORD wDayOfWeek;
  *     WORD wDay;
  *     WORD wHour;
  *     WORD wMinute;
  *     WORD wSecond;
  *     WORD wMilliseconds;
  * } SYSTEMTIME, 
  */
  public class TestCode extends AbstractBasicData<TestCode> {
 
     public short wYear;
     public short wMonth;
     public short wDayOfWeek;
     public short wDay;
     public short wHour;
     public short wMinute;
     public short wSecond;
     public short wMilliseconds;
 
//      /**
//      * 分配内存，并返回指针
//      */
//      public Pointer createPointer() throws NativeException {
//         pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(getSizeOf()));
//         return pointer;
//     }
     
 	@Override
 	public Pointer createPointer() throws NativeException {
      pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(100));
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
      public TestCode getValueFromPointer() throws NativeException {
    	  
    	  System.out.println(getSizeOf());
    	  System.out.println(pointer);
    	  
    	 String s =  this.getValueAsString();
    	  
    	  Pointer p = this.getPointer();
    	  
    	  
//    	  System.out.println(this.getValueFromPointer());
    	  System.out.println(new String());
    	  
//    	  this.getNextByte();
    	  
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
 
      public TestCode() throws NativeException {
         super(null);
         createPointer();
     }
 
      public String toString() {
         return wYear + "/" + wMonth + "/" + wDay + " at + " + wHour + ":" + wMinute + ":" + wSecond
                 + ":" + wMilliseconds;
     }
 
      public static TestCode GetSystemTime() throws NativeException, IllegalAccessException {
         // 创建对象
//    	  JNative nGetSystemTime = new JNative("Kernel32.dll", "GetSystemTime");
    	  String filePath_library = "E:\\code_sdk\\C++\\wylt.cds";
    	  String filePath_img = "http://bbs.ent.163.com/bbs/checkcode.jsp?1201359681906";
    	  JNative n = new JNative("AntiVC.dll", "LoadCdsFromFile"); // 常量DLL_NAME的值为User32.dll
			n.setRetVal(Type.LONG); // 指定返回参数的类型
			int i = 0;
			n.setParameter(i++, Type.STRING, "" + filePath_library);
			n.invoke(); // 调用方法
			Long load_res = Long.parseLong(n.getRetVal());
			if (1 == load_res.intValue()) {// 加载成功
				
				n = new JNative("AntiVC.dll", "GetVcodeFromURL");
				
				n.setRetVal(Type.INT); // 指定返回参数的类型
				int j = 0;
				String aa = "";
		         TestCode systemTime = new TestCode();
		         byte[] resArr = new byte[]{};
		         // 设置参数
				n.setParameter(0, Type.LONG, "0");
				n.setParameter(1, Type.STRING, "" + filePath_img);
//				n.setParameter(2, Type.STRING, resArr);
				n.setParameter(2, systemTime.getPointer());
				
				n.invoke(); // 调用方法
				String sss = n.getRetVal();
				

		         // 解析结构指针内容
		         return systemTime.getValueFromPointer();
				
			} else {
				System.out.println("加载是别库失败");
				return null;
			}
     }
 
      public static void main(String[] args) throws NativeException, IllegalAccessException {
         System.err.println(GetSystemTime());
     }


 
 }
 
