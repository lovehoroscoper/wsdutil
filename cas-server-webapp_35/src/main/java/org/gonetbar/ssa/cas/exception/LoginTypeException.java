package org.gonetbar.ssa.cas.exception;

import org.springframework.security.access.AccessDeniedException;

public class LoginTypeException extends AccessDeniedException {

	private static final long serialVersionUID = 1L;

	private String redirectUrl;

	public LoginTypeException(String msg) {
		super(msg);
	}

	public LoginTypeException(String msg, Throwable t) {
		super(msg, t);
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
