package com.wsd.service.impl;

import org.springframework.transaction.annotation.Transactional;

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
public class TestOneServiceImpl implements TestService {

	private TestDao testOneDao;
	private TestDao testOne2Dao;

	@Override
	@Transactional(readOnly=true)
	public void addUser(User u) throws Exception {
		testOneDao.addUser(u);
		testOne2Dao.addUser(u);
//		throw new RuntimeException("222");
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


	public TestDao getTestOneDao() {
		return testOneDao;
	}


	public void setTestOneDao(TestDao testOneDao) {
		this.testOneDao = testOneDao;
	}


	public TestDao getTestOne2Dao() {
		return testOne2Dao;
	}


	public void setTestOne2Dao(TestDao testOne2Dao) {
		this.testOne2Dao = testOne2Dao;
	}


}
