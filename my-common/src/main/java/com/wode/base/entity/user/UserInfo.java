package com.wode.base.entity.user;

import java.io.Serializable;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-7-2 下午5:14:08
 * @version v1.0
 */
public class UserInfo implements Serializable {

	// ----- 子系统信息---start--
	/**
	 * 子系统自己的ID数字
	 */
	private String subSeqid;

	/**
	 * 子系统自己的唯一标志
	 */
	private String subUserKey;

	/**
	 * 子系统自己的用户名
	 */
	private String subUserName;
	// ----- 子系统信息---start--

	// ----- 认证中心信息---start--
	/**
	 * 我方数据库中id_user 自增长数字类型 user_local_seqid
	 */
	public String USER_LOCAL_ID;

	/**
	 * 注册的唯一邮箱username user_local_uniquekey
	 */
	public String USER_LOCAL_UNIQUEKEY;

	/**
	 * UserLoginType.xxx user_login_type
	 */
	public String USER_LOGIN_TYPE;

	/**
	 * 第三方登录ID user_third_uniquekey
	 */
	public String USER_THIRD_UNIQUEKEY;

	/**
	 * 第三方登录类型 third_login_type
	 */
	public String THIRD_LOGIN_TYPE;

	/**
	 * 第三方登录类型编号 third_login_providerid
	 */
	public String THIRD_LOGIN_PROVIDERID;
	// ----- 认证中心信息---end--

	private String password;

	private String passwordBak;

	private String userEmail;

	private String userUrl;

	private String userStatus;
	
	private String userRegistered;
	
	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserRegistered() {
		return userRegistered;
	}

	public void setUserRegistered(String userRegistered) {
		this.userRegistered = userRegistered;
	}

	public String getSubSeqid() {
		return subSeqid;
	}

	public void setSubSeqid(String subSeqid) {
		this.subSeqid = subSeqid;
	}

	public String getSubUserKey() {
		return subUserKey;
	}

	public void setSubUserKey(String subUserKey) {
		this.subUserKey = subUserKey;
	}

	public String getSubUserName() {
		return subUserName;
	}

	public void setSubUserName(String subUserName) {
		this.subUserName = subUserName;
	}

	public String getUSER_LOCAL_ID() {
		return USER_LOCAL_ID;
	}

	public void setUSER_LOCAL_ID(String uSER_LOCAL_ID) {
		USER_LOCAL_ID = uSER_LOCAL_ID;
	}

	public String getUSER_LOCAL_UNIQUEKEY() {
		return USER_LOCAL_UNIQUEKEY;
	}

	public void setUSER_LOCAL_UNIQUEKEY(String uSER_LOCAL_UNIQUEKEY) {
		USER_LOCAL_UNIQUEKEY = uSER_LOCAL_UNIQUEKEY;
	}

	public String getUSER_LOGIN_TYPE() {
		return USER_LOGIN_TYPE;
	}

	public void setUSER_LOGIN_TYPE(String uSER_LOGIN_TYPE) {
		USER_LOGIN_TYPE = uSER_LOGIN_TYPE;
	}

	public String getUSER_THIRD_UNIQUEKEY() {
		return USER_THIRD_UNIQUEKEY;
	}

	public void setUSER_THIRD_UNIQUEKEY(String uSER_THIRD_UNIQUEKEY) {
		USER_THIRD_UNIQUEKEY = uSER_THIRD_UNIQUEKEY;
	}

	public String getTHIRD_LOGIN_TYPE() {
		return THIRD_LOGIN_TYPE;
	}

	public void setTHIRD_LOGIN_TYPE(String tHIRD_LOGIN_TYPE) {
		THIRD_LOGIN_TYPE = tHIRD_LOGIN_TYPE;
	}

	public String getTHIRD_LOGIN_PROVIDERID() {
		return THIRD_LOGIN_PROVIDERID;
	}

	public void setTHIRD_LOGIN_PROVIDERID(String tHIRD_LOGIN_PROVIDERID) {
		THIRD_LOGIN_PROVIDERID = tHIRD_LOGIN_PROVIDERID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordBak() {
		return passwordBak;
	}

	public void setPasswordBak(String passwordBak) {
		this.passwordBak = passwordBak;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

}
