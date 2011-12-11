package org.gonetbar.ssa.cas.jdbc;

import org.gonetbar.ssa.entity.UserInfoVo;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

public final class MybatisAuthenticationHandler extends AbstractSsaUsernamePasswordAuthenticationHandler {

	protected final boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials) throws AuthenticationException {
		final String username = getPrincipalNameTransformer().transform(credentials.getUsername());
		final String password = credentials.getPassword();
		final String encryptedPassword = this.getPasswordEncoder().encode(password);
		try {
			final UserInfoVo resultVo = this.getUserSsaService().findUserByName(username);
			if (null != resultVo && null != resultVo.getPassword()) {
				final String dbPassword = this.getUserSsaService().findUserByName(username).getPassword();
				return dbPassword.equals(encryptedPassword);
			} else {
				return false;
			}
		} catch (final IncorrectResultSizeDataAccessException e) {
			// this means the username was not found.
			return false;
		}
	}

}