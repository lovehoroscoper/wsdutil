package com.godtips.dao.impl.mysql;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.PostDAO;
import net.jforum.entities.Post;
import net.jforum.exceptions.DatabaseException;
import net.jforum.repository.ForumRepository;
import net.jforum.search.SearchFacade;
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
		List l = new ArrayList();
		l.add(post);
		this.removePosts(l);
	}

	private void removePosts(List posts) {
		for (Iterator iter = posts.iterator(); iter.hasNext();) {
			Post p = (Post) iter.next();
			this.deleteObject("PostModel.deletePost", p);
			this.deleteObject("PostModel.deletePostText", p);
			// TODO 未测
			SearchFacade.delete(p);
		}
	}

	@Override
	public void update(Post post) {
		this.updatePostsTable(post);
		this.updatePostsTextTable(post);
		SearchFacade.update(post);
	}

	protected void updatePostsTable(Post post) {
		post.setEditTime(new Date());
		this.updateObject("PostModel.updatePost", post);
	}

	protected void updatePostsTextTable(Post post) {
		this.updateObject("PostModel.updatePostText", post);
	}

	@Override
	public int addNew(Post post) {
		try {
			this.addNewPost(post);
			this.addNewPostText(post);
			// Search
			SearchFacade.create(post);
			return post.getId();
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	protected void addNewPost(Post post) {
		this.addObject("PostModel.addNewPost", post);
	}

	protected void addNewPostText(Post post) throws Exception {
		this.addObject("PostModel.addNewPostText", post);
	}

	@Override
	public List selectAllByTopicByLimit(int topicId, int startFrom, int count) {
		List l = new ArrayList();
		Map paramMap = new HashMap();
		paramMap.put("topicId", topicId);
		paramMap.put("startFrom", startFrom);
		paramMap.put("count", count);
		try {
			List list = this.queryList("PostModel.selectAllByTopicByLimit", paramMap);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					l.add(this.makePost(resMap));
				}
			}
			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

	}

	@Override
	public List selectByUserByLimit(int userId, int startFrom, int count) {
		Map paramMap = new HashMap();
		String fids = ForumRepository.getListAllowedForums();
		paramMap.put("userId", userId);
		paramMap.put("startFrom", startFrom);
		paramMap.put("count", count);
		paramMap.put("fids", fids);
		try {
			List list = this.queryList("PostModel.selectByUserByLimit", paramMap);
			List l = new ArrayList();
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					l.add(this.makePost(resMap));
				}
			}
			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public int countUserPosts(int userId) {
		int total = 0;
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("fids", ForumRepository.getListAllowedForums());
		total = (Integer) this.findObject("PostModel.countUserPosts", paramMap);
		return total;
	}

	/**
	 * @see net.jforum.dao.PostDAO#selectAllBytTopic(int)
	 */
	public List selectAllByTopic(int topicId) {
		return this.selectAllByTopicByLimit(topicId, 0, Integer.MAX_VALUE - 1);
	}

	@Override
	public void deleteByTopic(int topicId) {
		List posts = this.queryList("PostModel.deleteByTopic", topicId);
		this.removePosts(posts);
	}

	@Override
	public int countPreviousPosts(int postId) {
		int total = 0;
		total = (Integer) this.findObject("PostModel.countPreviousPosts", postId);
		return total;
	}

	@Override
	public List selectLatestByForumForRSS(int forumId, int limit) {
		List l = new ArrayList();
		Map paramMap = new HashMap();
		paramMap.put("forumId", forumId);
		paramMap.put("limit", limit);
		try {
			List list = this.queryList("PostModel.selectLatestByForumForRSS", paramMap);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					Post post = this.buildPostForRSS(resMap);
					l.add(post);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return l;
	}

	private Post buildPostForRSS(Map resMap) throws SQLException {
		Post post = new Post();
		post.setId(Integer.parseInt(resMap.get("post_id").toString()));
		post.setSubject((String) resMap.get("subject"));
		post.setText((String) resMap.get("post_text"));
		post.setTopicId(Integer.parseInt(resMap.get("topic_id").toString()));
		post.setForumId(Integer.parseInt(resMap.get("forum_id").toString()));
		post.setUserId(Integer.parseInt(resMap.get("user_id").toString()));
		post.setPostUsername((String) resMap.get("username"));
		post.setTime((Date) resMap.get("post_time"));
		return post;
	}

	@Override
	public List selectHotForRSS(int limit) {
		List l = new ArrayList();
		try {
			List list = this.queryList("PostModel.selectHotForRSS", limit);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					Post post = this.buildPostForRSS(resMap);
					l.add(post);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return l;
	}

}
