package com.wsd.dao.base.impl;

import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.wsd.dao.base.CommonBean;

public class CommonBeanImpl implements CommonBean {
	
	private Map<String,SqlMapClient> dataSourceList;
	public CommonBeanImpl() {
		// TODO Auto-generated constructor stub
	}

	public Map<String,SqlMapClient> getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(Map<String,SqlMapClient> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	 

	public Map<String,SqlMapClient> getDataSourceInfo() {
		// TODO Auto-generated method stub
		return this.dataSourceList;
	}

}
