//package com.test.client;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ResultCodeInformation {
//
//	/**
//	 * 每个判断里面的设置参数可能不一致
//	 * 
//	 * 怎么避免这种 if  else
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	public static String getSystemErrorReturnMsg(String msg) {
//		String comm = "";
//		String result = "";
//		Map<String, String> map = new HashMap<String, String>();
//		if (InterfaceCode.ORDER.equals(comm)) {
//			comm = InterfaceCode.ORDER_RETURN;
//			map.put("test1", "11");
//			map.put("test12", "22");
//			map.put("test13", "33");
//			map.put("test14", "44");
//		} else if (InterfaceCode.ORDERSEARCH.equals(comm)) {
//			comm = InterfaceCode.ORDERSEARCH_RETURN;
//			map.put("test2", "");
//			map.put("test22", "");
//			map.put("test24", "");
//		} else if (InterfaceCode.CHARGE.equals(comm)) {
//			comm = InterfaceCode.CHARGE_RETURN;
//			map.put("test3", "");
//			map.put("test34", "");
//			
//		// 这里可能还有更多选择省略
//		} else {
//			comm = CheckUtil.getSubString(msg, "comm", "=", "&");
//			map.put("test4", "");
//			map.put("test42", "");
//			map.put("test43", "");
//			map.put("test44", "");
//		}
//		return map;
//	}
//
//}
