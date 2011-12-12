package org.gonetbar.ssa.cas.filter.adm;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

/**
 * @desc 描述：访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-11 下午4:23:11
 */
public class SsaAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		// TODO Auto-generated method stub
		
		System.out.println("123231");
		
		System.out.println("456789");

	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

}
