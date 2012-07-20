package org.gonetbar.ssa.cas.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * @desc 描述：我自己的权限认证中心
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-13 下午5:29:20
 */
public class SsaFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
	// ~ Static fields/initializers
	// =====================================================================================
	// ~ Instance fields
	// ================================================================================================
	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	// ~ Methods
	// ========================================================================================================
	/**
	 * Method that is actually called by the filter chain. Simply delegates to
	 * the {@link #invoke(FilterInvocation)} method.
	 * 
	 * @param request
	 *            the servlet request
	 * @param response
	 *            the servlet response
	 * @param chain
	 *            the filter chain
	 * 
	 * @throws IOException
	 *             if the filter chain fails
	 * @throws ServletException
	 *             if the filter chain fails
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}

	}

	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	/**
	 * @deprecated use setSecurityMetadataSource instead
	 */
	public void setObjectDefinitionSource(FilterInvocationSecurityMetadataSource newSource) {
		logger.warn("The property 'objectDefinitionSource' is deprecated. Please use 'securityMetadataSource' instead");
		this.securityMetadataSource = newSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
		this.securityMetadataSource = newSource;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}