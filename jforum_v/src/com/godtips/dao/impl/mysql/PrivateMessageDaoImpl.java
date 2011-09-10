package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.PrivateMessageDAO;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Post;
import net.jforum.entities.PrivateMessage;
import net.jforum.entities.PrivateMessageType;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-7 下午11:21:33
 * @version v1.0
 */
public class PrivateMessageDaoImpl extends BaseDaoImpl implements PrivateMessageDAO {

	@Override
	public void send(PrivateMessage pm) {
		try {
			this.addPm(pm, null);
			this.addPmText(pm);
			pm.setType(PrivateMessageType.NEW);
			this.addPmText(pm);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	protected void addPm(PrivateMessage pm, PreparedStatement p) throws SQLException {
		pm.setType(PrivateMessageType.SENT);
		pm.getPost().setTime(new Date());
		this.addObject("PrivateMessageModel.add", pm);

	}

	protected void addPmText(PrivateMessage pm) throws Exception {
		this.addObject("PrivateMessagesModel.addText", pm);
	}

	@Override
	public void delete(PrivateMessage[] pm, int userId) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		for (int i = 0; i < pm.length; i++) {
			PrivateMessage currentMessage = pm[i];
			paramMap.put("id", currentMessage.getId());

			List list = this.queryList("PrivateMessagesModel.isDeleteAllowed", paramMap);
			if (null != list && list.size() > 0) {
				this.deleteObject("PrivateMessagesModel.deleteText", paramMap);
				this.deleteObject("PrivateMessageModel.delete", paramMap);
			}

		}
	}

	@Override
	public void updateType(PrivateMessage pm) {
		this.updateObject("PrivateMessageModel.updateType", pm);
	}

	@Override
	public List selectFromInbox(User user) {
		try {
			List pmList = new ArrayList();
			List list = this.queryList("PrivateMessageModel.baseListing", user);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					PrivateMessage pm = this.getPm(resMap, false);
					User fromUser = new User();
					fromUser.setId(Integer.parseInt(resMap.get("user_id").toString()));
					fromUser.setUsername((String) resMap.get("username"));
					pm.setFromUser(fromUser);
					pmList.add(pm);
				}
			}
			return pmList;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected PrivateMessage getPm(Map resMap, boolean full) throws SQLException {
		PrivateMessage pm = new PrivateMessage();
		Post p = new Post();

		pm.setId(Integer.parseInt(resMap.get("privmsgs_id").toString()));
		pm.setType(Integer.parseInt(resMap.get("privmsgs_type").toString()));
		p.setTime((Date) resMap.get("privmsgs_date"));
		p.setSubject((String) resMap.get("privmsgs_subject"));

		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		pm.setFormatedDate(df.format(p.getTime()));

		if (full) {
			UserDAO um = DataAccessDriver.getInstance().newUserDAO();
			pm.setFromUser(um.selectById(Integer.parseInt(resMap.get("privmsgs_from_userid").toString())));
			pm.setToUser(um.selectById(Integer.parseInt(resMap.get("privmsgs_to_userid").toString())));

			p.setBbCodeEnabled(Integer.parseInt(resMap.get("privmsgs_enable_bbcode").toString()) == 1);
			p.setSignatureEnabled(Integer.parseInt(resMap.get("privmsgs_attach_sig").toString()) == 1);
			p.setHtmlEnabled(Integer.parseInt(resMap.get("privmsgs_enable_html").toString()) == 1);
			p.setSmiliesEnabled(Integer.parseInt(resMap.get("privmsgs_enable_smilies").toString()) == 1);
			p.setText(this.getPmText(resMap));
		}
		pm.setPost(p);
		return pm;
	}

	protected String getPmText(Map resMap) throws SQLException {
		return (String) resMap.get("privmsgs_text");
	}

	@Override
	public List selectFromSent(User user) {
		try {
			List pmList = new ArrayList();
			List list = this.queryList("PrivateMessageModel.baseListing_sent", user);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map resMap = (Map) list.get(i);
					PrivateMessage pm = this.getPm(resMap, false);
					User toUser = new User();
					toUser.setId(Integer.parseInt(resMap.get("user_id").toString()));
					toUser.setUsername((String) resMap.get("username"));
					pm.setToUser(toUser);
					pmList.add(pm);
				}
			}
			return pmList;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public PrivateMessage selectById(PrivateMessage pm) {
		try {
			List list = this.queryList("PrivateMessageModel.selectById", pm);
			if (null != list && list.size() > 0) {
				Map resMap = (Map) list.get(0);
				pm = this.getPm(resMap);
			}
			return pm;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected PrivateMessage getPm(Map resMap) throws SQLException {
		return this.getPm(resMap, true);
	}
}
