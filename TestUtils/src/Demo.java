
public class Demo {
	static {
		// System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("testDll");
		// System.loadLibrary("jniDll");
	}

	public native static int add(int a, int b);

}