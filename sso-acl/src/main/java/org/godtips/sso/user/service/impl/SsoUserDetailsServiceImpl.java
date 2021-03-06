package org.godtips.sso.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.godtips.sso.user.cache.UserCacheManager;
import org.godtips.sso.user.constant.SsoCacheName;
import org.godtips.sso.user.constant.SsoCachePreKey;
import org.godtips.sso.user.dao.SsoUserDao;
import org.godtips.sso.user.entity.ThirdProvider;
import org.godtips.sso.user.entity.UserInfoVo;
import org.godtips.sso.user.entity.UserProviderInfoVo;
import org.godtips.sso.user.service.SsoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.godtips.common.UtilString;
import com.godtips.sso.acl.entity.AclGrantedAuthority;
import com.godtips.sso.acl.entity.AclUserVo;
import com.godtips.sso.acl.service.SecurityMetadataSourceService;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:34:14
 * @version v1.0
 */
public class SsoUserDetailsServiceImpl implements SsoUserService {

	private static Logger logger = LoggerFactory.getLogger(SsoUserDetailsServiceImpl.class);

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private SsoUserDao ssoUserDao;

	private SecurityMetadataSourceService securityMetadataSourceService;

	public UserDetails loadUserByUsername(String username_unique) throws UsernameNotFoundException, DataAccessException {
		UserInfoVo user = findUserByName(username_unique);
		if (null == user) {
			String str = "用户[" + username_unique + "]状态异常";
			logger.error(str);
			throw new UsernameNotFoundException(str);
		}
		AclUserVo aclUser = new AclUserVo(user.getId_user(), user.getUsername(), user.getPassword(), user.getValidtype());
		return securityMetadataSourceService.queryUserDetailsByAclUser(username_unique, aclUser);
	}

	@Override
	public UserInfoVo findUserByVo(UserInfoVo findVo) {
		if (null == findVo) {
			return null;
		}
		String cache_key = SsoCachePreKey.CACHE_USER_KEY_LOACL + findVo.getUsername();
		UserInfoVo resVo = UserCacheManager.get(UserInfoVo.class, SsoCacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssoUserDao.findUserByVo(findVo);
			UserCacheManager.set(SsoCacheName.CACHE_USER, cache_key, resVo);
		}
		return resVo;
	}

	@Override
	public UserInfoVo findUserByName(String username) {
		UserInfoVo findVo = new UserInfoVo();
		findVo.setUsername(username);
		return findUserByVo(findVo);
	}

	@Override
	public UserProviderInfoVo findUserByProviderId(String providerId, String thirdUserId) {
		UserProviderInfoVo findVo = new UserProviderInfoVo();
		findVo.setProviderid(providerId);
		findVo.setThirduserid(thirdUserId);
		return findUserByProviderId(findVo);
	}

	@Override
	public UserProviderInfoVo findUserByProviderId(UserProviderInfoVo findVo) {
		if (null == findVo) {
			return null;
		}
		String providerId = findVo.getProviderid();
		String thirdUserId = findVo.getThirduserid();
		if (UtilString.isEmptyOrNullByTrim(providerId) || UtilString.isEmptyOrNullByTrim(thirdUserId)) {
			return null;
		}
		String cache_key = SsoCachePreKey.CACHE_USER_KEY_THIRD + providerId + "#" + thirdUserId;
		UserProviderInfoVo resVo = UserCacheManager.get(UserProviderInfoVo.class, SsoCacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssoUserDao.findUserByProviderId(findVo);
			UserCacheManager.set(SsoCacheName.CACHE_USER, cache_key, resVo);
		}
		return resVo;
	}

	protected UserDetails createUserDetails(String username, UserInfoVo userFromUserQuery, List<AclGrantedAuthority> combinedAuthorities) {
		return new User(username, userFromUserQuery.getPassword(), userFromUserQuery.getValidtype() == 0, true, true, true, combinedAuthorities);
	}

	public void setSsoUserDao(SsoUserDao ssoUserDao) {
		this.ssoUserDao = ssoUserDao;
	}

	@Resource(name = "securityMetadataSourceService")
	public void setSecurityMetadataSourceService(SecurityMetadataSourceService securityMetadataSourceService) {
		this.securityMetadataSourceService = securityMetadataSourceService;
	}

	@Override
	public ThirdProvider findProviderIdByType(String profileType) {
		ThirdProvider findVo = new ThirdProvider();
		findVo.setProfileType(profileType);
		return findProviderIdByType(findVo);
	}

	@Override
	public ThirdProvider findProviderIdByType(ThirdProvider findVo) {
		String cache_key = SsoCachePreKey.CACHE_PROVIDER_KEY_OAUTH + findVo.getProfileType();
		ThirdProvider resVo = UserCacheManager.get(ThirdProvider.class, SsoCacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssoUserDao.findProviderIdByType(findVo);
			UserCacheManager.set(SsoCacheName.CACHE_USER, cache_key, resVo);
		}
		return resVo;
	}

}
