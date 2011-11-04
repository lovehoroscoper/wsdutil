package com.wsd.dao.base;

import java.util.List;

public interface BaseDao  {
	public List getDataList(String ds,String sqlid,Object obj);
	public Object addObject(String ds,String sqlid,Object obj) throws Exception;
	public int updateObject(String ds,String sqlid,Object obj) throws Exception;
	public int deleteObject(String ds,String sqlid,Object obj) throws Exception;
	public Object addObject(String ds, String sqlid) throws Exception;
	public int updateObject(String ds, String sqlid) throws Exception ;
	public int deleteObject(String ds, String sqlid) throws Exception;
	public String getSql(String sqlId);
	public Object getObject(String ds,String sqlid,Object parameterObject) throws Exception;
}
