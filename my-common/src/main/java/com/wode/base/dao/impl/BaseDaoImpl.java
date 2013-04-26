package com.wode.base.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.wode.base.dao.BaseDao;
import com.wode.base.entity.PageBean;
import com.wode.base.entity.ParamPageVo;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-1-5 下午09:13:30
 * @version v1.0
 */
public class BaseDaoImpl extends SqlSessionDaoSupport implements BaseDao {

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

	@SuppressWarnings("rawtypes")
	public PageBean queryListPage(ParamPageVo paramPageVo) {
		int pageNo = paramPageVo.getPageNo();
		int start = (pageNo - 1 ) * paramPageVo.getPageSize();
		paramPageVo.setPageNo(start);
		int count = queryTotalRows(paramPageVo.getCountSqlId(), paramPageVo);
		List resultList = queryList(paramPageVo.getSqlId(), paramPageVo);
		PageBean pageBean = new PageBean(pageNo, paramPageVo.getPageSize(), count, resultList);
		return pageBean;
	}
}
