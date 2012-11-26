package org.gonetbar.ssa.cas.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessFailException extends AccessDeniedException {
	
	private String redirectUrl;

	public AccessFailException(String msg) {
		super(msg);
	}

	public AccessFailException(String msg, Throwable t) {
		super(msg, t);
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
