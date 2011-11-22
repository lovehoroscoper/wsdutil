package com.weisd.dll;
import org.xvolks.jnative.JNative;
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
  public class SystemTime extends AbstractBasicData<SystemTime> {
 
     public short wYear;
     public short wMonth;
     public short wDayOfWeek;
     public short wDay;
     public short wHour;
     public short wMinute;
     public short wSecond;
     public short wMilliseconds;
 
      /**
      * 分配内存，并返回指针
      */
      public Pointer createPointer() throws NativeException {
         pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(getSizeOf()));
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
      public SystemTime getValueFromPointer() throws NativeException {
    	  
    	  
    	  
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
 
      public SystemTime() throws NativeException {
         super(null);
         createPointer();
     }
 
      public String toString() {
         return wYear + "/" + wMonth + "/" + wDay + " at + " + wHour + ":" + wMinute + ":" + wSecond
                 + ":" + wMilliseconds;
     }
 
      public static SystemTime GetSystemTime() throws NativeException, IllegalAccessException {
         // 创建对象
         JNative nGetSystemTime = new JNative("Kernel32.dll", "GetSystemTime");
         SystemTime systemTime = new SystemTime();
         // 设置参数
         nGetSystemTime.setParameter(0, systemTime.getPointer());
         nGetSystemTime.invoke();
         // 解析结构指针内容
         return systemTime.getValueFromPointer();
     }
 
      public static void main(String[] args) throws NativeException, IllegalAccessException {
         System.err.println(GetSystemTime());
     }
 
 }
 
