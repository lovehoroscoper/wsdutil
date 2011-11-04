package com.wsd.service.impl;

import com.wsd.dao.TestDao;
import com.wsd.service.TestService;
import com.wsd.vo.User;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-2-21 ����10:03:11
 * @version v1.0
 */
public class TestServiceImpl implements TestService {

	private TestDao test1Dao;
	private TestDao test2Dao;

	@Override
	public void addUser(User u) throws Exception {
		test1Dao.addUser(u);
		test2Dao.addUser(u);
		//throw new RuntimeException("222");
	}
	

	@Override
	public void addUser2(User user) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUser3(User user) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUser(User u) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUser2(User u) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUser3(User u) throws Exception {
		// TODO Auto-generated method stub

	}

	public TestDao getTest1Dao() {
		return test1Dao;
	}

	public void setTest1Dao(TestDao test1Dao) {
		this.test1Dao = test1Dao;
	}

	public TestDao getTest2Dao() {
		return test2Dao;
	}

	public void setTest2Dao(TestDao test2Dao) {
		this.test2Dao = test2Dao;
	}

}
