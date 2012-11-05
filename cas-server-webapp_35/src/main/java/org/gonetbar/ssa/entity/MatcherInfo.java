package org.gonetbar.ssa.entity;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-29 下午3:33:02
 */
public class MatcherInfo {

	private String menuid;// idSource
	private String subsysid;
	private String menuname;// sourceDesc
	private String status;// sourceStatus
	private String linkurl;// sourceKey;//pattern
	private String sort;
	private String systype;
	private String isadmin;

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getSubsysid() {
		return subsysid;
	}

	public void setSubsysid(String subsysid) {
		this.subsysid = subsysid;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSystype() {
		return systype;
	}

	public void setSystype(String systype) {
		this.systype = systype;
	}

	public String getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(String isadmin) {
		this.isadmin = isadmin;
	}

}
