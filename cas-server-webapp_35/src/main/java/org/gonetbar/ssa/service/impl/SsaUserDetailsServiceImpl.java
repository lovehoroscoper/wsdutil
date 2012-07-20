package org.gonetbar.ssa.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.gonetbar.ssa.dao.SsaUserDao;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.SpringSecurityMessageSource;
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
		String str = "用户[" + username + "]不是管理员无权访问!!!";
		logger.error(str);
		throw new UsernameNotFoundException(str);
		// throw new
		// UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound",
		// new Object[] { username }, "Username {0} 不是管理员无权访问!!!"), new
		// RuntimeException("不是管理员无权访问!!!"));
		// List<UserDetails> users = loadUsersByUsername(username);
		// if (null == users || users.size() == 0) {
		// logger.debug("Query returned no results for user '" + username +
		// "'");
		// throw new
		// UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound",
		// new Object[] { username }, "Username {0} not found"), new
		// RuntimeException("Username not found"));
		// }
		// UserDetails user = users.get(0); // contains no GrantedAuthority[]

		// Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

		// if (enableAuthorities) {
		// dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));
		// }
		//
		// if (enableGroups) {
		// dbAuthsSet.addAll(loadGroupAuthorities(user.getUsername()));
		// }
		//
		// List<GrantedAuthority> dbAuths = new
		// ArrayList<GrantedAuthority>(dbAuthsSet);
		//
		// addCustomAuthorities(user.getUsername(), dbAuths);
		//
		// if (dbAuths.size() == 0) {
		// logger.debug("User '" + username +
		// "' has no authorities and will be treated as 'not found'");
		//
		// throw new UsernameNotFoundException(
		// messages.getMessage("JdbcDaoImpl.noAuthority",
		// new Object[] {username}, "User {0} has no GrantedAuthority"),
		// username);
		// }
		//
		// return createUserDetails(username, user, dbAuths);
		// return null;
	}

	protected List<UserDetails> loadUsersByUsername(String username) {
		return ssaUserDao.loadUsersByUsername(username);
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

}
