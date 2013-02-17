package org.gonetbar.ssa.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.filter.GenericFilterBean;

public class LogoutCacheFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(LogoutCacheFilter.class);

	private List<String> processesUrls;;
	private List<LogoutHandler> handlers;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			if (requiresLogout(request, response)) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				for (LogoutHandler handler : handlers) {
					handler.logout(request, response, auth);
				}
			}
		} catch (Exception e) {
			logger.error("执行LogoutCacheFilter清楚信息handler异常", e);
		}
		chain.doFilter(request, response);
	}

	protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything from the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}
		int queryParamIndex = uri.indexOf('?');
		if (queryParamIndex > 0) {
			// strip everything from the first question mark
			uri = uri.substring(0, queryParamIndex);
		}
		if (null != processesUrls && processesUrls.size() > 0) {
			for (String filterProcessesUrl : processesUrls) {
				if (uri.endsWith(request.getContextPath() + filterProcessesUrl)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setProcessesUrls(List<String> processesUrls) {
		this.processesUrls = processesUrls;
	}

	public void setHandlers(List<LogoutHandler> handlers) {
		this.handlers = handlers;
	}

}
