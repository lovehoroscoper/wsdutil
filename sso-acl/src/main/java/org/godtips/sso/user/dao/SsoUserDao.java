package org.godtips.sso.user.dao;

import org.godtips.sso.user.entity.ThirdProvider;
import org.godtips.sso.user.entity.UserInfoVo;
import org.godtips.sso.user.entity.UserProviderInfoVo;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:35:37
 * @version v1.0
 */
public interface SsoUserDao {

	public UserInfoVo findUserByVo(UserInfoVo findVo);

	public UserProviderInfoVo findUserByProviderId(UserProviderInfoVo findVo);

	public ThirdProvider findProviderIdByType(ThirdProvider findVo);

}
