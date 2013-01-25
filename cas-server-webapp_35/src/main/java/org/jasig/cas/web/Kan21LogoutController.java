package org.jasig.cas.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * LogoutController
 * 
 * @author Administrator
 * 
 */
public final class Kan21LogoutController extends AbstractController {

	/** The CORE to which we delegate for all CAS functionality. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	/** CookieGenerator for TGT Cookie */
	@NotNull
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	/** CookieGenerator for Warn Cookie */
	@NotNull
	private CookieRetrievingCookieGenerator warnCookieGenerator;

	/** Logout view name. */
	@NotNull
	private String logoutView;

	@NotNull
	private ServicesManager servicesManager;

	/**
	 * Boolean to determine if we will redirect to any url provided in the
	 * service request parameter.
	 */
	private boolean followServiceRedirects;

	// new
	private LogoutHandler handler;
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	public Kan21LogoutController() {
		setCacheSeconds(0);
	}

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
		final String service = request.getParameter("service");

		//add by weisd
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			handler.logout(request, response, auth);
		} catch (Exception e) {
			logger.error("Kan21LogoutController退出清除缓存异常",e);
		}

		if (ticketGrantingTicketId != null) {
			this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);

			this.ticketGrantingTicketCookieGenerator.removeCookie(response);
			this.warnCookieGenerator.removeCookie(response);
		}

		if (this.followServiceRedirects && service != null) {
			final RegisteredService rService = this.servicesManager.findServiceBy(new SimpleWebApplicationServiceImpl(service));

			if (rService != null && rService.isEnabled()) {
				return new ModelAndView(new RedirectView(service));
			}
		}

		return new ModelAndView(this.logoutView);
	}

	public void setTicketGrantingTicketCookieGenerator(final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public void setWarnCookieGenerator(final CookieRetrievingCookieGenerator warnCookieGenerator) {
		this.warnCookieGenerator = warnCookieGenerator;
	}

	/**
	 * @param centralAuthenticationService
	 *            The centralAuthenticationService to set.
	 */
	public void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public void setFollowServiceRedirects(final boolean followServiceRedirects) {
		this.followServiceRedirects = followServiceRedirects;
	}

	public void setLogoutView(final String logoutView) {
		this.logoutView = logoutView;
	}

	public void setServicesManager(final ServicesManager servicesManager) {
		this.servicesManager = servicesManager;
	}

	public final void setHandler(LogoutHandler handler) {
		this.handler = handler;
	}
	
}
