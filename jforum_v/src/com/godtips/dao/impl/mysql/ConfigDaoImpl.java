package com.godtips.dao.impl.mysql;

import java.util.List;

import net.jforum.dao.ConfigDAO;
import net.jforum.entities.Config;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:14:28
 * @version v1.0
 */
public class ConfigDaoImpl extends BaseDaoImpl implements ConfigDAO {

	@Override
	public void insert(Config config) {
		this.addObjectArray("ConfigModel.insert", config);
	}

	@Override
	public void update(Config config) {
		this.updateObject("ConfigModel.update", config);
	}

	@Override
	public void delete(Config config) {
		this.deleteObject("ConfigModel.delete", config.getId());
	}

	@Override
	public List selectAll() {
		return this.queryList("ConfigModel.selectAll", null);
	}

	@Override
	public Config selectByName(String name) {
		List list = this.queryList("ConfigModel.selectByName", name);
		Config c = null;
		if (null != list && list.size() > 0) {
			c = (Config)list.get(0);
		}
		return c;
	}

}
