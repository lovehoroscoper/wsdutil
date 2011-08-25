package com.godtips.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.jforum.dao.KarmaDAO;
import net.jforum.entities.Karma;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.User;

import com.godtips.service.KarmaService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-25 下午8:10:35
 * @version v1.0
 */
public class KarmaServiceImpl implements KarmaService {

	private KarmaDAO karmaDao;
	
	public KarmaDAO getKarmaDao() {
		return karmaDao;
	}

	public void setKarmaDao(KarmaDAO karmaDao) {
		this.karmaDao = karmaDao;
	}

	@Override
	public void addKarma(Karma karma) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KarmaStatus getUserKarma(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserKarma(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userCanAddKarma(int userId, int postId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KarmaStatus getPostKarma(int postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Karma karma) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map getUserVotes(int topicId, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getUserTotalKarma(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getMostRatedUserByPeriod(int start, Date firstPeriod, Date lastPeriod, String orderField) {
		// TODO Auto-generated method stub
		return null;
	}

}
