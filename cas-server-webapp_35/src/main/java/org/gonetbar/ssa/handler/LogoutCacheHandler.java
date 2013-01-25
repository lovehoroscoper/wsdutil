package org.gonetbar.ssa.handler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gonetbar.ssa.constant.UserLoginAttr;
import org.gonetbar.ssa.service.LogoutCacheService;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
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
public class LogoutCacheHandler implements LogoutHandler {
	protected final Log logger = LogFactory.getLog(this.getClass());

	private static final String logout_client = "/logout";

	private static final String logout_server = "/services/logout.html";

	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
		String ticketGrantingTicketId = "";
		String user_local_uniquekey = "";
		String third_login_providerid = "";
		String user_third_uniquekey = "";
		String auth_TGT = "";

		String url = UtilString.getStringFromEmpty(getRequestPath(request));

		try {
			try {
				// 这个则是登录cas-client
				ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
			} catch (Exception e) {
				logger.error("logout查找ticketGrantingTicketId异常", e);
			}
			if (null != auth) {
				Object objCre = auth.getCredentials();
				if (null != objCre) {
					auth_TGT = objCre.toString();
				}
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

				System.out.println("end");

			}
			if (UtilString.isEmptyOrNullByTrim(user_local_uniquekey) || UtilString.isEmptyOrNullByTrim(third_login_providerid) || UtilString.isEmptyOrNullByTrim(user_third_uniquekey)) {
				// 借助TGT获取
				logger.error("无法清除用户缓存,必须借助TGT获取");
				if (UtilString.notEmptyOrNullByTrim(ticketGrantingTicketId)) {
					// 查找关联的
					final TicketGrantingTicket ticket = (TicketGrantingTicket) this.ticketRegistry.getTicket(ticketGrantingTicketId, TicketGrantingTicket.class);
					if (null != ticket) {
						final List<org.jasig.cas.authentication.Authentication> authentications = ticket.getChainedAuthentications();
						Map<String, Object> attr_map_tgt = authentications.get(authentications.size() - 1).getPrincipal().getAttributes();
						if (null != attr_map_tgt) {
							third_login_providerid = UtilString.getStringFromEmpty((String) attr_map_tgt.get(UserLoginAttr.THIRD_LOGIN_PROVIDERID));
							user_third_uniquekey = UtilString.getStringFromEmpty((String) attr_map_tgt.get(UserLoginAttr.USER_THIRD_UNIQUEKEY));
						}
					}
				}

			}
			logoutCacheService.removeLoginCacheBy(user_local_uniquekey, third_login_providerid, user_third_uniquekey);

			if (logout_server.equals(url.toLowerCase())) {
				// 清除TGT
				logger.error("而外清除TGT");
			}
		} catch (Exception e) {
			logger.error("清除用户缓存最外层异常", e);
		}
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();
		if (request.getPathInfo() != null) {
			url += request.getPathInfo();
		}
		url = url.toLowerCase();
		return url;
	}

	private LogoutCacheService logoutCacheService;

	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	private TicketRegistry ticketRegistry;

	public void setTicketGrantingTicketCookieGenerator(final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public final void setLogoutCacheService(LogoutCacheService logoutCacheService) {
		this.logoutCacheService = logoutCacheService;
	}

	public final void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}

}
