package com.godtips.common;

import java.lang.reflect.Method;
import java.util.Date;

public class UtilCheck {

	/**
	 * 检查对象项是否为NULL
	 * 
	 * @param obj
	 *            检查对象
	 * @param checkname
	 *            对象检查为NULL项(分隔符逗号) 格式为："Name,Age",属性第一个字母大写
	 * @throws CommonException
	 */
	public static void valueIsNull(Object obj, String checkName) {
		StringBuffer sb = new StringBuffer();
		checkValueIsNull(sb,obj,checkName);
		if (sb.toString().endsWith(",")) {
			sb.append(" 不能为NULL");
			throw new IllegalArgumentException(sb.toString());
		}
	}

	/**
	 * 检查对象项是否为NULL
	 * 
	 * @param objs
	 *            检查对象集合
	 * @param checkNames
	 *            对象检查为NULL项(分隔符逗号) 其中每项格式为："Name,Age" ,对象属性第一个字母大写
	 * @throws CommonException
	 */
	public static void valueIsNull(Object[] objs, String[] checkNames) {
		if (objs.length != checkNames.length) {
			throw new IllegalArgumentException(
					"objs's length is not equlas checkNames's length");
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < objs.length; i++) {
			checkValueIsNull(sb, objs[i], checkNames[i]);
		}
		if (sb.toString().endsWith(",")) {
			sb.append(" 不能为NULL");
			throw new IllegalArgumentException(sb.toString());
		}
	}

	/**
	 * 判断对象项是否为NULL
	 * @param sb	
	 * @param obj	 
	 * 			检查对象
	 * @param checkNames
	 * 			对象检查为NULL项(分隔符逗号) 其中每项格式为："Name,Age" ,对象属性第一个字母大写
	 */
	private static void checkValueIsNull(StringBuffer sb, Object obj,
			String checkNames) {
		if (obj == null) {
			sb.append(checkNames + ",");
			return;
		}
		try {
			Class cls = Class.forName(obj.getClass().getName());
			if (!(cls.isPrimitive() || obj instanceof Date
					|| obj instanceof String || obj instanceof Boolean || obj instanceof Number)) {
				Method meth = null;
				Class[] parameterTypes = new Class[] {};
				String chkname[] = checkNames.split(",");
				for (int j = 0; j < chkname.length; j++) {
					String name =chkname[j].substring(0,1).toUpperCase();
					String chename = name +chkname[j].substring(1,chkname[j].length());
					meth = cls.getMethod("get" + chename, parameterTypes);
					Object value = meth.invoke(obj, parameterTypes);
					if (value == null) {
						sb.append(chkname[j] + ",");
					}
				}
			}
			
		} catch (Exception ce) {
			if (ce instanceof  IllegalArgumentException)
				throw (IllegalArgumentException) ce;
			ce.printStackTrace();// ZW
		}
	}
	
	/**
	 * 给字符串去空
	 * @param string
	 * @return
	 */
	public static String dealNull(String string){
		if(null == string || string.equals("")){
		   string = "";
		}else{
		   string = string.trim();
		}
		return string;
	}
	
	/**
	 * 判断报文中,特定子串是否存在
	 * @param message     报文
	 * @param submessage  子串            如"comm"
	 * @param symbol      子串符号  如"="
	 * @return
	 */
	public static boolean isExistString(String message,String submessage,String symbol){
	    if(dealNull(message).equals("")){
		   return false;
		}
		else{
			if(message.contains(submessage+(dealNull(symbol).equals("")?"=":symbol))){
		       return true;
			}
		}
	    return false;
	}
	
	/**
	 * 获取报文中特定子串的值
	 * @param message     报文
	 * @param submessage  子串
	 * @param symbol      子串连接符号
	 * @param split       子串分隔符
	 * @return
	 */
	public static String getSubString(String message,String submessage,String symbol,String split){
		String substring = "";
		if(!dealNull(message).equals("")){
			symbol = dealNull(symbol).equals("")?"=":symbol;
			split = dealNull(split).equals("")?"&":split;
			// 子串存在
			if(isExistString(message,submessage,symbol)){
				String[] array = message.split(split);
				if(array.length>0){
					for(String s : array){
						if(!dealNull(s).equals("") && s.contains(submessage+symbol)){
							try{
								substring = s.split(symbol)[1];
							}catch(ArrayIndexOutOfBoundsException e){
							}
							break;
						}
					}
				}
			}
		}
		return dealNull(substring);
	}
	
	/**
	 * String req = "&comm=8001&version=1.0&hforderid123=JB011105191819341809&orderid=JB0000030&hforderid=JB011105191819341809";
	 * 
	 * hforderid
	 * orderid
	 * 
	 * 获取报文中特定子串的值
	 * @param message     报文
	 * @param submessage  子串
	 * @param symbol      子串连接符号
	 * @param split       子串分隔符
	 * @return
	 */
	public static String getSubStringStartsWith(String message,String submessage,String symbol,String split){
		String substring = "";
		if(!dealNull(message).equals("")){
			symbol = dealNull(symbol).equals("")?"=":symbol;
			split = dealNull(split).equals("")?"&":split;
			// 子串存在
			if(isExistString(message,submessage,symbol)){
				String[] array = message.split(split);
				if(array.length>0){
					for(String s : array){
						//String req = "&comm=8001&version=1.0&hforderid=JB011105191819341809&orderid=JB0000030";
						//hforderid orderid 获取一样了
						if(!dealNull(s).equals("") && s.contains(submessage+symbol)){
							if(s.startsWith(submessage+symbol)){//完整的开头
								try{
									substring = s.split(symbol)[1];
								}catch(ArrayIndexOutOfBoundsException e){
								}
								break;
							}
						}
					}
				}
			}
		}
		return dealNull(substring);
	}
	
	/**
	 * String req = "&comm=8001&version=1.0&hforderid=JB011105191819341809&orderid=JB0000030";
	 * 
	 * hforderid
	 * orderid
	 * 
	 * 针对加密的 卡密 String aa = "cardnum=1110011170693991973&cardpwd=XKGpqYK3XOwg4J/upQeOhBDJa=m/XSgbS"; 
	 * 
	 * 机密串包含=
	 * @param message     报文
	 * @param submessage  子串
	 * @param symbol      子串连接符号
	 * @param split       子串分隔符
	 * @return
	 */
	public static String getSubStringStartsWithPWD(String message,String submessage,String symbol,String split){
	   String substring = "";
	   if(!dealNull(message).equals("")){
		   symbol = dealNull(symbol).equals("")?"=":symbol;
		   split = dealNull(split).equals("")?"&":split;
		   // 子串存在
		   if(isExistString(message,submessage,symbol)){
			 String[] array = message.split(split);
			 if(array.length>0){
				for(String s : array){
					String strBeg = submessage + symbol;
					if(!dealNull(s).equals("") && s.contains(strBeg)){
						if(s.startsWith(strBeg)){//完整的开头
							try{
								//cardpwd=XKGpqYK3XOwg4J/upQeOhBDJa=m/XSgbS"
								substring = s.substring(strBeg.length(), s.length());
							}catch(ArrayIndexOutOfBoundsException e){
							}
							break;
						}
					}
				}
			 }
		   }
	   }
	   return dealNull(substring);
	}

	public static void main(String[] args) {
		String aa = "cardnum=1110011170693991973&cardpwd=XKGpqYK3XOwg4J/upQeOhBDJa=m/XSgbS";
		String comm = UtilCheck.getSubStringStartsWithPWD(aa, "cardpwd", "=", "&");
		String ss = comm;
		
		System.out.println(ss);
	}

}
