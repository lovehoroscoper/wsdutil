package com.wsd.dao.impl;

import java.util.Map;

import com.wsd.dao.TestDao;
import com.wsd.dao.base.impl.BaseDaoImpl;
import com.wsd.vo.User;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-2-21 ����10:19:44
 * @version v1.0
 */
public class Test2DaoImpl extends BaseDaoImpl implements TestDao {

	@Override
	public void addUser(User u) {
		System.out.println("222222222222222");
		this.addObject("sqlMapClient1", "addUser1", u);
		System.out.println("-------------66666666666666666---------------------------");

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
