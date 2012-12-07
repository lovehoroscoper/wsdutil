package org.gonetbar.ssa.dao;

import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:35:37
 * @version v1.0
 */
public interface SsaUserDao {

	public UserInfoVo findUserByVo(UserInfoVo findVo);

	public UserProviderInfoVo findUserByProviderType(UserProviderInfoVo findVo);

}
