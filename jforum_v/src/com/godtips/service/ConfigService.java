package com.godtips.service;

import java.util.List;

import net.jforum.entities.Config;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:14:41
 * @version v1.0
 */
public interface ConfigService {
	
	public void insert(Config config);

	public void update(Config config);

	public void delete(Config config);

	public List selectAll();

	public Config selectByName(String name);
	
}
