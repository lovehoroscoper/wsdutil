package com.wsd.dao.impl;

import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wsd.dao.TestDao;
import com.wsd.dao.base.impl.BaseDaoImpl;
import com.wsd.vo.User;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-2-26 ����09:56:08
 * @version v1.0
 */
public class Test1DaoImpl extends BaseDaoImpl implements TestDao {

	public void addUser(User u) {
		this.addObject("sqlMapClient2", "addUser2", u);
	}

	@Override
	public void addUser2(User u) throws Exception {
		this.getSqlMapClientTemplate().insert("insert_u", u);// this.addObject("man",
																// "insert_u",
																// u);

		// throw new RuntimeException("222aaaaa");
	}

	@Override
	public void addUser_1(User u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHfcore(Map map) {
		// TODO Auto-generated method stub

	}

}
