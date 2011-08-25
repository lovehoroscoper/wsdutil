package com.godtips.dao.impl.mysql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.dao.LuceneDAO;
import net.jforum.entities.Post;
import net.jforum.exceptions.DatabaseException;
import net.jforum.search.SearchPost;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;
import com.godtips.util.StringUtils;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-25 下午10:58:17
 * @version v1.0
 */
public class LuceneDaoImpl extends BaseDaoImpl implements LuceneDAO {

	@Override
	public List getPostsToIndex(int fromPostId, int toPostId) {
		List l = new ArrayList();
		try {
			Map param = new HashMap();
			param.put("fromPostId", fromPostId);
			param.put("toPostId", toPostId);
			List list = this.queryList("SearchModel.getPostsToIndexForLucene", param);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					l.add(this.makePost(map));
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

		return l;
	}

	private Post makePost(Map map) throws SQLException {
		Post p = new SearchPost();

		p.setId(Integer.valueOf(map.get("post_id").toString()));
		p.setForumId(Integer.valueOf(map.get("forum_id").toString()));
		p.setTopicId(Integer.valueOf(map.get("topic_id").toString()));
		p.setUserId(Integer.valueOf(map.get("user_id").toString()));
		p.setTime((Date) map.get("post_time"));
		p.setText(this.readPostTextFromResultSet(map));
		p.setBbCodeEnabled(Integer.valueOf(map.get("enable_bbcode").toString()) == 1);
		p.setSmiliesEnabled(Integer.valueOf(map.get("enable_smilies").toString()) == 1);
		String subject = (String) map.get("post_subject");
		if (subject == null || subject.trim().length() == 0) {
			subject = (String) map.get("topic_title");
		}
		p.setSubject(subject);
		return p;
	}

	protected String readPostTextFromResultSet(Map map) throws SQLException {
		return (String) map.get("post_text");
	}

	@Override
	public List getPostsData(int[] postIds) {
		if (postIds.length == 0) {
			return new ArrayList();
		}
		List l = new ArrayList();
		try {
			List list = this.queryList("SearchModel.getPostsDataForLucene", postIds);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					Post post = this.makePost(map);
					post.setPostUsername((String) map.get("username"));
					l.add(post);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return l;
	}

	private String buildInClause(int[] postIds) {
		StringBuffer sb = new StringBuffer(128);
		for (int i = 0; i < postIds.length - 1; i++) {
			sb.append(postIds[i]).append(',');
		}
		sb.append(postIds[postIds.length - 1]);
		return sb.toString();
	}

	@Override
	public int firstPostIdByDate(Date date) {
		return this.getPostIdByDate(date, "SearchModel.firstPostIdByDate");
	}

	private int getPostIdByDate(Date date, String query) {
		int postId = 0;
		String strId = (String) this.findObject(query, new Timestamp(date.getTime()));
		if (!StringUtils.isEmptyOrNullByTrim(strId)) {
			postId = Integer.valueOf(strId);
		}
		return postId;
	}

	@Override
	public int lastPostIdByDate(Date date) {
		return this.getPostIdByDate(date, "SearchModel.lastPostIdByDate");
	}

	@Override
	public int firstPostId() {
		int postId = 0;
		String minId = (String) this.findObject("SearchModel.getFirstPostId", null);
		if (!StringUtils.isEmptyOrNullByTrim(minId)) {
			postId = Integer.valueOf(minId);
		}
		return postId;
	}

}
