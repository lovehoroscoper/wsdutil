package com.godtips.sso.acl.entity;

import java.io.Serializable;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-11 下午12:42:47
 */
public class AclUserVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id_user;
	private String username;
	private String password;
	private int validtype;

	public AclUserVo() {
	}

	public AclUserVo(long id_user, String username, String password, int validtype) {
		this.id_user = id_user;
		this.username = username;
		this.password = password;
		this.validtype = validtype;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getValidtype() {
		return validtype;
	}

	public void setValidtype(int validtype) {
		this.validtype = validtype;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
