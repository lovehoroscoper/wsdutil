package com.godtips.dao.impl.mysql;

import java.util.List;

import net.jforum.dao.TreeGroupDAO;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-8 下午9:38:56
 * @version v1.0
 */
public class TreeGroupDaoImpl extends BaseDaoImpl implements TreeGroupDAO {

	@Override
	public List selectGroups(int parentId) {
		List list = this.queryList("TreeGroupModel.selectGroup", parentId);
		return list;
	}

}
