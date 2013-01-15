package org.gonetbar.ssa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.gonetbar.ssa.cache.SsoCacheManager;
import org.gonetbar.ssa.constant.SsoCacheName;
import org.gonetbar.ssa.constant.SsoCachePreKey;
import org.gonetbar.ssa.dao.SsaUserDao;
import org.gonetbar.ssa.entity.ThirdProvider;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
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
public class SsaUserDetailsServiceImpl implements SsaUserService {

	private static Logger logger = LoggerFactory.getLogger(SsaUserDetailsServiceImpl.class);

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private SsaUserDao ssaUserDao;

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
		UserInfoVo resVo = SsoCacheManager.get(UserInfoVo.class, SsoCacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssaUserDao.findUserByVo(findVo);
			SsoCacheManager.set(SsoCacheName.CACHE_USER, cache_key, resVo);
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
		UserProviderInfoVo resVo = SsoCacheManager.get(UserProviderInfoVo.class, SsoCacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssaUserDao.findUserByProviderId(findVo);
			SsoCacheManager.set(SsoCacheName.CACHE_USER, cache_key, resVo);
		}
		return resVo;
	}

	protected UserDetails createUserDetails(String username, UserInfoVo userFromUserQuery, List<AclGrantedAuthority> combinedAuthorities) {
		return new User(username, userFromUserQuery.getPassword(), userFromUserQuery.getValidtype() == 0, true, true, true, combinedAuthorities);
	}

	@Resource(name = "ssaUserDao")
	public void setSsaUserDao(SsaUserDao ssaUserDao) {
		this.ssaUserDao = ssaUserDao;
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
		ThirdProvider resVo = SsoCacheManager.get(ThirdProvider.class, SsoCacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssaUserDao.findProviderIdByType(findVo);
			SsoCacheManager.set(SsoCacheName.CACHE_USER, cache_key, resVo);
		}
		return resVo;
	}

}
