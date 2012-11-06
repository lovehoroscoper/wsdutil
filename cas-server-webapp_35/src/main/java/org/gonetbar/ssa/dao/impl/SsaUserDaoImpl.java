package org.gonetbar.ssa.dao.impl;

import java.util.List;

import org.gonetbar.ssa.dao.SsaUserDao;
import org.gonetbar.ssa.entity.AclGrantedAuthority;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<AclGrantedAuthority> queryUserAuthorities(UserInfoVo findVo) {
		return this.queryList("org.gonetbar.ssa.entity.UserInfoVoMapper.queryUserAuthoritiesId", findVo);
	}

}
