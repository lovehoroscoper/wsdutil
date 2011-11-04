package com.wsd.dao.base.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.wsd.dao.base.BaseDao;
import com.wsd.dao.base.CommonBean;

public class BaseDaoImpl implements BaseDao {

	private final static ThreadLocal<SqlMapClient> sqlMapClientLocal = new ThreadLocal<SqlMapClient>();

	private Logger log = Logger.getLogger("");

	private CommonBean commonBean;
	private CommonBean commonBeanAdmin;


	protected int waringTime = 25000;

	private boolean timeOutWaring = true;
	private boolean sqlExceptionWaring = false;
	public BaseDaoImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public Object addObject(String ds, String sqlid) {
		// TODO Auto-generated method stub
		if (ds == null)
			log.error("DataSource Catn't Be Null When Call addObject Method! ");
		this.initDataSource(ds);
		Date before = new Date();
		Object o = getSqlMapClientTemplate().insert(sqlid);
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return o;
	}
	public Object addObject(String ds, String sqlid, Object obj) {
		// TODO Auto-generated method stub

		this.initDataSource(ds);
		Date before = new Date();
		Connection con = null;
		try {
			con = this.getSqlMapClient().getCurrentConnection();
			 con = this.getSqlMapClientTemplate().getSqlMapClient().getCurrentConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object o = getSqlMapClientTemplate().insert(sqlid, obj);

		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return o;

	}
	public int deleteObject(String ds, String sqlid) {
		// TODO Auto-generated method stub
		if (ds == null)
			log.error("DataSource Catn't Be Null When Call deleteObject Method! ");
		this.initDataSource(ds);
		if (getSqlMapClient() == null)
			log.error("Catn't find SqlMapClient by DataSource:" + ds);

		Date before = new Date();
		int ac = getSqlMapClientTemplate().delete(sqlid);
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return ac;
	}

	public int deleteObject(String ds, String sqlid, Object obj) {
		// TODO Auto-generated method stub
		if (ds == null)
			log.error("DataSource Catn't Be Null When Call deleteObject Method! ");
		this.initDataSource(ds);
		if (getSqlMapClient() == null)
			log.error("Catn't find SqlMapClient by DataSource:" + ds);
		Date before = new Date();
		int ac = getSqlMapClientTemplate().delete(sqlid, obj);
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return ac;
	}

	public CommonBean getCommonBean() {
		return commonBean;
	}

	public List getDataList(String ds, String sqlid) {
		// TODO Auto-generated method stub
		this.initDataSource(ds);
		List tempList = null;
		Date before = new Date();
		if (ds != null)
			tempList = getSqlMapClientTemplate().queryForList(sqlid);
		else {
			tempList = new ArrayList();
			Collection dataSources = this.commonBean.getDataSourceInfo().values();
			Iterator it = dataSources.iterator();
			while (it.hasNext()) {
				SqlMapClient smc = (SqlMapClient) it.next();
				this.setSqlMapClient(smc);
				List temp = getSqlMapClientTemplate().queryForList(sqlid);
				tempList.addAll(temp);
			}
		}

		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return tempList;
	}

	public List getDataList(String ds, String sqlid, Object obj) {
		// TODO Auto-generated method stub
		this.initDataSource(ds);
		List tempList = null;
		Date before = new Date();
		if (ds != null)
			tempList = getSqlMapClientTemplate().queryForList(sqlid, obj);
		else {
			tempList = new ArrayList();
			Collection dataSources = this.commonBean.getDataSourceInfo().values();
			Iterator it = dataSources.iterator();
			while (it.hasNext()) {
				SqlMapClient smc = (SqlMapClient) it.next();
				this.setSqlMapClient(smc);
				tempList.addAll(getSqlMapClientTemplate().queryForList(sqlid, obj));
			}
		}
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return tempList;
	}



	public Object getObject(String ds, String sqlid, Object parameterObject) {
		// TODO Auto-generated method stub
		if (ds == null)
			log.error("DataSource Catn't Be Null When Call addObject Method! ");
		this.initDataSource(ds);
		if (getSqlMapClient() == null)
			log.error("Catn't find SqlMapClient by DataSource:" + ds);
		Date before = new Date();
		Object o = getSqlMapClientTemplate().queryForObject(sqlid, parameterObject);
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
		//	this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return o;
	}

	public String getSql(String sqlid) {
		// TODO Auto-generated method stub
		SqlMapClientImpl sqlmap = (SqlMapClientImpl) getSqlMapClient();
		if (sqlmap == null)
			return null;
		String sql = null;
		MappedStatement stmt = sqlmap.getMappedStatement(sqlid);
		// stmt.get
		Sql mapSql = (Sql) stmt.getSql();
		try {
			sql = mapSql.getSql(null, null);
		} catch (Exception e) {
			sql = stmt.getId();
		}
		return sql;
	}

	public SqlMapClient getSqlMapClient() {
		return (SqlMapClient) sqlMapClientLocal.get();
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return new SqlMapClientTemplate(getSqlMapClient());
	}



	public int getWaringTime() {
		return waringTime;
	}

	/**
	 * 这里在set SqlMapClient的时候能保证对于线程是安全的 create time：2010-2-3 下午04:39:23
	 * 
	 * @param dataSource
	 */
	protected void initDataSource(String dataSource) {
		if (dataSource == null)
			return;
		this.setSqlMapClient((SqlMapClient) this.commonBean.getDataSourceInfo().get(dataSource));
	}

	public boolean isSqlExceptionWaring() {
		return sqlExceptionWaring;
	}

	public boolean isTimeOutWaring() {
		return timeOutWaring;
	}



	public void setCommonBean(CommonBean commonBean) {
		if(commonBean==null){
			System.out.println("空....");
		}
		this.commonBean = commonBean;
	}

	public void setSqlExceptionWaring(boolean sqlExceptionWaring) {
		this.sqlExceptionWaring = sqlExceptionWaring;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		sqlMapClientLocal.set(sqlMapClient);
	}



	public void setTimeOutWaring(boolean timeOutWaring) {
		this.timeOutWaring = timeOutWaring;
	}

	public void setWaringTime(int waringTime) {
		this.waringTime = waringTime;
	}

	public int updateObject(String ds, String sqlid) {
		// TODO Auto-generated method stub
		if (ds == null)
			log.error("DataSource Catn't Be Null When Call updateObject Method! ");
		this.initDataSource(ds);
		Date before = new Date();
		int ac = getSqlMapClientTemplate().update(sqlid);
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return ac;
	}

	public int updateObject(String ds, String sqlid, Object obj) {
		// TODO Auto-generated method stub
		if (ds == null)
			log.error("DataSource Catn't Be Null When Call updateObject Method! ");

		this.initDataSource(ds);

		if (getSqlMapClient() == null)
			log.error("Catn't find SqlMapClient by DataSource:" + ds);

		Date before = new Date();
		int ac = getSqlMapClientTemplate().update(sqlid, obj);
		Date after = new Date();
		int woTime = (int) (after.getTime() - before.getTime());
		if (woTime > waringTime) {
			//this.seedSqlWaringEMail(ds, sqlid, woTime);
		}
		return ac;
	}
	public CommonBean getCommonBeanAdmin() {
		return commonBeanAdmin;
	}
	public void setCommonBeanAdmin(CommonBean commonBeanAdmin) {
		this.commonBeanAdmin = commonBeanAdmin;
	}

}
