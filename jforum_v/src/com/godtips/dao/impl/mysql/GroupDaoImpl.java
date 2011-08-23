package com.godtips.dao.impl.mysql;

import java.util.List;

import net.jforum.dao.GroupDAO;
import net.jforum.dao.GroupSecurityDAO;
import net.jforum.entities.Group;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:43:39
 * @version v1.0
 */
public class GroupDaoImpl extends BaseDaoImpl implements GroupDAO {
	
	private GroupSecurityDAO groupSecurityDao;
	
	public GroupSecurityDAO getGroupSecurityDao() {
		return groupSecurityDao;
	}

	public void setGroupSecurityDao(GroupSecurityDAO groupSecurityDao) {
		this.groupSecurityDao = groupSecurityDao;
	}

	@Override
	public Group selectById(int groupId) {
		List list = this.queryList("GroupModel.selectById", groupId);
		Group g = new Group();
		if(null != list && list.size() > 0){
			g = (Group)list.get(0);
		}
		return g;
	}

	@Override
	public List selectAll() {
		return this.queryList("GroupModel.selectAll", null);
	}

	@Override
	public boolean canDelete(int groupId) {
		int count = (Integer)this.findObject("GroupModel.canDelete", groupId);
		return count < 1;
	}

	@Override
	public void delete(int groupId) {
		this.deleteObject("GroupModel.delete", groupId);
		groupSecurityDao.deleteAllRoles(groupId);
	}

	@Override
	public void update(Group group) {
		this.updateObject("GroupModel.update", group);
	}

	@Override
	public void addNew(Group group) {
		this.addObjectArray("GroupModel.addNew", group);
	}

	@Override
	public List selectUsersIds(int groupId) {
		return this.queryList("GroupModel.selectUsersIds", groupId);
	}

}
