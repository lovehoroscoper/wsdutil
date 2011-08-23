package com.godtips.dao.impl.mysql;

import net.jforum.dao.GroupSecurityDAO;
import net.jforum.entities.User;
import net.jforum.security.Role;
import net.jforum.security.RoleCollection;
import net.jforum.security.RoleValueCollection;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午10:06:02
 * @version v1.0
 */
public class GroupSecurityDaoImpl extends BaseDaoImpl implements GroupSecurityDAO {

	@Override
	public void deleteForumRoles(int forumId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllRoles(int groupId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRole(int groupId, Role role) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRoleValue(int id, Role role, RoleValueCollection rvc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRole(int id, Role role, RoleValueCollection roleValues) {
		// TODO Auto-generated method stub

	}

	@Override
	public RoleCollection loadRoles(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoleCollection loadRolesByUserGroups(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
