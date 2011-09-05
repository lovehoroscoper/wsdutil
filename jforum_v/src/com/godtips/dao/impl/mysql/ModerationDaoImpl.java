package com.godtips.dao.impl.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.dao.ModerationDAO;
import net.jforum.entities.ModerationPendingInfo;
import net.jforum.entities.Post;
import net.jforum.entities.TopicModerationInfo;
import net.jforum.exceptions.DatabaseException;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-26 下午11:21:18
 * @version v1.0
 */
public class ModerationDaoImpl extends BaseDaoImpl implements ModerationDAO {

	@Override
	public void aprovePost(int postId) {
		this.updateObject("ModerationModel.aprovePost", postId);
	}

	@Override
	public Map topicsByForum(int forumId) {
		Map m = new HashMap();
		try {
			int lastId = 0;
			TopicModerationInfo info = null;
			List list = this.queryList("ModerationModel.topicsByForum", forumId);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					int id = Integer.valueOf(resMap.get("topic_id").toString());
					if (id != lastId) {
						lastId = id;
						if (info != null) {
							m.put(new Integer(info.getTopicId()), info);
						}
						info = new TopicModerationInfo();
						info.setTopicId(id);
						info.setTopicReplies(Integer.valueOf(resMap.get("topic_replies").toString()));
						info.setTopicTitle((String) resMap.get("topic_title"));
					}
					if (info != null) {
						info.addPost(this.getPost(resMap));
					}
				}
			}
			if (info != null) {
				m.put(new Integer(info.getTopicId()), info);
			}
			return m;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected Post getPost(Map resMap) throws SQLException {
		Post p = new Post();
		p.setPostUsername((String) resMap.get("username"));
		p.setId((Integer) resMap.get("post_id"));
		p.setUserId((Integer) resMap.get("user_id"));
		p.setBbCodeEnabled((Integer) resMap.get("enable_bbcode") == 1);
		p.setHtmlEnabled((Integer) resMap.get("enable_html") == 1);
		p.setSmiliesEnabled((Integer) resMap.get("enable_smilies") == 1);
		p.setSubject((String) resMap.get("post_subject"));
		p.setText(this.getPostTextFromResultSet(resMap));
		return p;
	}

	protected String getPostTextFromResultSet(Map resMap) throws SQLException {
		return (String) resMap.get("post_text");
	}

	@Override
	public List categoryPendingModeration() {
		List l = new ArrayList();
		int lastId = 0;
		ModerationPendingInfo info = null;
		List list = this.queryList("ModerationModel.categoryPendingModeration", null);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map) list.get(i);
				int id = (Integer) resMap.get("categories_id");
				if (id != lastId) {
					lastId = id;
					if (info != null) {
						l.add(info);
					}
					info = new ModerationPendingInfo();
					info.setCategoryName((String) resMap.get("title"));
					info.setCategoryId(id);
				}
				if (info != null) {
					info.addInfo((String) resMap.get("forum_name"), (Integer) resMap.get("forum_id"), (Integer) resMap.get("total"));
				}
			}
		}
		if (info != null) {
			l.add(info);
		}
		return l;
	}

}
