package org.gonetbar.ssa.service;

import java.util.Map;

/**
 * 
 * @author Administrator
 * 
 */
public interface RegisterUserService {

	/**
	 * 添加用户主信息
	 * @param param
	 */
	public void addUser(Map param);
	
	/**
	 * 用于检测用户唯一性
	 * @param param
	 */
	public void queryCheckUserUnique(Map param);

}
