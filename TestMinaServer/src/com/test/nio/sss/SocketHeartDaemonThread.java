//package com.caoya.cz.thread;
//
//import java.text.ParseException;
//import java.util.Date;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.biz.SocketHelp;
//import com.caoya.cz.constant.MessageResponseDone;
//import com.caoya.cz.entity.TaskBean;
//import com.caoya.cz.service.IService;
//import com.caoya.cz.spring.util.SpringBeanFactoryHolder;
//import com.hisunsray.commons.res.Config;
//import com.junbao.hf.utils.common.CheckUtil;
//import com.junbao.hf.utils.common.DateUtils;
//
///**
// * 心跳只用来维持长连接
// * 
// * @author Administrator
// * 
// */
//public class SocketHeartDaemonThread extends Thread {
//
//	private static Logger logger = Logger.getLogger(SocketHeartDaemonThread.class);
//	private boolean isInterrupted = false;
//
//	public void interrupt() {
//		isInterrupted = true;
//		super.interrupt();
//	}
//
//	public void run() {
//		try {
//			logger.info("系统启动30秒后开始运行心跳程序");
//			Thread.sleep(30 * 1000);
//		} catch (Exception e2) {
//			logger.error("启动睡眠异常:" + e2.getMessage());
//		}
//		logger.info("系统启动30秒睡眠结束运行心跳程序");
//		String interval = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//		while (!isInterrupted) {
//			try {
//				String now_interval = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//				long conn_wait = Long.valueOf(Config.getProperty("SOCKET_CONN_WAIT"));
//				if (checkTime(interval, now_interval) > conn_wait) {
//					logger.error("心跳判断长连接超时[" + interval + "]--[" + now_interval + "]准备重连");
//					try {
//						SocketHelp.connect();
//						interval = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//						logger.error("心跳判断长连接超时[" + interval + "]--[" + now_interval + "]重连完毕");
//						LoginThread login = new LoginThread();
//						login.start();
//					} catch (Exception e) {
//						String time = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//						MessageResponseDone.createResultLoginMsg("", "0", "no", "心跳中socket重连异常后登录失败", time, "0", "");
//						logger.error("心跳判断长连接超时[" + interval + "]--[" + now_interval + "]异常");
//					}
//				}
//			} catch (Exception e) {
//				logger.error("原有socket连接等待异常", e);
//			}
//			try {
//				long start = new Date().getTime();
//				long sleep = Long.valueOf(Config.getProperty("HEARTBEAT_FREQUENCY"));
//				String res = "";
//				try {
//					IService balanceService = (IService) SpringBeanFactoryHolder.getBean("comm_8012");
//					TaskBean taskBean = new TaskBean();
//					taskBean.setReq("comm=8012&version=1.0");
//					taskBean = balanceService.dealMessage(taskBean);
//					if (null != taskBean) {
//						res = taskBean.getRes();
//					}
//				} catch (Exception e) {
//					logger.error("心跳异常异常", e);
//				}
//				long end = new Date().getTime();
//				long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
//				long nh = 1000 * 60 * 60;// 一小时的毫秒数
//				// long nm = 1000 * 60;// 一分钟的毫秒数
//				long nn = 1000;// 秒数
//				long min = (end - start) % nd % nh / nn;// 计算差多少分钟
//				long dif = sleep - min;
//				String state = CheckUtil.getSubStringStartsWith(res, "state", "=", "&");
//				if (!"0".equals(state)) {
//					logger.error("心跳查询失败[" + state + "]用时[" + min + "]继续睡眠[" + dif + "][" + start + " -- " + end + "]" + res);
//				} else {
//					// 心跳成功设置时间
//					logger.info("心跳成功,最后一次无数据间隔[" + interval + "]");
//					//interval = DateUtils.getFormatCurrDate("yyyyMMddHHmmss");
//				}
//				if (dif > 0) {
//					Thread.sleep(dif * 1000);
//				} else {
//					logger.info("等待时间过长心跳不再睡眠:" + dif);
//				}
//			} catch (Exception e) {
//				logger.error("心跳时间异常:" + e.getMessage());
//				try {
//					Thread.sleep(90 * 1000);
//				} catch (Exception e1) {
//					logger.error("睡眠异常:" + e1.getMessage());
//				}
//			}
//		}
//	}
//
//	private long checkTime(String startTime, String endTime) throws ParseException {
//		Long start = DateUtils.parseDate(startTime, "yyyyMMddHHmmss").getTime();
//		Long end = DateUtils.parseDate(endTime, "yyyyMMddHHmmss").getTime();
//		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
//		long nh = 1000 * 60 * 60;// 一小时的毫秒数
//		long ns = 1000;// 一分钟的毫秒数
//		long min = (end - start) % nd % nh / ns;// 计算差多少秒
//		return min;
//	}
//
//}
