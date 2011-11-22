package cn.imu.jni;



public final class MyFileTime {

	/**
	 * 只加载一次MyFileTime.dll
	 * @param args
	 */
	static {
        System.loadLibrary("MyFileTime");
    }

	/**
	 * 调用入口dll（该路口经过c++再次封装）
	 * @param fileName
	 * @return
	 */
    private static native String getFileCreationTime(String fileName);

    public static String getCreationTime(String fileName) {
        return getFileCreationTime(fileName);
    }
    
    public static void main(String[] args) {
    	for (int i = 0; i < 100; i++) {
    		/*
    		 * 这里会循环调用方法
    		 * MyFileTime.dll
    		 * 但是MyFileTime.dll中的Test_DLL2.dll是否会多次调用
    		 */
        	String ss = MyFileTime.getCreationTime("");
        	System.out.println(ss);
		}
    }

}