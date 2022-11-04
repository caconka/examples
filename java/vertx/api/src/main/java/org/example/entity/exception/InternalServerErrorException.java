package org.example.entity.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class InternalServerErrorException extends BackendException {

	public InternalServerErrorException(String message) {
		super(ErrorCodes.INTERNAL_SERVER_ERROR_CODE, message, HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
	}
}
