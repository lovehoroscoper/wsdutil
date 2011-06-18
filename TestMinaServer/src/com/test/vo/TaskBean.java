package com.test.vo;

import org.apache.mina.core.session.IoSession;

/**
 * 存放每个从客户端发过来的请求任务
 * @author：wangjiang    
 * @since：2011-4-27 上午09:49:41 
 * @version:
 */
public class TaskBean {
	//客户端请求报文
	String req;
	//响应给客户端的报文
	String res;
	//任务处理状态
	String status;
	IoSession clinetSession;
	//是否立即响应客户端（如下单请求就为立即响应，充正请求需要与网关交互后再响应）
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
