package org.gonetbar.ssa.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.gonetbar.ssa.dao.SsaUserDao;
import org.gonetbar.ssa.entity.AclGrantedAuthority;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
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

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserInfoVo user = findUserByName(username);
		if (null == user) {
			String str = "用户[" + username + "]异常!!!";
			logger.error(str);
			throw new UsernameNotFoundException(str);
		}
		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		dbAuthsSet.addAll(queryUserAuthorities(user.getId_user()));
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		if (dbAuths.size() == 0) {
			String str = "用户[" + username + "] has no authorities and will be treated as 'not found'";
			logger.error(str);
			throw new UsernameNotFoundException(str);
		}
		return createUserDetails(username, user, dbAuths);
	}

	protected UserDetails createUserDetails(String username, UserInfoVo userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
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
		return ssaUserDao.findUserByVo(findVo);
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

	// 缓存
	@Override
	public UserProviderInfoVo findUserByProviderType(UserProviderInfoVo findVo) {
		if (null == findVo) {
			return null;
		}
		return ssaUserDao.findUserByProviderType(findVo);
	}

	@Override
	public List<AclGrantedAuthority> queryUserAuthorities(long id_user) {
		UserInfoVo findVo = new UserInfoVo();
		findVo.setId_user(id_user);
		return ssaUserDao.queryUserAuthorities(findVo);
	}

}
