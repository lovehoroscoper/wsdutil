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
public class TestTypeServiceImpl implements TestService {

	private TestDao testType1Dao;
	private TestDao testType2Dao;

	@Override
	@Transactional(readOnly=true)
	public void addUser(User u) throws Exception {
		testType1Dao.addUser(u);
		testType2Dao.addUser(u);
		throw new RuntimeException("222");
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


	public TestDao getTestType1Dao() {
		return testType1Dao;
	}


	public void setTestType1Dao(TestDao testType1Dao) {
		this.testType1Dao = testType1Dao;
	}


	public TestDao getTestType2Dao() {
		return testType2Dao;
	}


	public void setTestType2Dao(TestDao testType2Dao) {
		this.testType2Dao = testType2Dao;
	}

}
