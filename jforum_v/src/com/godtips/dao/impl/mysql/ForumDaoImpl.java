package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.ForumDAO;
import net.jforum.entities.Forum;
import net.jforum.entities.ForumStats;
import net.jforum.entities.LastPostInfo;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:38:59
 * @version v1.0
 */
public class ForumDaoImpl extends BaseDaoImpl implements ForumDAO {

	@Override
	public List selectAll() {
		try {
			List l = new ArrayList();
			List list = this.queryList("ForumModel.selectAll", null);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					l.add(this.fillForum(resMap));
				}
			}
			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected Forum fillForum(Map resMap) throws SQLException {
		Forum f = new Forum();
		f.setId(Integer.valueOf(resMap.get("forum_id").toString()));
		f.setIdCategories(Integer.valueOf(resMap.get("categories_id").toString()));
		f.setName((String) resMap.get("forum_name"));
		f.setDescription((String) resMap.get("forum_desc"));
		f.setOrder(Integer.valueOf(resMap.get("forum_order").toString()));
		f.setTotalTopics(Integer.valueOf(resMap.get("forum_topics").toString()));
		f.setLastPostId(Integer.valueOf(resMap.get("forum_last_post_id").toString()));
		f.setModerated(Integer.valueOf(resMap.get("moderated").toString()) > 0);
		f.setTotalPosts(this.countForumPosts(f.getId()));
		return f;
	}

	protected int countForumPosts(int forumId) {
		return (Integer) this.findObject("ForumModel.countForumPosts", forumId);
	}

	@Override
	public Forum selectById(int forumId) {
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
