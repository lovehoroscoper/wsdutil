package org.gonetbar.ssa.cookie;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.gonetbar.ssa.constant.UserLoginAttr;
import org.gonetbar.ssa.util.LgotucaCookie;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.godtips.common.UtilString;

public final class AddLoginTicketCookieAction extends AbstractAction {

	private static final Logger logger = LoggerFactory.getLogger(AddLoginTicketCookieAction.class);

	private TicketRegistry ticketRegistry;

	@NotNull
	private CacheUserCookieGenerator cacheUserCookieGenerator;

	protected Event doExecute(final RequestContext context) {
		final String loginTGT = WebUtils.getTicketGrantingTicketId(context);
		if (UtilString.isEmptyOrNullByTrim(loginTGT)) {
			return success();
		}
		String user_local_uniquekey = "";
		String third_login_providerid = "";
		String user_third_uniquekey = "";
		try {
			final TicketGrantingTicket ticket = (TicketGrantingTicket) this.ticketRegistry.getTicket(loginTGT);
			if (null != ticket) {
				Authentication auth = ticket.getAuthentication();
				if (null != auth) {
					Principal pri = auth.getPrincipal();
					if (null != pri) {
						user_local_uniquekey = pri.getId();
						Map<String, Object> sp_m = pri.getAttributes();
						third_login_providerid = UtilString.getStringFromEmpty((String) sp_m.get(UserLoginAttr.THIRD_LOGIN_PROVIDERID));
						user_third_uniquekey = UtilString.getStringFromEmpty((String) sp_m.get(UserLoginAttr.USER_THIRD_UNIQUEKEY));
					}
				}
			}
		} catch (Exception e) {
			logger.error("登录成功后添加用户信息cookie异常", e);
		}
		if (UtilString.notEmptyOrNullByTrim(user_local_uniquekey)) {
			try {
				final HttpServletResponse response = WebUtils.getHttpServletResponse(context);
				String cookieValue = LgotucaCookie.encodeCookieValue(user_local_uniquekey, third_login_providerid, user_third_uniquekey, loginTGT);
				cacheUserCookieGenerator.addCookie(response, cookieValue);
			} catch (Exception e) {
				logger.error("登录成功后写入cookie异常", e);
			}
		} else {
			logger.error("登录成功后user_local_uniquekey为空");
		}
		return success();
	}

	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}

	public void setCacheUserCookieGenerator(CacheUserCookieGenerator cacheUserCookieGenerator) {
		this.cacheUserCookieGenerator = cacheUserCookieGenerator;
	}

}
