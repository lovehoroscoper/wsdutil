package org.gonetbar.ssa.cas.jdbc;

import javax.validation.constraints.NotNull;

import org.gonetbar.ssa.service.UserSsaService;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-10 下午4:26:15
 */
public abstract class AbstractSsaUsernamePasswordAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

	@NotNull
	private UserSsaService userSsaService;

	protected UserSsaService getUserSsaService() {
		return this.userSsaService;
	}

	public final void setUserSsaService(UserSsaService userSsaService) {
		this.userSsaService = userSsaService;
	}

}
