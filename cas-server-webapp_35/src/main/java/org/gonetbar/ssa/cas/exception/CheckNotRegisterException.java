package org.gonetbar.ssa.cas.exception;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.scribe.up.credential.OAuthCredential;
import org.scribe.up.profile.UserProfile;

public class CheckNotRegisterException extends AuthenticationException {

	private OAuthCredential credential;

	private UserProfile userProfile;

	public static final CheckNotRegisterException ERROR = new CheckNotRegisterException();

	private static final long serialVersionUID = 3256719585087797044L;

	public static final String CODE = "error.authentication.credentials.bad";

	public CheckNotRegisterException(final String code, OAuthCredential credential, UserProfile userProfile) {
		super(code);
		this.credential = credential;
		this.userProfile = userProfile;
	}

	public CheckNotRegisterException() {
		super(CODE);
	}

	public CheckNotRegisterException(final Throwable throwable) {
		super(CODE, throwable);
	}

	public CheckNotRegisterException(final String code) {
		super(code);
	}

	public CheckNotRegisterException(final String code, final Throwable throwable) {
		super(code, throwable);
	}

	public final OAuthCredential getCredential() {
		return credential;
	}

	public final UserProfile getUserProfile() {
		return userProfile;
	}
	
	
}
