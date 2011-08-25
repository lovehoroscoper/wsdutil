package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.KarmaDAO;
import net.jforum.entities.Karma;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-25 下午8:10:07
 * @version v1.0
 */
public class KarmaDaoImpl extends BaseDaoImpl implements KarmaDAO {

	@Override
	public void addKarma(Karma karma) {
		karma.setRateDate(new Date());
		this.addObject("KarmaModel.add", karma);
		this.updateUserKarma(karma.getPostUserId());
	}

	@Override
	public KarmaStatus getUserKarma(int userId) {
		KarmaStatus status = new KarmaStatus();
		List list = this.queryList("KarmaModel.getUserKarma", userId);
		if (null != list && list.size() > 0) {
			double d = (Double) list.get(0);
			status.setKarmaPoints(Math.round(d));
		}
		return status;
	}

	@Override
	public void updateUserKarma(int userId) {

		int totalRecords = 0;
		double totalPoints = 0;
		List list = this.queryList("KarmaModel.getUserKarmaPoints", userId);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				int points = Integer.valueOf(map.get("points").toString());
				int votes = Integer.valueOf(map.get("votes").toString());
				totalPoints += ((double) points / votes);
				totalRecords++;
			}
		}
		double karmaPoints = totalPoints / totalRecords;
		if (Double.isNaN(karmaPoints)) {
			karmaPoints = 0;
		}
		Map upMap = new HashMap();
		upMap.put("karmaPoints", karmaPoints);
		upMap.put("userId", userId);
		this.updateObject("KarmaModel.updateUserKarma", upMap);
	}

	@Override
	public boolean userCanAddKarma(int userId, int postId) {
		boolean status = true;
		Map map = new HashMap();
		map.put("postId", postId);
		map.put("userId", userId);
		int count = (Integer) this.findObject("KarmaModel.userCanAddKarma", map);
		return count < 1;
	}

	@Override
	public KarmaStatus getPostKarma(int postId) {
		KarmaStatus karma = new KarmaStatus();
		List list = this.queryList("KarmaModel.getPostKarma", postId);
		if (null != list && list.size() > 0) {
			double d = (Double) list.get(0);
			karma.setKarmaPoints(Math.round(d));
		}
		return karma;
	}

	@Override
	public void update(Karma karma) {
		this.updateObject("KarmaModel.update", karma);
	}

	@Override
	public Map getUserVotes(int topicId, int userId) {
		Map m = new HashMap();

		Map param = new HashMap();
		param.put("topicId", topicId);
		param.put("userId", userId);
		List list = this.queryList("KarmaModel.getUserVotes", param);
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map) list.get(i);
				m.put((Integer) resMap.get("post_id"), (Integer) resMap.get("points"));
			}
		}
		return m;
	}

	@Override
	public void getUserTotalKarma(User user) {

		List list = this.queryList("KarmaModel.getUserTotalVotes", user);
		user.setKarma(new KarmaStatus());

		if (null != list && list.size() > 0) {
			Map map = (Map) list.get(0);
			user.getKarma().setTotalPoints((Integer) map.get("points"));
			user.getKarma().setVotesReceived((Integer) map.get("votes"));
		}
		if (user.getKarma().getVotesReceived() != 0) {
			// prevetns division by
			// zero.
			user.getKarma().setKarmaPoints(user.getKarma().getTotalPoints() / user.getKarma().getVotesReceived());
		}
		this.getVotesGiven(user);
	}

	private void getVotesGiven(User user) {
		int count = (Integer) this.findObject("KarmaModel.getUserGivenVotes", user);
		user.getKarma().setVotesGiven(count);
	}

	@Override
	public List getMostRatedUserByPeriod(int start, Date firstPeriod, Date lastPeriod, String orderField) {
		String sql = "KarmaModel.getMostRatedUserByPeriod";
		return this.getMostRatedUserByPeriod(sql, orderField, firstPeriod, lastPeriod);
	}

	protected List getMostRatedUserByPeriod(String sql, String orderField, Date firstPeriod, Date lastPeriod) {
		if (firstPeriod.after(lastPeriod)) {
			throw new DatabaseException("First Date needs to be before the Last Date");
		}
		try {
			Map param = new HashMap();
			param.put("orderField", orderField);
			param.put("firstPeriod", firstPeriod);
			param.put("lastPeriod", lastPeriod);
			List list = this.queryList(sql, "");
			return this.fillUser(list);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

	}

	protected List fillUser(List list) throws SQLException {
		List usersAndPoints = new ArrayList();
		KarmaStatus karma = null;
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map) list.get(i);
				User user = new User();
				karma = new KarmaStatus();
				karma.setTotalPoints((Integer) resMap.get("total"));
				karma.setVotesReceived((Integer) resMap.get("votes_received"));
				karma.setKarmaPoints((Double) resMap.get("user_karma"));
				karma.setVotesGiven((Integer) resMap.get("votes_given"));
				user.setUsername((String) resMap.get("username"));
				user.setId((Integer) resMap.get("user_id"));
				user.setKarma(karma);
				usersAndPoints.add(user);
			}
		}
		return usersAndPoints;
	}
}
