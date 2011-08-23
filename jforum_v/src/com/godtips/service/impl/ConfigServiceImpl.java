package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.ConfigDAO;
import net.jforum.entities.Config;

import com.godtips.service.ConfigService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:14:59
 * @version v1.0
 */
public class ConfigServiceImpl implements ConfigService {
	
	private ConfigDAO configDao;

	public ConfigDAO getConfigDao() {
		return configDao;
	}

	public void setConfigDao(ConfigDAO configDao) {
		this.configDao = configDao;
	}

	@Override
	public void insert(Config config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Config config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Config config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Config selectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
