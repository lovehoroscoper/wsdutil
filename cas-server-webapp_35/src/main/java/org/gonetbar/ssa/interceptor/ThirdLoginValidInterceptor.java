package org.gonetbar.ssa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gonetbar.ssa.constant.SystemPropertiesUtils;
import org.jasig.cas.support.oauth.OAuthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.godtips.common.UtilRegex;
import com.godtips.common.UtilString;

/**
 * 第三方登录来源验证
 * 
 * @author Administrator
 * 
 */
public class ThirdLoginValidInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(ThirdLoginValidInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		String providerType = req.getParameter(OAuthConstants.OAUTH_PROVIDER);
		String nextUrl = req.getContextPath() + "/loginerror/loginlimit.do?logintype=oauth&providerType=" + providerType;
		String cur_ip = null;
		if (UtilString.notEmptyOrNullByTrim(providerType)) {
			cur_ip = getIp(req);
			String limit_ips = SystemPropertiesUtils.globals.getString("THIRD_CALLBACK_LIMIT_IPS_" + providerType);
			if (UtilString.notEmptyOrNullByTrim(cur_ip) && UtilString.notEmptyOrNullByTrim(limit_ips)) {
				if ("-1".equals(limit_ips) || (UtilRegex.checkIP(cur_ip) && limit_ips.contains("," + cur_ip + ","))) {
					nextUrl = null;
				}
			}
		} else {
			nextUrl = null;
		}
		if (UtilString.notEmptyOrNullByTrim(nextUrl)) {
			logger.error("第三方[" + providerType + "]回调[" + cur_ip + "]IP来源受限制");
			res.setContentType("text/html;charset=UTF-8");
			res.sendRedirect(nextUrl);
			return false;
		} else {
			return true;
		}
	}

	private String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (UtilString.isEmptyOrNullByTrim(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (UtilString.isEmptyOrNullByTrim(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (UtilString.isEmptyOrNullByTrim(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}