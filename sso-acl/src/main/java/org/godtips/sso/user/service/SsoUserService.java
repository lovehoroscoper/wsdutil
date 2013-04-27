package org.godtips.sso.user.service;

import org.godtips.sso.user.entity.ThirdProvider;
import org.godtips.sso.user.entity.UserInfoVo;
import org.godtips.sso.user.entity.UserProviderInfoVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:33:55
 * @version v1.0
 */
public interface SsoUserService extends UserDetailsService {

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
	public UserProviderInfoVo findUserByProviderId(String providerId, String thirdUserId);

	public UserProviderInfoVo findUserByProviderId(UserProviderInfoVo findVo);

	/**
	 * 通过profileType
	 * @param findVo
	 * @return
	 */
	public ThirdProvider findProviderIdByType(ThirdProvider findVo);

	/**
	 * 通过profileType
	 * @param providerType
	 * @return
	 */
	public ThirdProvider findProviderIdByType(String profileType);

}
