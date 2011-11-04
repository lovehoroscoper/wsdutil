package com.wsd.dao.base;

import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

public interface CommonBean {
	public Map<String,SqlMapClient> getDataSourceInfo ();
}
