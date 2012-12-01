package org.gonetbar.ssa.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.gonetbar.ssa.cache.SsoCacheManager;
import org.gonetbar.ssa.constant.CacheName;
import org.gonetbar.ssa.constant.CachePreKey;
import org.gonetbar.ssa.dao.SsaUserDao;
import org.gonetbar.ssa.entity.AclGrantedAuthority;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
import org.gonetbar.ssa.util.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

	public UserDetails loadUserByUsername(String username_unique) throws UsernameNotFoundException, DataAccessException {
		UserInfoVo user = findUserByName(username_unique);
		if (null == user) {
			String str = "用户[" + username_unique + "]状态异常";
			logger.error(str);
			throw new UsernameNotFoundException(str);
		}
		String cache_key = CachePreKey.CACHE_USERDETAILS_KEY_USERDETAIL + username_unique;
		UserDetails resVo = (UserDetails) SsoCacheManager.get(UserDetails.class, CacheName.CACHE_USERDETAILS, cache_key);
		if (null == resVo) {
			List<AclGrantedAuthority> dbAuths = queryUserAuthorities(user.getId_user(), username_unique);
			if (null == dbAuths || dbAuths.size() == 0) {
				String str = "用户[" + username_unique + "]无任何访问权限!!!";
				logger.error(str);
				throw new UsernameNotFoundException(str);
			}
			resVo = createUserDetails(username_unique, user, dbAuths);
			SsoCacheManager.set(CacheName.CACHE_USERDETAILS, cache_key, resVo);
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

	@Override
	public UserInfoVo findUserByVo(UserInfoVo findVo) {
		if (null == findVo) {
			return null;
		}
		String cache_key = CachePreKey.CACHE_USER_KEY_LOACL + findVo.getUsername();
		UserInfoVo resVo = SsoCacheManager.get(UserInfoVo.class, CacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssaUserDao.findUserByVo(findVo);
			SsoCacheManager.set(CacheName.CACHE_USER, cache_key, resVo);
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
	public UserProviderInfoVo findUserByProviderType(String providertype, String providerid) {
		UserProviderInfoVo findVo = new UserProviderInfoVo();
		findVo.setProvidertype(providertype);
		findVo.setProviderid(providerid);
		return findUserByProviderType(findVo);
	}

	@Override
	public UserProviderInfoVo findUserByProviderType(UserProviderInfoVo findVo) {
		if (null == findVo) {
			return null;
		}
		String providertype = findVo.getProvidertype();
		String providerid = findVo.getProviderid();
		if (CommUtils.isEmptyOrNullByTrim(providertype) || CommUtils.isEmptyOrNullByTrim(providerid)) {
			return null;
		}
		String cache_key = CachePreKey.CACHE_USER_KEY_THIRD + providertype + "#" + providerid;
		UserProviderInfoVo resVo = SsoCacheManager.get(UserProviderInfoVo.class, CacheName.CACHE_USER, cache_key);
		if (null == resVo) {
			resVo = ssaUserDao.findUserByProviderType(findVo);
			SsoCacheManager.set(CacheName.CACHE_USER, cache_key, resVo);
		}
		return resVo;
	}

	@SuppressWarnings("unchecked")
	private List<AclGrantedAuthority> queryUserAuthorities(long id_user, String username_unique) {
		String cache_key = CachePreKey.CACHE_USERDETAILS_KEY_AUTHORITY + username_unique;
		List<AclGrantedAuthority> list = (List<AclGrantedAuthority>) SsoCacheManager.get(CacheName.CACHE_USERDETAILS, cache_key);
		if (null == list) {
			UserInfoVo findVo = new UserInfoVo();
			findVo.setId_user(id_user);
			list = ssaUserDao.queryUserAuthorities(findVo);
			SsoCacheManager.set(CacheName.CACHE_USERDETAILS, cache_key, (Serializable) list);
		}
		return list;
	}

}
