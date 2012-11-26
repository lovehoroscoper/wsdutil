package org.gonetbar.ssa.cas.filter.adm;

import java.util.Collection;
import java.util.List;

import org.gonetbar.ssa.cas.exception.AccessFailException;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.core.Authentication;

/**
 * @desc 描述：访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-11 下午4:23:11
 */
public class AclAccessDecisionManager extends AbstractAccessDecisionManager {

	@Deprecated
	public AclAccessDecisionManager() {
	}

	@SuppressWarnings("rawtypes")
	public AclAccessDecisionManager(List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
		int deny = 0;

		for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, object, configAttributes);

			if (logger.isDebugEnabled()) {
				logger.debug("Voter: " + voter + ", returned: " + result);
			}

			switch (result) {
			case AccessDecisionVoter.ACCESS_GRANTED:
				return;

			case AccessDecisionVoter.ACCESS_DENIED:
				deny++;

				break;

			default:
				break;
			}
		}

		if (deny > 0) {
//			throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
			throw new AccessFailException("您无权访问!!!");
		}
		checkAllowIfAllAbstainDecisions();
	}

}
