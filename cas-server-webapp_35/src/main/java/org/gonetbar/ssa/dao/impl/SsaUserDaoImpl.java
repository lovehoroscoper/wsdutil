package org.gonetbar.ssa.dao.impl;

import org.gonetbar.ssa.dao.SsaUserDao;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:36:02
 * @version v1.0
 */
public class SsaUserDaoImpl extends BaseDaoImpl implements SsaUserDao {

	@Override
	public UserInfoVo findUserByVo(UserInfoVo findVo) {
		return (UserInfoVo) this.findObject("org.gonetbar.ssa.entity.UserInfoVoMapper.findUserByVoId", findVo);
	}

	@Override
	public UserProviderInfoVo findUserByProviderType(UserProviderInfoVo findVo) {
		return (UserProviderInfoVo) this.findObject("org.gonetbar.ssa.entity.UserInfoVoMapper.findUserByProviderTypeId", findVo);
	}

}
