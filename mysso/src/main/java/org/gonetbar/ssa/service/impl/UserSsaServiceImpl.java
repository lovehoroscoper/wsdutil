package org.gonetbar.ssa.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.gonetbar.ssa.dao.UserSsaDao;
import org.gonetbar.ssa.service.UserSsaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
public class UserSsaServiceImpl implements UserSsaService {

	private static Logger logger = LoggerFactory.getLogger(UserSsaServiceImpl.class);

	private UserSsaDao userSsaDao;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		List<UserDetails> users = loadUsersByUsername(username);

		if (users.size() == 0) {
			logger.debug("Query returned no results for user '" + username + "'");

			throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[] { username }, "Username {0} not found"), username);
		}

		UserDetails user = users.get(0); // contains no GrantedAuthority[]

		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

		if (enableAuthorities) {
			dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));
		}

		if (enableGroups) {
			dbAuthsSet.addAll(loadGroupAuthorities(user.getUsername()));
		}

		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);

		addCustomAuthorities(user.getUsername(), dbAuths);

		if (dbAuths.size() == 0) {
			logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");

			throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.noAuthority", new Object[] { username }, "User {0} has no GrantedAuthority"), username);
		}

		// 这步必须返回一个UserDetails
		return createUserDetails(username, user, dbAuths);
	}

	protected List<UserDetails> loadUsersByUsername(String username) {
		
		return userSsaDao.loadUsersByUsername(username);
		
//		return getJdbcTemplate().query(usersByUsernameQuery, new String[] { username }, new RowMapper<UserDetails>() {
//			public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
//				String username = rs.getString(1);
//				String password = rs.getString(2);
//				boolean enabled = rs.getBoolean(3);
//				return new User(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
//			}
//
//		});
	}

	@Resource(name = "userSsaDao")
	public void setUserSsaDao(UserSsaDao userSsaDao) {
		this.userSsaDao = userSsaDao;
	}

}
