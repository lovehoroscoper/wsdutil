package org.gonetbar.ssa.service.impl;

import java.util.Map;

import org.gonetbar.ssa.dao.RegisterUserDao;
import org.gonetbar.ssa.service.RegisterUserService;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-31 下午5:06:29
 */
public class RegisterUserServiceImpl implements RegisterUserService {

	private RegisterUserDao registerUserDao;

	@Override
	public void addUser(Map param) {
		registerUserDao.addUser(param);
	}

	public final void setRegisterUserDao(RegisterUserDao registerUserDao) {
		this.registerUserDao = registerUserDao;
	}

}
