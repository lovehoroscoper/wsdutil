package org.gonetbar.ssa.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gonetbar.ssa.constant.UserLoginAttr;
import org.gonetbar.ssa.cookie.CacheUserCookieGenerator;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.LogoutCacheService;
import org.gonetbar.ssa.util.LgotucaCookie;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.godtips.common.UtilString;

/**
 * 
 * 清除缓存里面的内容
 * 
 * 希望这个handler能被2个登出地方使用:
 * 
 * 1、[/logout] LogoutController : ticketGrantingTicketId
 * 
 * 2、[/services/logout.html] LogoutFilter : HttpSession
 * 
 * @author Administrator
 * 
 */
public class CopyOfLogoutCacheHandler implements LogoutHandler {
	protected final Log logger = LogFactory.getLog(this.getClass());

	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
		String ticketGrantingTicketId = "";
		String user_local_uniquekey = "";
		String third_login_providerid = "";
		String user_third_uniquekey = "";
		String lgotucaCookieValue = "";
		try {
			if (null != auth) {
				try {
					if (auth instanceof CasAuthenticationToken) {
						CasAuthenticationToken cas_auth = (CasAuthenticationToken) auth;
						org.jasig.cas.client.validation.Assertion ass = cas_auth.getAssertion();
						if (null != ass) {
							AttributePrincipal attrPri = ass.getPrincipal();
							if (null != attrPri) {
								Map<String, Object> attr_map = attrPri.getAttributes();
								if (null != attr_map) {
									third_login_providerid = UtilString.getStringFromEmpty((String) attr_map.get(UserLoginAttr.THIRD_LOGIN_PROVIDERID));
									user_third_uniquekey = UtilString.getStringFromEmpty((String) attr_map.get(UserLoginAttr.USER_THIRD_UNIQUEKEY));
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("logout查找CasAuthenticationToken异常", e);
				}
			}
			if (UtilString.isEmptyOrNullByTrim(user_local_uniquekey) || UtilString.isEmptyOrNullByTrim(third_login_providerid) || UtilString.isEmptyOrNullByTrim(user_third_uniquekey)) {
				// 借助TGT获取
				logger.error("无法清除用户缓存,必须借助TGT获取");
				try {
					// 这个则是登录cas-client
					ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
				} catch (Exception e) {
					logger.error("logout查找ticketGrantingTicketId异常", e);
				}
				try {
					//
					lgotucaCookieValue = cacheUserCookieGenerator.retrieveCookieValue(request);
				} catch (Exception e) {
					logger.error("logout查找lgotuca异常", e);
				}
				if (UtilString.notEmptyOrNullByTrim(lgotucaCookieValue) && UtilString.notEmptyOrNullByTrim(ticketGrantingTicketId)) {
					UserProviderInfoVo co_vo = LgotucaCookie.decodeCookieValue(lgotucaCookieValue, ticketGrantingTicketId);
					user_local_uniquekey = co_vo.getUsername();
					third_login_providerid = co_vo.getProviderid();
					user_third_uniquekey = co_vo.getThirduserid();
				}
			}
			logoutCacheService.removeLoginCacheBy(user_local_uniquekey, third_login_providerid, user_third_uniquekey);
		} catch (Exception e) {
			logger.error("清除用户缓存最外层异常", e);
		} finally {
			cacheUserCookieGenerator.removeCookie(response);
		}
	}

	private LogoutCacheService logoutCacheService;

	private CacheUserCookieGenerator cacheUserCookieGenerator;

	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	public void setTicketGrantingTicketCookieGenerator(final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public void setLogoutCacheService(LogoutCacheService logoutCacheService) {
		this.logoutCacheService = logoutCacheService;
	}

	public void setCacheUserCookieGenerator(CacheUserCookieGenerator cacheUserCookieGenerator) {
		this.cacheUserCookieGenerator = cacheUserCookieGenerator;
	}

}
