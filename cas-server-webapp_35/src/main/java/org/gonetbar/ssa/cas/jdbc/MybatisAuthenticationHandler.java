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
			final UserInfoVo resultVo = this.getSsaUserService().findUserByName(username);
			if (null != resultVo && null != resultVo.getPassword()) {
				// TODO 当初为什么这么写？忘记了
				//final String dbPassword = this.getSsaUserService().findUserByName(username).getPassword();
				final String dbPassword = resultVo.getPassword();
				if(null != dbPassword && !"".equals(dbPassword.trim())){
					return dbPassword.equals(encryptedPassword);
				}else{
					return false;
				}
			} else {
				return false;
			}
		} catch (final IncorrectResultSizeDataAccessException e) {
			// this means the username was not found.
			return false;
		}
	}

}