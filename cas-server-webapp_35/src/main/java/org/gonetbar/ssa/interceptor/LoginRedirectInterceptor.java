package org.gonetbar.ssa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gonetbar.ssa.constant.SystemPropertiesUtils;
import org.jasig.cas.support.oauth.OAuthConstants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.godtips.common.UtilString;

/**
 * 如果登录时候没有附带跳转url那么访问cas server自己信息时候需要再次登录
 * 
 * @author Administrator
 * 
 */
public class LoginRedirectInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		final String acc_url = req.getServletPath();
		final String queryString = req.getQueryString();// 未空
		String nextUrl = "";
		if (redirect && UtilString.isEmptyOrNullByTrim(queryString) && UtilString.notEmptyOrNullByTrim(redirectUrl) && UtilString.notEmptyOrNullByTrim(loginUrl) && loginUrl.equals(acc_url)) {
			nextUrl = req.getContextPath() + redirectUrl;
		} else {
			final String providerType = req.getParameter(OAuthConstants.OAUTH_PROVIDER);
			String usernmae = req.getParameter("username");
			if (UtilString.notEmptyOrNullByTrim(providerType)) {
				String login_status_oauth = SystemPropertiesUtils.globals.getString("LOGIN_STATUS_OAUTH");
				String login_oauth_ids = SystemPropertiesUtils.globals.getString("LOGIN_OAUTH_IDS");
				if ("0".contains(login_status_oauth) && login_oauth_ids.contains("," + providerType + ",")) {

				} else {
					nextUrl = req.getContextPath() + "/loginerror/loginlimit.do?logintype=oauth&providerType=" + providerType;
				}
			} else if (UtilString.notEmptyOrNullByTrim(usernmae)) {
				// 本地登录
				String login_status_local = SystemPropertiesUtils.globals.getString("LOGIN_STATUS_LOCAL");
				String login_local_users = SystemPropertiesUtils.globals.getString("LOGIN_LOCAL_USERS");
				if ("0".equals(login_status_local) || login_local_users.contains(usernmae)) {

				} else {
					// 不支持的登录类型
					nextUrl = req.getContextPath() + "/loginerror/loginlimit.do?logintype=local";
				}
			}
		}
		if (UtilString.notEmptyOrNullByTrim(nextUrl)) {
			res.setContentType("text/html;charset=UTF-8");
			res.sendRedirect(nextUrl);
			return false;
		} else {
			return true;
		}
	}
	
	private boolean redirect;

	private String loginUrl;

	private String redirectUrl;
	
	public final void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}

	public final void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public final void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}