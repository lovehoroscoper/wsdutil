package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.ForumDAO;
import net.jforum.entities.Forum;
import net.jforum.entities.ForumStats;
import net.jforum.entities.LastPostInfo;

import com.godtips.service.ForumService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:39:40
 * @version v1.0
 */
public class ForumServiceImpl implements ForumService {
	
	private ForumDAO forumDao;
	
	public ForumDAO getForumDao() {
		return forumDao;
	}

	public void setForumDao(ForumDAO forumDao) {
		this.forumDao = forumDao;
	}

	@Override
	public Forum selectById(int forumId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Forum setOrderUp(Forum forum, Forum related) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Forum setOrderDown(Forum forum, Forum related) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int forumId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Forum forum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int addNew(Forum forum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLastPost(int forumId, int postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incrementTotalTopics(int forumId, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementTotalTopics(int forumId, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LastPostInfo getLastPostInfo(int forumId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getModeratorList(int forumId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalMessages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalTopics(int forumId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxPostId(int forumId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveTopics(String[] topics, int fromForumId, int toForumId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List checkUnreadTopics(int forumId, long lastVisit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModerated(int categoryId, boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ForumStats getBoardStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List notifyUsers(Forum forum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void subscribeUser(int forumId, int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUserSubscribed(int forumId, int userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeSubscription(int forumId, int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubscriptionByForum(int forumId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int discoverForumId(String listEmail) {
		// TODO Auto-generated method stub
		return 0;
	}

}
