package com.godtips.service;

import java.util.List;

import net.jforum.entities.Group;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:43:52
 * @version v1.0
 */
public interface GroupService {

	public Group selectById(int groupId) ;
	
	public List selectAll() ;
	
	public boolean canDelete(int groupId) ;
	
	public void delete(int groupId) ;
	
	public void update(Group group) ;
	
	public void addNew(Group group) ;

	public List selectUsersIds(int groupId) ;
}
