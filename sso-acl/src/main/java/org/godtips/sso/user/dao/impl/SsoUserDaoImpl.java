package org.godtips.sso.user.dao.impl;

import org.godtips.sso.user.dao.SsoUserDao;
import org.godtips.sso.user.entity.ThirdProvider;
import org.godtips.sso.user.entity.UserInfoVo;
import org.godtips.sso.user.entity.UserProviderInfoVo;

import com.wode.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-12-8 下午9:36:02
 * @version v1.0
 */
public class SsoUserDaoImpl extends BaseDaoImpl implements SsoUserDao {

	@Override
	public UserInfoVo findUserByVo(UserInfoVo findVo) {
		return (UserInfoVo) this.findObject("com.wode.sso.user.entity.UserInfoVoMapper.findUserByVoId", findVo);
	}

	@Override
	public UserProviderInfoVo findUserByProviderId(UserProviderInfoVo findVo) {
		return (UserProviderInfoVo) this.findObject("com.wode.sso.user.entity.UserInfoVoMapper.findUserByProviderId", findVo);
	}

	@Override
	public ThirdProvider findProviderIdByType(ThirdProvider findVo) {
		return (ThirdProvider) this.findObject("com.wode.sso.user.entity.UserInfoVoMapper.findProviderIdByTypeId", findVo);
	}

}
