package com.godtips.dao.impl.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.dao.ModerationLogDAO;
import net.jforum.entities.ModerationLog;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-5 下午8:53:45
 * @version v1.0
 */
public class ModerationLogDaoImpl extends BaseDaoImpl implements ModerationLogDAO {

	@Override
	public void add(ModerationLog log) {
		log.setDate(new Date());
		this.addObject("ModerationLog.addNew", log);
	}

	@Override
	public List selectAll(int start, int count) {
		List l = new ArrayList();
		Map paramMap = new HashMap();
		paramMap.put("start", start);
		paramMap.put("count", count);
		try {
			List list = this.queryList("ModerationLog.selectAll", paramMap);
			if (null != list && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					Map resMap = (Map) list.get(j);
					l.add(this.makeLog(resMap));
				}
			}
			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected ModerationLog makeLog(Map resMap) throws SQLException {
		ModerationLog log = new ModerationLog();

		log.setId(new Integer(resMap.get("log_id").toString()));
		log.setDescription(this.readDesriptionFromResultSet(resMap));
		log.setOriginalMessage(this.readOriginalMessageFromResultSet(resMap));
		log.setType(new Integer(resMap.get("log_type").toString()));
		log.setDate((Date) resMap.get("log_date"));
		log.setPostId(new Integer(resMap.get("post_id").toString()));
		log.setTopicId(new Integer(resMap.get("topic_id").toString()));

		User user = new User();
		user.setId(new Integer(resMap.get("user_id").toString()));
		user.setUsername((String) resMap.get("username"));

		log.setUser(user);

		User posterUser = new User();
		posterUser.setId(new Integer(resMap.get("post_user_id").toString()));
		posterUser.setUsername((String) resMap.get("poster_username"));

		log.setPosterUser(posterUser);

		return log;
	}

	protected String readDesriptionFromResultSet(Map resMap) throws SQLException {
		return (String) resMap.get("log_description");
	}

	protected String readOriginalMessageFromResultSet(Map resMap) throws SQLException {
		return (String) resMap.get("log_original_message");
	}

	@Override
	public int totalRecords() {
		int total = 0;
		total = (Integer) this.findObject("ModerationLog.totalRecords", null);
		return total;
	}

}
