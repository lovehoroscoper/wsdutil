package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.GroupDAO;
import net.jforum.entities.Group;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:44:11
 * @version v1.0
 */
public class GroupServiceImpl implements com.godtips.service.GroupService {
	
	private GroupDAO groupDao;
	
	public GroupDAO getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(GroupDAO groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public Group selectById(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDelete(int groupId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(int groupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNew(Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List selectUsersIds(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}


}
