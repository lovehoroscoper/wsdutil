//package com.caoya.cz.thread;
//
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.biz.JbczBackServiceImplBiz;
//
//public class DetailReadStreamThread implements Runnable {
//	private static Logger logger = Logger.getLogger(DetailReadStreamThread.class);
//
//	private Charset charset = Charset.forName("ISO8859-1");
//
//	private ByteBuffer buffer;
//
//	public DetailReadStreamThread(ByteBuffer buffer) {
//		this.buffer = buffer;
//	}
//
//	@Override
//	public void run() {
//		String msg = "";
//		try {
//			// 不能trim
//			msg = charset.decode(buffer).toString();
//			logger.info("渠道返回[" + msg + "][" + msg.length() + "]");
//			JbczBackServiceImplBiz.getInstance().dealMessage(msg);
//		} catch (Exception e) {
//			logger.error("解析渠道返回异常:" + msg + "|" + e.getMessage());
//		}
//	}
//}
