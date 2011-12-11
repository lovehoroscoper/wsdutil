package org.gonetbar.ssa.service;

import org.gonetbar.ssa.entity.UserInfoVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:33:55
 * @version v1.0
 */
public interface UserSsaService extends UserDetailsService {

	public UserInfoVo findUserByVo(UserInfoVo findVo);

	/**
	 * 根据登录用户名
	 * 
	 * @param username
	 * @return
	 */
	public UserInfoVo findUserByName(String username);
	
}
