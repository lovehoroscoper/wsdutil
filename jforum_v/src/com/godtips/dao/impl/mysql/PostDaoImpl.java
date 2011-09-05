package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.PostDAO;
import net.jforum.entities.Post;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-5 下午10:42:53
 * @version v1.0
 */
public class PostDaoImpl extends BaseDaoImpl implements PostDAO {

	@Override
	public Post selectById(int postId) {
		try {
			List list = this.queryList("PostModel.selectById", postId);
			Post post = new Post();
			if (null != list && list.size() > 0) {
				Map resMap = (Map) list.get(0);
				post = this.makePost(resMap);
			}
			return post;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected Post makePost(Map resMap) throws SQLException {
		Post post = new Post();
		post.setId(new Integer(resMap.get("post_id").toString()));
		post.setTopicId(new Integer(resMap.get("topic_id").toString()));
		post.setForumId(new Integer(resMap.get("forum_id").toString()));
		post.setUserId(new Integer(resMap.get("user_id").toString()));

		Date postTime = (Date) resMap.get("post_time");
		post.setTime(postTime);
		post.setUserIp((String) resMap.get("poster_ip"));
		post.setBbCodeEnabled(new Integer(resMap.get("enable_bbcode").toString()) > 0);
		post.setHtmlEnabled(new Integer(resMap.get("enable_html").toString()) > 0);
		post.setSmiliesEnabled(new Integer(resMap.get("user_id").toString()) > 0);
		post.setSignatureEnabled(new Integer(resMap.get("enable_sig").toString()) > 0);
		post.setEditCount(new Integer(resMap.get("post_edit_count").toString()));

		post.setEditTime((Date) resMap.get("post_edit_time") != null ? (Date) resMap.get("post_edit_time") : null);

		post.setSubject((String) resMap.get("post_subject"));
		post.setText(this.getPostTextFromResultSet(resMap));
		post.setPostUsername((String) resMap.get("username"));
		post.hasAttachments(new Integer(resMap.get("attach").toString()) > 0);
		post.setModerate(new Integer(resMap.get("need_moderate").toString()) == 1);

		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		post.setFormatedTime(df.format(postTime));

		post.setKarma(DataAccessDriver.getInstance().newKarmaDAO().getPostKarma(post.getId()));

		return post;
	}

	protected String getPostTextFromResultSet(Map resMap) throws SQLException {
		return (String) resMap.get("post_text");
	}

	@Override
	public void delete(Post post) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Post post) {
		// TODO Auto-generated method stub

	}

	@Override
	public int addNew(Post post) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List selectAllByTopicByLimit(int topicId, int startFrom, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List selectByUserByLimit(int userId, int startFrom, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countUserPosts(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List selectAllByTopic(int topicId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByTopic(int topicId) {
		// TODO Auto-generated method stub

	}

	@Override
	public int countPreviousPosts(int postId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List selectLatestByForumForRSS(int forumId, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List selectHotForRSS(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
