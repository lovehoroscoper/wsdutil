//package com.caoya.cz.biz;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.caoya.cz.constant.MessageRequest;
//import com.caoya.cz.constant.QdInterfaceCode;
//import com.caoya.cz.entity.TaskBean;
//import com.caoya.cz.service.BackService;
//import com.caoya.cz.spring.util.CodeChangeUtil;
//import com.caoya.cz.spring.util.SpringBeanFactoryHolder;
//import com.junbao.hf.utils.common.StringUtils;
//
//public class JbczBackServiceImplBizV2 {
//	private static Logger logger = Logger.getLogger(JbczBackServiceImplBizV2.class);
//
//	private static JbczBackServiceImplBizV2 backServiceImplBiz;
//
//	public static JbczBackServiceImplBizV2 getInstance() {
//		if (backServiceImplBiz == null) {
//			load();
//		}
//		return backServiceImplBiz;
//	}
//
//	public static void load() {
//		String className = "backServiceImplBizV2";
//		logger.info("开始加载渠道处理类配置信息");
//		try {
//			backServiceImplBiz = (JbczBackServiceImplBizV2) SpringBeanFactoryHolder.getBean(className);
//		} catch (Exception e) {
//			logger.error("加载渠道处理类配置文件:" + className + "|error:" + e.getMessage());
//		}
//	}
//
//	public TaskBean dealMessage(List<String> msgList) {
//		try {
//			if (null != msgList && msgList.size() > 0) {
//				for (int i = 0; i < msgList.size(); i++) {
//					String serviceKey = "";
//					String one_msg = msgList.get(i);
//					try {
//						// 根据应答的前 4位判断消息
//						String msgType = one_msg.substring(MessageRequest.len_header, MessageRequest.len_header + 4);
//						serviceKey = "comm_back_" + QdInterfaceCode.getReCommByType(msgType);
//						BackService backService = (BackService) SpringBeanFactoryHolder.getBean(serviceKey);
//						if (null != backService) {
//							backService.dealMessage(one_msg);
//						} else {
//							logger.error("找不到可以执行的impl类:" + serviceKey + "|输入参数:" + one_msg);
//						}
//					} catch (Exception e) {
//						logger.error("解析一个消息[" + serviceKey + "]异常:" + one_msg, e);
//					}
//				}
//			} else {
//				logger.error("切割返回消息为空:");
//			}
//		} catch (Exception e) {
//			logger.error("返回消息异常:");
//		}
//		return null;
//	}
//
//	private List<String> splitMsg(String returnMsg) {
//		List<String> msgList = new ArrayList<String>();
//		int old_msg_length = returnMsg.length();
//		String newStr = returnMsg;
//		String one_msg = "";
//		while (newStr.length() > 3 && !StringUtils.isEmptyOrNullByTrim(newStr)) {
//			try {
//				one_msg = "";
//				int length = newStr.length();// 初始化避免死循环
//				length = CodeChangeUtil.bytes2IntByStringV1(newStr.substring(0, 4), "GB2312");
//				// logger.info("消息长度[" + length + "]真实长度[" + newStr.length() +
//				// "]");
//				one_msg = newStr.substring(0, length);
//				newStr = newStr.substring(length, newStr.length());
//				if (!StringUtils.isEmptyOrNullByTrim(one_msg)) {
//					msgList.add(one_msg);
//				}
//			} catch (Exception e) {
//				// 如遇异常,则说明已到结束,终止循环
//				logger.error("截取消息异常[" + newStr + "][" + newStr.length() + "][" + old_msg_length + "]", e);
//				break;
//			}
//		}
//		return msgList;
//	}
//
//}
