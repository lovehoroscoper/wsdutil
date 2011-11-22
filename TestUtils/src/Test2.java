
public class Test2 {

	static {

		System.loadLibrary("Test2");

	}

	public static native String hello(String msg);

	public static void main(String[] args) {

		String ss = hello("Hello,Kimm!");

		System.out.println(ss);

	}

}
