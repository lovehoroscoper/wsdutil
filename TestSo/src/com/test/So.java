package com.test;

import org.apache.log4j.Logger;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;

public class So {
	private static Logger logger = Logger.getLogger(So.class);

	public void initial() {
		logger.info("进入-----------initial------");
		try {
			System.setProperty("jnative.loadNative", "/usr/lib/libJNativeCpp.so");
		} catch (Exception e) {
			logger.error("/usr/lib/libJNativeCpp.so", e);
		}
		logger.info("完成设置--------System.setProperty----");

		// 纯c写到动态库
		logger.info("------clib  开始 ------");
		JNative clib = null;
		try {
			clib = new JNative("libtest.so", "test_a"); // 调用libtest.so下到test_a函数
		} catch (Exception e) {
			logger.error("------new JNative----- libtest.so-------异常------", e);
		}
		if (null != clib) {
			try {
				clib.setRetVal(Type.STRING); // 设置此函数的返回值
				clib.invoke(); // 函数执行
				logger.info("clib---result-----[" + clib.getRetVal() + "]");
			} catch (Exception e) {
				logger.error("-------clib.invoke()---------异常-------", e);
			}
		} else {
			logger.error("------clib  null ------");
		}
		logger.info("------clib  结束------");

		logger.info("------getstring  开始 ------");
		JNative getstring = null;
		try {
			getstring = new JNative("libmylib.so", "getstring");
		} catch (Exception e) {
			logger.error("------new JNative----- libmylib.so-------异常------", e);
		}
		if (null != getstring) {
			try {
				getstring.setRetVal(Type.STRING);
				getstring.invoke();
				logger.info("getstring----result----[" + getstring.getRetVal() + "]");
			} catch (Exception e) {
				logger.error("-------getstring.invoke()---------异常-------", e);
			}
		} else {
			logger.error("------getstring  null------");
		}

		logger.info("------getstring  结束------");
		logger.info("结束-----------initial------");
	}

	// public void initial() {
	// logger.info("进入-----------initial------");
	// try {
	// System.setProperty("jnative.loadNative", "/usr/lib/libJNativeCpp.so");
	// } catch (Exception e) {
	// logger.error("/usr/lib/libJNativeCpp.so", e);
	// }
	// logger.info("完成设置--------System.setProperty----");
	// JNative n = null;
	// try {
	// n = new JNative("libmylib.so", "getstring");
	// } catch (Exception e) {
	// logger.error("----------- new JNative-------异常------", e);
	// }
	// if (null != n) {
	// logger.error("初始化------- JNative---不为空");
	// try {
	// n.setRetVal(Type.STRING);
	// n.invoke();
	// String res = n.getRetVal();
	// logger.info("n.invoke():so调用[" + res + "]");
	// } catch (Exception e) {
	// logger.error("----------n.invoke()----------", e);
	// }
	// } else {
	// logger.error("初始化------- JNative---为空");
	// }
	// logger.info("结束-----------initial------");
	// }

	// public void initial_() {
	// logger.info("加载验证码识别so文件信息");
	// try {
	// System.setProperty("jnative.debug", "true");
	// System.setProperty("jnative.loadNative", "/usr/lib/libJNativeCpp.so");
	// } catch (Exception e) {
	// logger.error("/usr/lib/libJNativeCpp.so", e);
	// }
	// JNative n = null;
	// try {
	// n = new JNative("libtstts.so", "tsttsInit");
	// } catch (Exception e) {
	// logger.error("----------- new JNative-------------", e);
	// }
	// if (null != n) {
	// try {
	// String pth = "/app/testso/datedir/";
	// n.setRetVal(Type.INT);
	// n.setParameter(0, Type.STRING, pth);
	// n.invoke();
	// String res = n.getRetVal();
	// logger.info("n.invoke():so调用[" + res + "]");
	// } catch (Exception e) {
	// logger.error("----------n.invoke()----------", e);
	// }
	// } else {
	// logger.error("初始化------- JNative---为空");
	// }
	//
	// }
}
