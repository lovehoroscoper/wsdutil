package org.gonetbar.ssa.dao.impl;

import java.util.Map;

import org.gonetbar.ssa.dao.RegisterUserDao;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-31 下午5:05:56
 */
public class RegisterUserDaoImpl extends BaseDaoImpl implements RegisterUserDao {

	@Override
	public void addUser(Map param) {
		this.findObject("org.gonetbar.ssa.entity.UserInfoVoMapper.addUser", param);
	}

	@Override
	public void queryCheckUserUnique(Map param) {
		this.findObject("org.gonetbar.ssa.entity.UserInfoVoMapper.queryCheckUserUnique", param);
	}

}
