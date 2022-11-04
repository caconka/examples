package org.example.entity.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class BadRequestException extends BackendException {

	public BadRequestException(String message) {
		super(ErrorCodes.BAD_REQUEST_EXCEPTION_CODE, message, HttpResponseStatus.BAD_REQUEST.code());
	}

}
