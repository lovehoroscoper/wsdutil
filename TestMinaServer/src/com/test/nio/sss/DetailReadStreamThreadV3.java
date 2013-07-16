//package com.caoya.cz.thread;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.biz.JbczBackServiceImplBizV3;
//
//public class DetailReadStreamThreadV3 implements Runnable {
//	private static Logger logger = Logger.getLogger(DetailReadStreamThreadV3.class);
//
//	private ByteArrayOutputStream bos;
//
//	public DetailReadStreamThreadV3(ByteArrayOutputStream bos) {
//		this.bos = bos;
//	}
//
//	@Override
//	public void run() {
//		String msg = "";
//		try {
//			if (null != bos && bos.size() > 0) {
//				msg = new String(bos.toByteArray(), "ISO8859-1");
//				logger.info("渠道返回[" + msg + "][" + msg.length() + "]");
//				JbczBackServiceImplBizV3.getInstance().dealMessage(msg);
//			} else {
//				logger.info("渠道返回空或者连接已经关闭!");
//			}
//		} catch (Exception e) {
//			logger.error("解析渠道返回异常:" + msg + "|" + e.getMessage());
//		} finally {
//			if (null != bos) {
//				try {
//					bos.close();
//					bos = null;
//				} catch (IOException e) {
//
//				}
//			}
//		}
//	}
//
//}
