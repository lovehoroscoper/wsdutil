package org.gonetbar.ssa.dao;

import java.util.Map;

/**
 * 
 * @author Administrator
 * 
 */
public interface RegisterUserDao {

	public void addUser(Map param);
	
	public void queryCheckUserUnique(Map param);

}
