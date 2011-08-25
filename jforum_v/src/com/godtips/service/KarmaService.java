package com.godtips.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.jforum.entities.Karma;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.User;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-25 下午8:10:18
 * @version v1.0
 */
public interface KarmaService {

	public void addKarma(Karma karma) ;
	
	public KarmaStatus getUserKarma(int userId) ;
	
	public void updateUserKarma(int userId) ;
	
	public boolean userCanAddKarma(int userId, int postId) ;
	
	public KarmaStatus getPostKarma(int postId) ;
	
	public void update(Karma karma) ;
	
	public Map getUserVotes(int topicId, int userId) ;
	
	public void getUserTotalKarma(User user) ;
	
	public List getMostRatedUserByPeriod(int start, Date firstPeriod, Date lastPeriod, String orderField) ;
}
