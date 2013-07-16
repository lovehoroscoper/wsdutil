//package com.caoya.cz.thread;
//
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.biz.JbczBackServiceImplBizV2;
//
//public class DetailReadStreamThreadV2 implements Runnable {
//	private static Logger logger = Logger.getLogger(DetailReadStreamThreadV2.class);
//
//	private List<String> list;
//
//	public DetailReadStreamThreadV2(List<String> list) {
//		this.list = list;
//	}
//
//	@Override
//	public void run() {
//		try {
//			JbczBackServiceImplBizV2.getInstance().dealMessage(list);
//		} catch (Exception e) {
//			logger.error("解析渠道返回异常:" + e.getMessage());
//		}
//	}
//}
