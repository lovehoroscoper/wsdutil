package com.godtips.sso.acl.dao;

import java.util.List;

public interface AclBaseDao {

	public Object addObject(String sqlid, Object obj);

	public int updateObject(String sqlid, Object obj);

	public int deleteObject(String sqlid, Object obj);

	public Object findObject(String sqlid, Object obj);

	@SuppressWarnings("rawtypes")
	public List queryList(String sqlid, Object obj);

	public int queryTotalRows(String countSqlid, Object obj);


}
