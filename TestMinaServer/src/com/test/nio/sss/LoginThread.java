//package com.caoya.cz.thread;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.biz.SocketHelp;
//
///**
// * 为什么启用线程登录,因为登录时候要等到很长时间会阻塞读取数据的主线程
// * 
// * @author Administrator
// * 
// */
//public class LoginThread extends Thread {
//
//	private static Logger logger = Logger.getLogger(LoginThread.class);
//
//	public void run() {
//		logger.info("登录线程1秒后执行登录");
//		try {
//			sleep(1 * 1000);
//			String res = SocketHelp.login("0");
//			logger.info("执行登录结果:" + res);
//		} catch (Exception e) {
//			logger.info("登录线程登录异常" + e.getMessage());
//		}
//	}
//
//}
