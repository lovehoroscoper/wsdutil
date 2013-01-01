package org.gonetbar.ssa.entity;

import java.io.Serializable;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-11 下午12:42:47
 */
public class UserProviderInfoVo implements Serializable {

	private String providerid;
	private String thirduserid;
	private String username;// my systemid
	private String createdate;

	public final String getProviderid() {
		return providerid;
	}

	public final void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public final String getThirduserid() {
		return thirduserid;
	}

	public final void setThirduserid(String thirduserid) {
		this.thirduserid = thirduserid;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getCreatedate() {
		return createdate;
	}

	public final void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

}
