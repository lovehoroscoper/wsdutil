package com.godtips.base.dao.impl;

import java.sql.SQLException;
import java.util.List;

import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.godtips.base.dao.BaseDao;
import com.godtips.base.entity.PageBean;
import com.godtips.base.entity.ParamPageVo;

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
	
	public Object addObjectArray(String sqlid, Object... obj) {
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
		int count = queryTotalRows(paramPageVo.getCountSqlId(), paramPageVo.getCondition());
		List resultList = queryList(paramPageVo.getSqlId(), paramPageVo.getCondition());
		PageBean pageBean = new PageBean(paramPageVo.getPageNo(), paramPageVo.getPageSize(), count, resultList);
		return pageBean;
	}
	
	/**
	 * TODO 后期要删除
	 * @return
	 */
	protected boolean supportAutoGeneratedKeys() {
		return SystemGlobals.getBoolValue(ConfigKeys.DATABASE_AUTO_KEYS);
	}
	
	protected int executeAutoKeysQuery(String sqlid) throws SQLException {
		int id = -1;
		if (this.supportAutoGeneratedKeys()) {//数据库支持自动生成主键
			id = -1;
			//不使用该方法，需要修改原有sql
		} else {
			//TODO 需要自己写序列
			id = -1;
		}
		if (id == -1) {
			throw new SQLException(this.getErrorMessage());
		}
		return id;
	}
	
	protected String getErrorMessage() {
//		return "Could not obtain the latest generated key. This error may be associated" 
//				+ " to some invalid database driver operation or server failure."
//				+ " Please check the database configurations and code logic.";
		return "只使用mysql数据库且使用mybatis自动生成主键,请修改数据库语句,请暂时不使用该方法!!!";
	}

	@Override
	public int deleteBatchObj(Object... obj) {
		
//		getSqlSession().
		//TODO 对于批量删除 是否有什么好的批量处理？？ 不是 使用  in
		
		return 0;
	}

}
