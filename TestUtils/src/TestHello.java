public class TestHello {

	static {

		System.loadLibrary("TestHello");

	}

	public static native void hello(String msg);

	public static void main(String[] args) {

		hello("Hello,Kimm!");

	}

}
