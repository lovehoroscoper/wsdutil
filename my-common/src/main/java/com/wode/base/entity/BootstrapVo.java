package com.wode.base.entity;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-4-11 下午3:55:19
 */
public class BootstrapVo {

	private String nameInfo;

	private String cssInfo;
	
	private String pageNo;
	
	private String isCurNo = "false";
	
	public String getIsCurNo() {
		return isCurNo;
	}

	public void setIsCurNo(String isCurNo) {
		this.isCurNo = isCurNo;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getNameInfo() {
		return nameInfo;
	}

	public void setNameInfo(String nameInfo) {
		this.nameInfo = nameInfo;
	}

	public String getCssInfo() {
		return cssInfo;
	}

	public void setCssInfo(String cssInfo) {
		this.cssInfo = cssInfo;
	}

}
