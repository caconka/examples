package org.example.domain.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class UnauthorizedException extends BackendException {

	public UnauthorizedException(String message) {
		super(ErrorCodes.UNAUTHORIZED_EXCEPTION_CODE, message, HttpResponseStatus.UNAUTHORIZED.code());
	}

}
