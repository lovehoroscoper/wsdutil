package com.wsd.dao.impl;

import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wsd.dao.TestDao;
import com.wsd.vo.User;


/**
 * @desc: 
 *
 *
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-11-4 下午10:52:05
 * @version:v1.0
 *
 */
public class TestOne2DaoImpl extends SqlMapClientDaoSupport implements TestDao {

	@Override
	public void addUser(User u) {
		this.getSqlMapClientTemplate().insert("addUser2", u);
	}

	@Override
	public void addUser_1(User u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUser2(User u) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHfcore(Map map) {
		// TODO Auto-generated method stub

	}

}
