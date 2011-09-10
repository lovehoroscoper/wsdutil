package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.dao.PollDAO;
import net.jforum.dao.PostDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.repository.ForumRepository;
import net.jforum.search.SearchArgs;
import net.jforum.search.SearchResult;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-8 下午9:37:33
 * @version v1.0
 */
public class TopicDaoImpl extends BaseDaoImpl implements TopicDAO {

	private ForumDAO forumDao;

	private PostDAO postDao;

	private PollDAO pollDao;

	public ForumDAO getForumDao() {
		return forumDao;
	}

	public void setForumDao(ForumDAO forumDao) {
		this.forumDao = forumDao;
	}

	public PostDAO getPostDao() {
		return postDao;
	}

	public void setPostDao(PostDAO postDao) {
		this.postDao = postDao;
	}

	public PollDAO getPollDao() {
		return pollDao;
	}

	public void setPollDao(PollDAO pollDao) {
		this.pollDao = pollDao;
	}

	public SearchResult findTopicsByDateRange(SearchArgs args) {
		SearchResult result = null;
		try {
			List l = new ArrayList();
			int counter = 0;
			List list = this.queryList("TopicModel.findTopicsByDateRange", args);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (counter >= args.startFrom() && counter < args.startFrom() + args.fetchCount()) {
						l.add(new Integer((String) list.get(i)));
					}
					counter++;
				}
			}
			result = new SearchResult(this.newMessages(l), counter);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
		return result;
	}

	public void fixFirstLastPostId(int topicId) {
		List list = this.queryList("TopicModel.getFirstLastPostId", topicId);
		if (null != list && list.size() > 0) {
			Map resMap = (Map) list.get(0);
			int first = Integer.parseInt(resMap.get("first_post_id").toString());
			int last = Integer.parseInt(resMap.get("last_post_id").toString());
			Map paramMap = new HashMap();
			paramMap.put("first", first);
			paramMap.put("last", last);
			paramMap.put("topicId", topicId);
			this.updateObject("TopicModel.fixFirstLastPostId", paramMap);
		}
	}

	public Topic selectById(int topicId) {
		Map paramMap = new HashMap();
		paramMap.put("topicId", topicId);
		Topic t = new Topic();
		List l = this.fillTopicsData("TopicModel.selectById", paramMap);
		if (l.size() > 0) {
			t = (Topic) l.get(0);
		}
		return t;
	}

	public Topic selectRaw(int topicId) {
		try {
			Topic t = new Topic();
			List list = this.queryList("TopicModel.selectRaw", topicId);
			if (null != list && list.size() > 0) {
				Map resMap = (Map) list.get(0);
				t = this.getBaseTopicData(resMap);
			}
			return t;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public void delete(Topic topic, boolean fromModeration) {
		List l = new ArrayList();
		l.add(topic);
		this.deleteTopics(l, fromModeration);
	}

	public void deleteTopics(List topics, boolean fromModeration) {
		for (Iterator iter = topics.iterator(); iter.hasNext();) {
			Topic topic = (Topic) iter.next();
			// Remove watches
			this.removeSubscriptionByTopic(topic.getId());

			// Remove the messages
			postDao.deleteByTopic(topic.getId());

			// Remove the poll
			pollDao.deleteByTopicId(topic.getId());

			// Delete the topic itself
			this.deleteObject("TopicModel.delete", topic);

			if (!fromModeration) {
				forumDao.decrementTotalTopics(topic.getForumId(), 1);
			}
		}

	}

	public void deleteByForum(int forumId) {
		List topics = new ArrayList();
		List list = this.queryList("TopicModel.deleteByForum", forumId);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map) list.get(i);
				Topic t = new Topic();
				t.setId(Integer.parseInt(resMap.get("topic_id").toString()));
				t.setForumId(forumId);
				topics.add(t);
			}
		}
		this.deleteTopics(topics, false);
	}

	public void update(Topic topic) {
		this.updateObject("TopicModel.update", topic);
	}

	public int addNew(Topic topic) {
		this.addObject("TopicModel.addNew", topic);
		return topic.getId();
	}

	public void incrementTotalViews(int topicId) {
		this.updateObject("TopicModel.incrementTotalViews", topicId);
	}

	public void incrementTotalReplies(int topicId) {
		this.updateObject("TopicModel.incrementTotalReplies", topicId);
	}

	public void decrementTotalReplies(int topicId) {
		this.updateObject("TopicModel.decrementTotalReplies", topicId);
	}

	public void setLastPostId(int topicId, int postId) {
		Map paramMap = new HashMap();
		paramMap.put("topicId", topicId);
		paramMap.put("postId", postId);
		this.updateObject("TopicModel.setLastPostId", postId);
	}

	public List selectAllByForum(int forumId) {
		return this.selectAllByForumByLimit(forumId, 0, Integer.MAX_VALUE);
	}

	public List selectAllByForumByLimit(int forumId, int startFrom, int count) {
		Map paramMap = new HashMap();
		paramMap.put("forumId", forumId);
		paramMap.put("forumId", forumId);
		paramMap.put("startFrom", startFrom);
		paramMap.put("count", count);
		return this.fillTopicsData("TopicModel.selectAllByForumByLimit", paramMap);
	}

	public List selectByUserByLimit(int userId, int startFrom, int count) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("startFrom", startFrom);
		paramMap.put("count", count);
		paramMap.put("fids", ForumRepository.getListAllowedForums());
		List list = this.fillTopicsData("TopicModel.selectByUserByLimit", paramMap);
		return list;
	}

	public int countUserTopics(int userId) {
		int total = 0;
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("fids", ForumRepository.getListAllowedForums());
		total = (Integer) this.findObject("TopicModel.countUserTopics", paramMap);
		return total;
	}

	protected Topic getBaseTopicData(Map resMap) throws SQLException {
		Topic t = new Topic();
		t.setTitle((String) resMap.get("topic_title"));
		t.setId(Integer.parseInt(resMap.get("topic_id").toString()));
		t.setTime((Date) resMap.get("topic_time"));
		t.setStatus(Integer.parseInt(resMap.get("topic_status").toString()));
		t.setTotalViews(Integer.parseInt(resMap.get("topic_views").toString()));
		t.setTotalReplies(Integer.parseInt(resMap.get("topic_replies").toString()));
		t.setFirstPostId(Integer.parseInt(resMap.get("topic_first_post_id").toString()));
		t.setLastPostId(Integer.parseInt(resMap.get("topic_last_post_id").toString()));
		t.setType(Integer.parseInt(resMap.get("topic_type").toString()));
		t.setForumId(Integer.parseInt(resMap.get("forum_id").toString()));
		t.setModerated(Integer.parseInt(resMap.get("moderated").toString()) == 1);
		t.setVoteId(Integer.parseInt(resMap.get("topic_vote_id").toString()));
		t.setMovedId(Integer.parseInt(resMap.get("topic_moved_id").toString()));
		User user = new User();
		user.setId(Integer.parseInt(resMap.get("user_id").toString()));
		t.setPostedBy(user);
		return t;
	}

	public int getMaxPostId(int topicId) {
		int id = -1;
		List list = this.queryList("TopicModel.getMaxPostId", topicId);
		if (null != list && list.size() > 0) {
			id = (Integer) list.get(0);
		}
		return id;
	}

	public int getTotalPosts(int topicId) {
		int total = 0;
		total = (Integer) this.findObject("TopicModel.getTotalPosts", topicId);
		return total;
	}

	public List notifyUsers(Topic topic) {
		int posterId = SessionFacade.getUserSession().getUserId();
		int anonUser = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);
		Map paramMap = new HashMap();
		paramMap.put("id", topic.getId());
		paramMap.put("posterId", posterId);
		paramMap.put("anonUser", anonUser);
		List list = this.queryList("TopicModel.notifyUsers", paramMap);
		List users = new ArrayList();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map) list.get(i);
				User user = new User();
				user.setId(Integer.parseInt(resMap.get("user_id").toString()));
				user.setEmail((String) resMap.get("user_email"));
				user.setUsername((String) resMap.get("username"));
				user.setLang((String) resMap.get("user_lang"));
				user.setNotifyText(Integer.parseInt(resMap.get("user_notify_text").toString()) == 1);
				users.add(user);
			}
		}
		return users;
	}

	public void subscribeUsers(int topicId, List users) {
		Map paramMap = new HashMap();
		paramMap.put("topicId", topicId);
		for (Iterator iter = users.iterator(); iter.hasNext();) {
			int userId = ((User) iter.next()).getId();
			paramMap.put("userId", userId);
			this.addObject("TopicModel.subscribeUser", paramMap);
		}
	}

	public void subscribeUser(int topicId, int userId) {
		User user = new User();
		user.setId(userId);
		List l = new ArrayList();
		l.add(user);
		this.subscribeUsers(topicId, l);
	}

	public boolean isUserSubscribed(int topicId, int userId) {
		Map paramMap = new HashMap();
		paramMap.put("topicId", topicId);
		paramMap.put("userId", userId);
		List list = this.queryList("TopicModel.isUserSubscribed", paramMap);
		if (null != list && list.size() > 0) {
			return (Integer) list.get(0) > 0;
		} else {
			return false;
		}
	}

	/**
	 * @see net.jforum.dao.TopicDAO#removeSubscription(int, int)
	 */
	public void removeSubscription(int topicId, int userId) {
		Map paramMap = new HashMap();
		paramMap.put("topicId", topicId);
		paramMap.put("userId", userId);
		this.deleteObject("TopicModel.removeSubscription", paramMap);
	}

	/**
	 * @see net.jforum.dao.TopicDAO#removeSubscriptionByTopic(int)
	 */
	public void removeSubscriptionByTopic(int topicId) {
		this.deleteObject("TopicModel.removeSubscriptionByTopic", topicId);
	}

	public void updateReadStatus(int topicId, int userId, boolean read) {
		if (this.isUserSubscribed(topicId, userId)) {
			Map paramMap = new HashMap();
			paramMap.put("read", read ? 1 : 0);
			paramMap.put("topicId", topicId);
			paramMap.put("userId", userId);
			paramMap.put("TopicModel.updateReadStatus", paramMap);
			this.updateObject("TopicModel.updateReadStatus", paramMap);
		}
	}

	public void lockUnlock(int[] topicId, int status) {
		Map paramMap = new HashMap();
		paramMap.put("status", status);
		for (int i = 0; i < topicId.length; i++) {
			paramMap.put("topicId", topicId[i]);
			this.updateObject("TopicModel.lockUnlock", paramMap);
		}
	}

	private List newMessages(List topicIds) {
		if (topicIds.size() == 0) {
			return new ArrayList();
		}
		StringBuffer sb = new StringBuffer();

		for (Iterator iter = topicIds.iterator(); iter.hasNext();) {
			sb.append(iter.next()).append(',');
		}

		sb.append("-1");
		Map paramMap = new HashMap();
		paramMap.put("topicIds", sb.toString());

		return this.fillTopicsData("TopicModel.selectForNewMessages", paramMap);
	}

	public List fillTopicsData(String sqlId, Map paramMap) {
		List l = new ArrayList();
		try {
			List list = this.queryList(sqlId, paramMap);
			SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
			StringBuffer sbFirst = new StringBuffer(128);
			StringBuffer sbLast = new StringBuffer(128);

			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					Topic t = this.getBaseTopicData(resMap);
					// Posted by
					User u = new User();
					u.setId(Integer.parseInt(resMap.get("user_id").toString()));
					t.setPostedBy(u);

					// Last post by
					u = new User();
					u.setId(Integer.parseInt(resMap.get("last_user_id").toString()));
					t.setLastPostBy(u);

					t.setHasAttach(Integer.parseInt(resMap.get("attach").toString()) > 0);
					t.setFirstPostTime(df.format((Date) resMap.get("topic_time")));
					t.setLastPostTime(df.format((Date) resMap.get("post_time")));
					t.setLastPostDate((Date) resMap.get("post_time"));
					l.add(t);
					sbFirst.append(Integer.parseInt(resMap.get("user_id").toString())).append(',');
					sbLast.append(Integer.parseInt(resMap.get("last_user_id").toString())).append(',');
				}
			}
			// Users
			if (sbFirst.length() > 0) {
				sbLast.delete(sbLast.length() - 1, sbLast.length());

				Map users = new HashMap();

				List list_2 = this.queryList("getUserInformation_ID", sbFirst.toString() + sbLast.toString());
				if (null != list_2 && list_2.size() > 0) {
					Map resMap = (Map) list_2.get(0);
					users.put(new Integer(resMap.get("user_id").toString()), (String) resMap.get("username"));
				}
				for (Iterator iter = l.iterator(); iter.hasNext();) {
					Topic t = (Topic) iter.next();
					t.getPostedBy().setUsername((String) users.get(new Integer(t.getPostedBy().getId())));
					t.getLastPostBy().setUsername((String) users.get(new Integer(t.getLastPostBy().getId())));
				}
			}

			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public List selectRecentTopics(int limit) {
		Map paramMap = new HashMap();
		paramMap.put("limit", limit);
		List list = this.fillTopicsData("TopicModel.selectRecentTopicsByLimit", paramMap);
		return list;
	}

	public List selectHottestTopics(int limit) {
		Map paramMap = new HashMap();
		paramMap.put("limit", limit);
		List list = this.fillTopicsData("TopicModel.selectHottestTopicsByLimit", paramMap);
		return list;
	}

	public void setFirstPostId(int topicId, int postId) {
		Map paramMap = new HashMap();
		paramMap.put("postId", postId);
		paramMap.put("topicId", topicId);
		this.queryList("TopicModel.setFirstPostId", paramMap);
	}

	public int getMinPostId(int topicId) {
		int id = -1;
		List list = this.queryList("TopicModel.getMinPostId", topicId);
		if (null != list && list.size() > 0) {
			id = (Integer) list.get(0);
		}
		return id;
	}

	public void setModerationStatus(int forumId, boolean status) {
		Map paramMap = new HashMap();
		paramMap.put("status", status ? 1 : 0);
		paramMap.put("forumId", forumId);
		this.updateObject("TopicModel.setModerationStatus", paramMap);
	}

	public void setModerationStatusByTopic(int topicId, boolean status) {
		Map paramMap = new HashMap();
		paramMap.put("status", status ? 1 : 0);
		paramMap.put("topicId", topicId);
		this.updateObject("TopicModel.setModerationStatusByTopic", paramMap);
	}

	public List selectTopicTitlesByIds(Collection idList) {
		List l = new ArrayList();
		StringBuffer sb = new StringBuffer(idList.size() * 2);
		for (Iterator iter = idList.iterator(); iter.hasNext();) {
			sb.append(iter.next()).append(",");
		}
		int len = sb.length();
		List list = this.queryList("TopicModel.selectTopicTitlesByIds", len > 0 ? sb.toString().substring(0, len - 1) : "0");
		return list;
	}

	/**
	 * @see net.jforum.model.UserModel#topicPosters(int)
	 */
	public Map topicPosters(int topicId) {
		Map m = new HashMap();
		StringBuffer sb = new StringBuffer();
		List list = this.queryList("TopicModel.distinctPosters", topicId);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sb.append((Integer) list.get(i)).append(',');
			}
		}
		List list_2 = this.queryList("TopicModel.topicPosters", sb.substring(0, sb.length() - 1));
		if (null != list_2 && list_2.size() > 0) {
			for (int i = 0; i < list_2.size(); i++) {
				Map resMap = (Map) list_2.get(i);
				User u = new User();
				u.setId(Integer.parseInt(resMap.get("user_id").toString()));
				u.setUsername((String) resMap.get("username"));
				u.setKarma(new KarmaStatus(u.getId(), Double.parseDouble(resMap.get("user_karma").toString())));
				u.setAvatar((String) resMap.get("user_avatar"));
				u.setAvatarEnabled(Integer.parseInt(resMap.get("user_allowavatar").toString()) == 1);
				u.setRegistrationDate((Date) resMap.get("user_regdate"));
				u.setTotalPosts(Integer.parseInt(resMap.get("user_posts").toString()));
				u.setFrom((String) resMap.get("user_from"));
				u.setEmail((String) resMap.get("user_email"));
				u.setRankId(Integer.parseInt(resMap.get("rank_id").toString()));
				u.setViewEmailEnabled(Integer.parseInt(resMap.get("user_viewemail").toString()) == 1);
				u.setIcq((String) resMap.get("user_icq"));
				u.setAttachSignatureEnabled(Integer.parseInt(resMap.get("user_attachsig").toString()) == 1);
				u.setMsnm((String) resMap.get("user_msnm"));
				u.setYim((String) resMap.get("user_yim"));
				u.setWebSite((String) resMap.get("user_website"));
				u.setAim((String) resMap.get("user_aim"));
				u.setSignature((String) resMap.get("user_sig"));
				m.put(new Integer(u.getId()), u);
			}
		}
		return m;
	}

}
