package org.gonetbar.ssa.cas.exception;

import org.gonetbar.ssa.entity.ThirdRegVo;
import org.jasig.cas.authentication.handler.AuthenticationException;

public class CheckNotRegisterException extends AuthenticationException {

	private ThirdRegVo thirdRegVo;

	public static final CheckNotRegisterException ERROR = new CheckNotRegisterException();

	private static final long serialVersionUID = 3256719585087797044L;

	public static final String CODE = "error.authentication.credentials.bad";

	public CheckNotRegisterException(final String code, ThirdRegVo thirdRegVo) {
		super(code);
		this.thirdRegVo = thirdRegVo;
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

	public final ThirdRegVo getThirdRegVo() {
		return thirdRegVo;
	}

}
