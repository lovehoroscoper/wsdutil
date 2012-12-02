package com.test;

import org.apache.log4j.Logger;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;

public class So {
	private static Logger logger = Logger.getLogger(So.class);

	public void initial() {
		logger.info("加载验证码识别so文件信息");
		try {
			System.setProperty("jnative.debug", "true");
			System.setProperty("jnative.loadNative", "/usr/lib/libJNativeCpp.so");
		} catch (Exception e) {
			logger.error("/usr/lib/libJNativeCpp.so", e);
		}
		try {
			JNative n = new JNative("libtstts.so", "tsttsInit");
			String pth = "/mydata/app/sopath/";
			n.setRetVal(Type.INT);
			n.setParameter(0, Type.STRING, pth);
			n.invoke();
			String res = n.getRetVal();
			logger.info("验证码识别so初始化结果为:so调用[" + res + "]");
		} catch (Exception e) {
			logger.error("加载验证码识别so文件信息", e);
		}
	}
	//
	// public void initial() {
	// logger.info("加载验证码识别so文件信息");
	// try {
	// System.setProperty("jnative.loadNative", "/usr/lib/libJNativeCpp.so");
	// } catch (Exception e) {
	// logger.error("/usr/lib/libJNativeCpp.so", e);
	// }
	// try {
	// JNative n = new JNative("libmylib.so", "getstring");
	// n.setRetVal(Type.INT);
	// n.invoke();
	// String res = n.getRetVal();
	// logger.info("验证码识别so初始化结果为:so调用[" + res + "]");
	// } catch (Exception e) {
	// logger.error("加载验证码识别so文件信息", e);
	// }
	// }

	//
	// java.lang.IllegalStateException: JNative library not loaded, sorry !
	// at org.xvolks.jnative.JNative.<init>(JNative.java:512)
	// at org.xvolks.jnative.JNative.<init>(JNative.java:440)
	// at com.test.So.initial(So.java:39)
	// at com.test.TextStart.main(TextStart.java:16)
	//

}
