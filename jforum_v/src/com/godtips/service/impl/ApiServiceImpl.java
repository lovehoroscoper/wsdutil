package com.godtips.service.impl;

import net.jforum.dao.ApiDAO;

import com.godtips.service.ApiService;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-13 下午3:40:43
 */
public class ApiServiceImpl implements ApiService {

	private ApiDAO apiDao;
	
	@Override
	public boolean findValid(String apiKey) {
		return apiDao.isValid(apiKey);
	}

	public void setApiDao(ApiDAO apiDao) {
		this.apiDao = apiDao;
	}

}
