package com.wsd.dao;

import java.util.Map;

import com.wsd.vo.User;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-2-21 обнГ10:19:27
 * @version v1.0
 */
public interface TestDao {
	
	public void addUser(User u);
	public void addUser_1(User u);
	public void addUser2(User u) throws Exception;
	
	public void addHfcore(Map map);

}
