package com.godtips.sso.acl.exception;

import org.springframework.security.access.AccessDeniedException;

public class AclAccessFailException extends AccessDeniedException {

	private static final long serialVersionUID = 1L;

	private String redirectUrl;

	public AclAccessFailException(String msg) {
		super(msg);
	}

	public AclAccessFailException(String msg, Throwable t) {
		super(msg, t);
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
