package org.example.entity.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class InvalidConfigException extends BackendException {

	public InvalidConfigException(String message) {
		super(ErrorCodes.INVALID_CONFIG_EXCEPTION_CODE, message, HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
	}
}
