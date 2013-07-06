package com.godtips.sso.acl.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.godtips.sso.acl.dao.AclBaseDao;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-1-5 下午09:13:30
 * @version v1.0
 */
public class AclBaseDaoImpl extends SqlSessionDaoSupport implements AclBaseDao {
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	public Object addObject(String sqlid, Object obj) {
		return this.getSqlSession().insert(sqlid, obj);
	}

	public Object findObject(String sqlid, Object obj) {
		return this.getSqlSession().selectOne(sqlid, obj);
	}

	@SuppressWarnings("rawtypes")
	public List queryList(String sqlid, Object obj) {
		return this.getSqlSession().selectList(sqlid, obj);
	}

	public int deleteObject(String sqlid, Object obj) {
		return this.getSqlSession().delete(sqlid, obj);
	}

	public int updateObject(String sqlid, Object obj) {
		return this.getSqlSession().update(sqlid, obj);
	}

	public int queryTotalRows(String countSqlid, Object obj) {
		return (Integer) this.getSqlSession().selectOne(countSqlid, obj);
	}

}
