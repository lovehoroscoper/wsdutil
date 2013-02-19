package org.gonetbar.ssa.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-2-19 上午9:31:18
 */
public class ShowUtils {

	private static final Log logger = LogFactory.getLog(ShowUtils.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void showCookies(HttpServletRequest request,String site) {
		String uri = request.getRequestURI();
		logger.warn("-----start------显示cookie---------[" + uri + "][" + site + "]-----");
		try {
			Cookie[] cookies = request.getCookies();
			if (null != cookies && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie c = cookies[i];
					logger.info("一个cookie[" + c.getName() + "][" + c.getValue() + "]");
				}
			} else {
				logger.warn("cookies 为空");
			}
		} catch (Exception e) {
			logger.error("输出显示cookie异常", e);
		}
		logger.warn("-----end------显示cookie---------[" + uri + "][" + site + "]-----");
	}

}
