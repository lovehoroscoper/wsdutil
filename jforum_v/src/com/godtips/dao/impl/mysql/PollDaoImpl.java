package com.godtips.dao.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.PollDAO;
import net.jforum.entities.Poll;
import net.jforum.entities.PollOption;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-3 下午7:55:26
 * @version v1.0
 */
public class PollDaoImpl extends BaseDaoImpl implements PollDAO {

	@Override
	public Poll selectById(int pollId) {
		List list = this.queryList("PollModel.selectById", pollId);
		Poll poll = null;
		if (null != list && list.size() > 0) {
			poll = (Poll) list.get(0);
			List list2 = this.queryList("PollModel.selectOptionsByPollId", poll);
			if (null != list2 && list2.size() > 0) {
				for (int j = 0; j < list2.size(); j++) {
					PollOption option = (PollOption) list2.get(j);
					poll.addOption(option);
				}
			}
		}
		return poll;
	}

	@Override
	public void delete(int pollId) {
		this.deletePollVotes(pollId);
		this.deleteAllPollOptions(pollId);
		this.deletePoll(pollId);
	}

	protected void deletePollVotes(int pollId) {
		this.deleteObject("PollModel.deletePollVoters", pollId);
	}

	protected void deleteAllPollOptions(int pollId) {
		this.deleteObject("PollModel.deleteAllPollOptions", pollId);
	}

	protected void deletePoll(int pollId) {
		this.deleteObject("PollModel.deletePoll", pollId);
	}

	@Override
	public void deleteByTopicId(int topicId) {
		// first, lookup the poll id, then delete it
		int pollId = 0;
		List list = this.queryList("PollModel.selectPollByTopicId", topicId);
		if (null != list && list.size() > 0) {
			Map map = (Map) list.get(0);
			pollId = Integer.valueOf(map.get("vote_id").toString());

		}
		if (pollId != 0) {
			delete(pollId);
		}
	}

	@Override
	public void update(Poll poll) {
		try {
			this.updatePoll(poll);
			if (poll.getChanges() != null) {
				this.deletePollOptions(poll.getId(), poll.getChanges().getDeletedOptions());
				this.updatePollOptions(poll.getId(), poll.getChanges().getChangedOptions());
				this.addNewPollOptions(poll.getId(), poll.getChanges().getNewOptions());
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected void updatePoll(Poll poll) throws SQLException {
		this.updateObject("PollModel.updatePoll", poll);
	}

	protected void deletePollOptions(int pollId, List deleted) throws SQLException {
		if (null != deleted && deleted.size() > 0) {
			for (Iterator iter = deleted.iterator(); iter.hasNext();) {
				PollOption o = (PollOption) iter.next();
				o.setPollId(pollId);
				this.deleteObject("PollModel.deletePollOption", o);
			}
		}
	}

	protected void updatePollOptions(int pollId, List options) throws SQLException {
		if (null != options && options.size() > 0) {
			for (Iterator iter = options.iterator(); iter.hasNext();) {
				PollOption o = (PollOption) iter.next();
				o.setPollId(pollId);
				this.updateObject("PollModel.updatePollOption", o);
			}
		}
	}

	@Override
	public int addNew(Poll poll) {
		this.addNewPoll(poll);
		this.addNewPollOptions(poll.getId(), poll.getOptions());
		return poll.getId();
	}

	protected void addNewPoll(Poll poll) {
		this.addObject("PollModel.addNewPoll", poll);
	}

	protected void addNewPollOptions(int pollId, List options) {
		int optionId = (Integer) this.findObject("pollId", pollId);
		for (Iterator iter = options.iterator(); iter.hasNext();) {
			PollOption option = (PollOption) iter.next();
			option.setId(++optionId);
			option.setPollId(pollId);
			this.addObject("PollModel.addNewPollOption", option);
		}
	}

	@Override
	public void voteOnPoll(int pollId, int optionId, int userId, String ipAddress) {
		Map paramMap = new HashMap();
		paramMap.put("pollId", pollId);
		paramMap.put("optionId", optionId);
		paramMap.put("userId", userId);
		paramMap.put("ipAddress", ipAddress);
		this.updateObject("PollModel.incrementVoteCount", paramMap);
		this.addObject("PollModel.addNewVoter", paramMap);
	}

	@Override
	public boolean hasUserVotedOnPoll(int pollId, int userId) {
		Map paramMap = new HashMap();
		paramMap.put("pollId", pollId);
		paramMap.put("userId", userId);
		List list = this.queryList("PollModel.selectVoter", paramMap);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean hasUserVotedOnPoll(int pollId, String ipAddress) {
		Map paramMap = new HashMap();
		paramMap.put("pollId", pollId);
		paramMap.put("ipAddress", ipAddress);
		List list = this.queryList("PollModel.selectVoterByIP", paramMap);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
