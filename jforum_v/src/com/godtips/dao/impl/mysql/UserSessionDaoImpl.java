package com.godtips.dao.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.jforum.dao.UserSessionDAO;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-5 下午10:17:15
 * @version v1.0
 */
public class UserSessionDaoImpl extends BaseDaoImpl implements UserSessionDAO {

	@Override
	public void add(UserSession us, Connection conn) {
		this.add(us, conn, false);
	}

	private void add(UserSession us, Connection conn, boolean checked) {
		if (!checked && this.selectById(us, conn) != null) {
			return;
		}
		this.addObject("UserSessionModel.add", us);
	}

	@Override
	public void update(UserSession us, Connection conn) {
		if (this.selectById(us, conn) == null) {
			this.add(us, conn, true);
			return;
		}
		this.updateObject("UserSessionModel.update", us);
	}

	@Override
	public UserSession selectById(UserSession us, Connection conn) {
		List list = this.queryList("UserSessionModel.selectById", us.getUserId());
		boolean found = false;
		UserSession returnUs = new UserSession(us);
		if (null != list && list.size() > 0) {
			Map resMap = (Map) list.get(0);
			returnUs.setSessionTime(new Long(resMap.get("session_time").toString()));
			returnUs.setStartTime((Date) resMap.get("session_start"));
			found = true;
		}
		return (found ? returnUs : null);
	}

}
