package com.godtips.service;

import java.util.List;

import net.jforum.entities.Forum;
import net.jforum.entities.ForumStats;
import net.jforum.entities.LastPostInfo;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:39:22
 * @version v1.0
 */
public interface ForumService {

	public Forum selectById(int forumId) ;
	
	public List selectAll() ;
	
	public Forum setOrderUp(Forum forum, Forum related) ;
	
	public Forum setOrderDown(Forum forum, Forum related) ;
	
	public void delete(int forumId) ;
		
	public void update(Forum forum) ;
	
	public int addNew(Forum forum) ;

	public void setLastPost(int forumId, int postId) ;

	public void incrementTotalTopics(int forumId, int count) ;
	
	public void decrementTotalTopics(int forumId, int count) ;

	public LastPostInfo getLastPostInfo(int forumId) ;

	public List getModeratorList(int forumId) ;
	
	public int getTotalMessages() ;
	
	public int getTotalTopics(int forumId) ;
	
	public int getMaxPostId(int forumId) ;
	
	public void moveTopics(String[] topics, int fromForumId, int toForumId) ;
	
	public List checkUnreadTopics(int forumId, long lastVisit) ;
	
	public void setModerated(int categoryId, boolean status) ;
	
	public ForumStats getBoardStatus() ;
	
	
	//codes below are added by socialnework@gmail.com for "watching forum" purpose
	public List notifyUsers(Forum forum) ;
	
	
	public void subscribeUser(int forumId, int userId) ;
	
	public boolean isUserSubscribed(int forumId, int userId) ;
	
	public void removeSubscription(int forumId, int userId) ;
	
	public void removeSubscriptionByForum(int forumId) ;

	public int discoverForumId(String listEmail);
}
