package org.gonetbar.ssa.cas.jdbc;

import org.gonetbar.ssa.entity.UserInfoVo;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

public final class MybatisAuthenticationHandler extends AbstractSsaUsernamePasswordAuthenticationHandler {

	protected final boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials) throws AuthenticationException {
		final String username = getPrincipalNameTransformer().transform(credentials.getUsername());
		final String rawPassword = credentials.getPassword();
		try {
			final UserInfoVo resultVo = this.getSsaUserService().findUserByName(username);
			if (null != resultVo) {
				final String encodedPassword = resultVo.getPassword();
				if(null != encodedPassword && !"".equals(encodedPassword.trim())){
					return this.getSscPasswordEncoder().matches(rawPassword, encodedPassword);
				}else{
					return false;
				}
			} else {
				return false;
			}
		} catch (final IncorrectResultSizeDataAccessException e) {
			return false;
		}
	}

}