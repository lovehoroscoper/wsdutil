package com.weisd.quartz;

/**
 * 
 * @author Lenovo
 * 
 */
public class TaskBean {

	private String req;

	private int checkCount;// 通知次数

	private String existStr = "false";// 是否已经存在

	private String hforderid;

	private String orderid;

	private String status;

	private String ordermoney;

	private String mark;

	private String ordersource;

	private String errorcode;

	private String agentid;

	private String machineid;
	
	private String newstatus;//转码后的
	
	private String newerrorcode;//转码后的
	
	public String getNewstatus() {
		return newstatus;
	}

	public void setNewstatus(String newstatus) {
		this.newstatus = newstatus;
	}

	public String getNewerrorcode() {
		return newerrorcode;
	}

	public void setNewerrorcode(String newerrorcode) {
		this.newerrorcode = newerrorcode;
	}

	public String getHforderid() {
		return hforderid;
	}

	public void setHforderid(String hforderid) {
		this.hforderid = hforderid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrdermoney() {
		return ordermoney;
	}

	public void setOrdermoney(String ordermoney) {
		this.ordermoney = ordermoney;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOrdersource() {
		return ordersource;
	}

	public void setOrdersource(String ordersource) {
		this.ordersource = ordersource;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getMachineid() {
		return machineid;
	}

	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}

	public String getExistStr() {
		return existStr;
	}

	public void setExistStr(String existStr) {
		this.existStr = existStr;
	}

	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}

	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

}
