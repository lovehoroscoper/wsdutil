package org.gonetbar.ssa.entity;

import java.io.Serializable;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-11 下午12:42:47
 */
public class UserInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id_user;
	private String username;
	private String password;
	private String nikename;
	private int id_site;
	private int logintype;
	private int validtype;
	private String createdate;
	private int encrypttype;
	private String thirdloginid;
	private String email;

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNikename() {
		return nikename;
	}

	public void setNikename(String nikename) {
		this.nikename = nikename;
	}

	public int getId_site() {
		return id_site;
	}

	public void setId_site(int id_site) {
		this.id_site = id_site;
	}

	public int getLogintype() {
		return logintype;
	}

	public void setLogintype(int logintype) {
		this.logintype = logintype;
	}

	public int getValidtype() {
		return validtype;
	}

	public void setValidtype(int validtype) {
		this.validtype = validtype;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public int getEncrypttype() {
		return encrypttype;
	}

	public void setEncrypttype(int encrypttype) {
		this.encrypttype = encrypttype;
	}

	public String getThirdloginid() {
		return thirdloginid;
	}

	public void setThirdloginid(String thirdloginid) {
		this.thirdloginid = thirdloginid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
