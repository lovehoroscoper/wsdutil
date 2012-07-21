package org.gonetbar.ssa.cas.jdbc;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.gonetbar.ssa.service.SsaUserService;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-10 下午4:26:15
 */
public abstract class AbstractSsaUsernamePasswordAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	
	@NotNull
	private PasswordEncoder sscPasswordEncoder = new StandardPasswordEncoder("");

	private SsaUserService ssaUserService;

	public SsaUserService getSsaUserService() {
		return ssaUserService;
	}

	@Resource(name = "userDetailsService")
	public void setSsaUserService(SsaUserService ssaUserService) {
		this.ssaUserService = ssaUserService;
	}

	public final PasswordEncoder getSscPasswordEncoder() {
		return sscPasswordEncoder;
	}

	public final void setSscPasswordEncoder(final PasswordEncoder sscPasswordEncoder) {
		this.sscPasswordEncoder = sscPasswordEncoder;
	}
	
}
