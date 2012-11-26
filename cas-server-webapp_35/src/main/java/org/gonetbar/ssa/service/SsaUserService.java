package org.gonetbar.ssa.service;

import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:33:55
 * @version v1.0
 */
public interface SsaUserService extends UserDetailsService {

	public UserInfoVo findUserByVo(UserInfoVo findVo);

	/**
	 * 根据登录用户名
	 * 
	 * @param username
	 * @return
	 */
	public UserInfoVo findUserByName(String username);
	
	/**
	 * 根据登录类型查找用户信息
	 * 
	 * @param providerType
	 * @return
	 */
	public UserProviderInfoVo findUserByProviderType(String providertype, String providerid);
	
	public UserProviderInfoVo findUserByProviderType(UserProviderInfoVo findVo);
	
	//public List<AclGrantedAuthority> queryUserAuthorities(long id_user);
	
}
