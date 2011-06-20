package com.test.vo;

import org.apache.mina.core.session.IoSession;


public class TaskBean {
	String req;
	String res;
	String status;
	IoSession clinetSession;
	boolean isImmBack = true;
	

	public boolean isImmBack() {
		return isImmBack;
	}
	public void setImmBack(boolean isImmBack) {
		this.isImmBack = isImmBack;
	}
	public String getReq() {
		return req;
	}
	public void setReq(String req) {
		this.req = req;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public IoSession getClinetSession() {
		return clinetSession;
	}
	public void setClinetSession(IoSession clinetSession) {
		this.clinetSession = clinetSession;
	}
	
	

}
