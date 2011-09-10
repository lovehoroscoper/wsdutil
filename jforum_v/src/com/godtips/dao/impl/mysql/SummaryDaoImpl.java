package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.SummaryDAO;
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
 * @date 2011-9-8 下午9:19:35
 * @version v1.0
 */
public class SummaryDaoImpl extends BaseDaoImpl implements SummaryDAO {

	@Override
	public List selectLastPosts(Date firstDate, Date lastDate) {
		try {
			Map paramMap = new HashMap();
			paramMap.put("firstDate", firstDate);
			paramMap.put("lastDate", lastDate);
			List posts = new ArrayList();
			List list = this.queryList("SummaryModel.selectPosts", paramMap);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					posts.add(this.fillPost(resMap));
				}
			}
			return posts;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private Post fillPost(Map resMap) throws SQLException {
		Post post = new Post();

		post.setId(Integer.parseInt(resMap.get("post_id").toString()));
		post.setTopicId(Integer.parseInt(resMap.get("topic_id").toString()));
		post.setForumId(Integer.parseInt(resMap.get("forum_id").toString()));
		post.setUserId(Integer.parseInt(resMap.get("user_id").toString()));
		Date postTime = (Date) resMap.get("post_time");
		post.setTime(postTime);
		post.setSubject((String) resMap.get("post_subject"));
		post.setText((String) resMap.get("post_text"));
		post.setPostUsername((String) resMap.get("username"));
		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		post.setFormatedTime(df.format(postTime));
		post.setKarma(DataAccessDriver.getInstance().newKarmaDAO().getPostKarma(post.getId()));
		return post;
	}

	@Override
	public List listRecipients() {
		List recipients = new ArrayList();
		List list = this.queryList("SummaryDAO.selectAllRecipients", null);
		String mail = null;
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map) list.get(i);
				mail = (String) resMap.get("user_email");
				if (mail != null && !mail.trim().equals("")) {
					recipients.add(mail);
				}
			}
		}
		return recipients;
	}

}
