package com.godtips.common;

import javax.servlet.http.HttpServletRequest;


/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-5-18 下午2:56:16
 */
public class RequestUtil {

	public static String getParam(HttpServletRequest request, String paramName, String def) {
		String value = request.getParameter(paramName);
		return value == null ? def : value.trim();
	}

}
