package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Group;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.exceptions.ForumException;
import net.jforum.sso.LoginAuthenticator;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;
import com.godtips.util.StringUtils;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-10 上午11:14:11
 * @version v1.0
 */
public class UserDaoImpl extends BaseDaoImpl implements UserDAO {

	private static LoginAuthenticator loginAuthenticator;

	/**
	 * 这里需要修改 不知道有没有问题
	 */
	public UserDaoImpl() {
		loginAuthenticator = (LoginAuthenticator) SystemGlobals.getObjectValue(ConfigKeys.LOGIN_AUTHENTICATOR_INSTANCE);

		if (loginAuthenticator == null) {
			throw new ForumException("There is no login.authenticator configured. Check your login.authenticator configuration key.");
		}

		loginAuthenticator.setUserModel(this);
	}

	public List pendingActivations() {
		List list = this.queryList("UserModel.pendingActivations", null);
		return list;
	}

	public User selectById(int userId) {
		try {
			List list = this.queryList("UserModel.selectById", userId);
			User u = new User();
			if (null != list && list.size() > 0) {
				Map resMap = (Map) list.get(0);
				this.fillUserFromResultSet(u, resMap);
				u.setPrivateMessagesCount(Integer.parseInt(resMap.get("private_messages").toString()));
				// User groups
				List glist = this.queryList("UserModel.selectGroups", userId);
				if (null != glist && glist.size() > 0) {
					for (int i = 0; i < glist.size(); i++) {
						Group g = (Group) glist.get(i);
						u.getGroupsList().add(g);
					}
				}
			}
			return u;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public User selectByName(String username) {
		try {
			User u = null;
			List list = this.queryList("UserModel.selectByName", username);
			if (null != list && list.size() > 0) {
				u = new User();
				Map resMap = (Map) list.get(0);
				this.fillUserFromResultSet(u, resMap);
			}
			return u;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected void fillUserFromResultSet(User u, Map resMap) throws SQLException {
		u.setAim((String) resMap.get("user_aim"));
		u.setAvatar((String) resMap.get("user_avatar"));
		u.setGender((String) resMap.get("gender"));
		u.setRankId(Integer.parseInt(resMap.get("rank_id").toString()));
		u.setThemeId(Integer.parseInt(resMap.get("themes_id").toString()));
		u.setPrivateMessagesEnabled(Integer.parseInt(resMap.get("user_allow_pm").toString()) == 1);
		u.setNotifyOnMessagesEnabled(Integer.parseInt(resMap.get("user_notify").toString()) == 1);
		u.setViewOnlineEnabled(Integer.parseInt(resMap.get("user_viewonline").toString()) == 1);
		u.setPassword((String) resMap.get("user_password"));
		u.setViewEmailEnabled(Integer.parseInt(resMap.get("user_viewemail").toString()) == 1);
		u.setViewOnlineEnabled(Integer.parseInt(resMap.get("user_viewonline").toString()) == 1);
		u.setAvatarEnabled(Integer.parseInt(resMap.get("user_allowavatar").toString()) == 1);
		u.setBbCodeEnabled(Integer.parseInt(resMap.get("user_allowbbcode").toString()) == 1);
		u.setHtmlEnabled(Integer.parseInt(resMap.get("user_allowhtml").toString()) == 1);
		u.setSmiliesEnabled(Integer.parseInt(resMap.get("user_allowsmilies").toString()) == 1);
		u.setEmail((String) resMap.get("user_email"));
		u.setFrom((String) resMap.get("user_from"));
		u.setIcq((String) resMap.get("user_icq"));
		u.setId(Integer.parseInt(resMap.get("user_id").toString()));
		u.setInterests((String) resMap.get("user_interests"));
		u.setBiography((String) resMap.get("user_biography"));
		u.setLastVisit((Date) resMap.get("user_lastvisit"));
		u.setOccupation((String) resMap.get("user_occ"));
		u.setTotalPosts(Integer.parseInt(resMap.get("user_posts").toString()));
		u.setRegistrationDate((Date) resMap.get("user_regdate"));
		u.setSignature((String) resMap.get("user_sig"));
		u.setWebSite((String) resMap.get("user_website"));
		u.setYim((String) resMap.get("user_yim"));
		u.setUsername((String) resMap.get("username"));
		u.setAttachSignatureEnabled(Integer.parseInt(resMap.get("user_attachsig").toString()) == 1);
		u.setMsnm((String) resMap.get("user_msnm"));
		u.setLang((String) resMap.get("user_lang"));
		u.setActive(Integer.parseInt(resMap.get("user_active").toString()));
		u.setKarma(new KarmaStatus(u.getId(), Double.parseDouble(resMap.get("user_karma").toString())));
		u.setNotifyPrivateMessagesEnabled(Integer.parseInt(resMap.get("user_notify_pm").toString()) == 1);
		u.setDeleted(Integer.parseInt(resMap.get("deleted").toString()));
		u.setNotifyAlways(Integer.parseInt(resMap.get("user_notify_always").toString()) == 1);
		u.setNotifyText(Integer.parseInt(resMap.get("user_notify_text").toString()) == 1);
		String actkey = (String) resMap.get("user_actkey");
		u.setActivationKey(actkey == null || "".equals(actkey) ? null : actkey);
	}

	public void delete(int userId) {
		Map paramMap = new HashMap();
		paramMap.put("deleted", 1);
		paramMap.put("userId", userId);
		this.deleteObject("UserModel.deletedStatus", paramMap);
	}

	public void update(User user) {
		this.updateObject("UserModel.update", user);
	}

	/**
	 * @see net.jforum.dao.UserDAO#addNew(net.jforum.entities.User)
	 */
	public int addNew(User user) {
		try {
			this.initNewUser(user, "UserModel.addNew");
			int id = user.getId();
			this.addToGroup(id, new int[] { SystemGlobals.getIntValue(ConfigKeys.DEFAULT_USER_GROUP) });
			user.setId(id);
			return id;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected void initNewUser(User user, String sql) throws SQLException {
		this.addObject(sql, user);
	}

	public void addNewWithId(User user) {
		try {
			this.initNewUser(user, "UserModel.addNewWithId");
			this.addToGroup(user.getId(), new int[] { SystemGlobals.getIntValue(ConfigKeys.DEFAULT_USER_GROUP) });
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public void decrementPosts(int userId) {
		this.updateObject("UserModel.decrementPosts", userId);
	}

	public void incrementPosts(int userId) {
		this.updateObject("UserModel.incrementPosts", userId);
	}

	public void setRanking(int userId, int rankingId) {
		Map paramMap = new HashMap();
		paramMap.put("rankingId", rankingId);
		paramMap.put("userId", userId);
		this.updateObject("UserModel.rankingId", paramMap);
	}

	public void setActive(int userId, boolean active) {
		Map paramMap = new HashMap();
		paramMap.put("active", active ? 1 : 0);
		paramMap.put("userId", userId);
		this.updateObject("UserModel.activeStatus", paramMap);
	}

	public void undelete(int userId) {
		Map paramMap = new HashMap();
		paramMap.put("deleted", 0);
		paramMap.put("userId", userId);
		this.updateObject("UserModel.deletedStatus", paramMap);
	}

	public List selectAll() {
		return selectAll(0, 0);
	}

	/**
	 * @see net.jforum.dao.UserDAO#selectAll(int, int)
	 */
	public List selectAll(int startFrom, int count) {
		try {
			List list = null;
			if (count > 0) {
				Map paramMap = new HashMap();
				paramMap.put("startFrom", startFrom);
				paramMap.put("count", count);
				list = this.queryList("UserModel.selectAllByLimit", paramMap);

			} else {
				list = this.queryList("UserModel.selectAll", null);
			}
			return this.processSelectAll(list);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * @see net.jforum.dao.UserDAO#selectAllWithKarma()
	 */
	public List selectAllWithKarma() {
		return this.selectAllWithKarma(0, 0);
	}

	/**
	 * @see net.jforum.dao.UserDAO#selectAllWithKarma(int, int)
	 */
	public List selectAllWithKarma(int startFrom, int count) {
		return this.loadKarma(this.selectAll(startFrom, count));
	}

	protected List processSelectAll(List resList) throws SQLException {
		List list = new ArrayList();
		if (null != resList && resList.size() > 0) {
			for (int i = 0; i < resList.size(); i++) {
				Map resMap = (Map) resList.get(i);
				User u = new User();
				u.setEmail((String) resMap.get("user_email"));
				u.setId(Integer.parseInt(resMap.get("user_id").toString()));
				u.setTotalPosts(Integer.parseInt(resMap.get("user_posts").toString()));
				u.setRegistrationDate((Date) resMap.get("user_regdate"));
				u.setUsername((String) resMap.get("username"));
				u.setDeleted(Integer.parseInt(resMap.get("deleted").toString()));
				KarmaStatus karma = new KarmaStatus();
				karma.setKarmaPoints(Integer.parseInt(resMap.get("user_karma").toString()));
				u.setKarma(karma);
				u.setFrom((String) resMap.get("user_from"));
				u.setWebSite((String) resMap.get("user_website"));
				u.setViewEmailEnabled(Integer.parseInt(resMap.get("user_viewemail").toString()) == 1);
				list.add(u);
			}
		}
		return list;
	}

	public List selectAllByGroup(int groupId, int start, int count) {
		try {
			Map paramMap = new HashMap();
			paramMap.put("groupId", groupId);
			paramMap.put("start", start);
			paramMap.put("count", count);
			List list = this.queryList("UserModel.selectAllByGroup", paramMap);
			return this.processSelectAll(list);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public User getLastUserInfo() {
		User u = new User();
		List list = this.queryList("UserModel.lastUserRegistered", null);
		if (null != list && list.size() > 0) {
			Map resMap = (Map) list.get(0);
			u.setUsername((String) resMap.get("username"));
			u.setId(Integer.parseInt(resMap.get("user_id").toString()));
		}
		return u;
	}

	public int getTotalUsers() {
		try {
			return this.getTotalUsersCommon("UserModel.totalUsers", null);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public int getTotalUsersByGroup(int groupId) {
		try {
			Map paramMap = new HashMap();
			paramMap.put("groupId", groupId);
			return this.getTotalUsersCommon("UserModel.totalUsersByGroup", paramMap);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	protected int getTotalUsersCommon(String sql, Map paramMap) throws SQLException {
		int total = (Integer) this.findObject(sql, paramMap);
		return total;
	}

	public boolean isDeleted(int userId) {
		int deleted = 0;
		List list = this.queryList("UserModel.isDeleted", userId);
		if (null != list && list.size() > 0) {
			deleted = (Integer) list.get(0);
		}
		return deleted == 1;
	}

	public boolean isUsernameRegistered(String username) {
		boolean status = false;
		List list = this.queryList("UserModel.isUsernameRegistered", username);
		if (null != list && list.size() > 0) {
			status = (Integer) list.get(0) > 0;
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * @see net.jforum.dao.UserDAO#validateLogin(java.lang.String,
	 *      java.lang.String)
	 */
	public User validateLogin(String username, String password) {
		return loginAuthenticator.validateLogin(username, password, null);
	}

	public void addToGroup(int userId, int[] groupId) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		for (int i = 0; i < groupId.length; i++) {
			paramMap.put("groupId", groupId[i]);
			this.addObject("UserModel.addToGroup", paramMap);
		}
	}

	public void removeFromGroup(int userId, int[] groupId) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		for (int i = 0; i < groupId.length; i++) {
			this.deleteObject("UserModel.removeFromGroup", paramMap);
			paramMap.put("groupId", groupId[i]);
		}
	}

	public void saveNewPassword(String password, String email) {

		Map paramMap = new HashMap();
		paramMap.put("password", password);
		paramMap.put("email", email);
		this.updateObject("UserModel.saveNewPassword", paramMap);
	}

	public boolean validateLostPasswordHash(String email, String hash) {
		boolean status = false;
		Map paramMap = new HashMap();
		paramMap.put("hash", hash);
		paramMap.put("email", email);
		int count = (Integer) this.findObject("UserModel.validateLostPasswordHash", paramMap);
		if (count > 0) {
			status = true;
			this.writeLostPasswordHash(email, "");
		}
		return status;
	}

	public void writeLostPasswordHash(String email, String hash) {
		Map paramMap = new HashMap();
		paramMap.put("hash", hash);
		paramMap.put("email", email);
		this.updateObject("UserModel.writeLostPasswordHash", paramMap);
	}

	public String getUsernameByEmail(String email) {
		String username = "";
		List list = this.queryList("UserModel.getUsernameByEmail", email);
		if (null != list && list.size() > 0) {
			username = (String) list.get(0);
		}
		return username;
	}

	public List findByName(String input, boolean exactMatch) {
		List list = this.queryList("UserModel.findByName", exactMatch ? input : "%" + input + "%");
		return list;
	}

	public boolean validateActivationKeyHash(int userId, String hash) {
		boolean status = false;
		Map paramMap = new HashMap();
		paramMap.put("hash", hash);
		paramMap.put("userId", userId);
		int count = (Integer) this.findObject("UserModel.validateActivationKeyHash", paramMap);
		status = count == 1;
		return status;
	}

	public void writeUserActive(int userId) {
		this.updateObject("UserModel.writeUserActive", userId);
	}

	public void updateUsername(int userId, String username) {
		Map paramMap = new HashMap();
		paramMap.put("username", username);
		paramMap.put("userId", userId);
		this.updateObject("UserModel.updateUsername", paramMap);
	}

	public boolean hasUsernameChanged(int userId, String usernameToCheck) {
		String dbUsername = null;
		// TODO usernameToCheck没用到------------------
		Map paramMap = new HashMap();
		paramMap.put("usernameToCheck", usernameToCheck);
		paramMap.put("userId", userId);
		dbUsername = (String) this.findObject("UserModel.getUsername", paramMap);
		boolean status = false;
		if (!usernameToCheck.equals(dbUsername)) {
			status = true;
		}
		return status;
	}

	/**
	 * Load KarmaStatus from a list of users.
	 * 
	 * @param users
	 *            List
	 * @return List
	 * @throws SQLException
	 */
	protected List loadKarma(List users) {
		List result = new ArrayList(users.size());

		for (Iterator iter = users.iterator(); iter.hasNext();) {
			User user = (User) iter.next();

			// Load Karma
			DataAccessDriver.getInstance().newKarmaDAO().getUserTotalKarma(user);
			result.add(user);
		}

		return result;
	}

	public void saveUserAuthHash(int userId, String hash) {
		Map paramMap = new HashMap();
		paramMap.put("hash", hash);
		paramMap.put("userId", userId);
		this.updateObject("UserModel.saveUserAuthHash", paramMap);
	}

	public String getUserAuthHash(int userId) {
		String hash = null;
		List list = this.queryList("UserModel.getUserAuthHash", userId);
		if (null != list && list.size() > 0) {
			hash = (String) list.get(0);
		}
		return hash;
	}

	public User findByEmail(String email) {
		User u = null;
		try {
			List list = this.queryList("UserModel.findByEmail", email);
			if (null != list && list.size() > 0) {
				Map resMap = (Map) list.get(0);
				u = new User();
				fillUserFromResultSet(u, resMap);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		return u;
	}
}
