package org.example.entity.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class NotFoundException extends BackendException {

	public NotFoundException(String message) {
		super(ErrorCodes.RESOURCE_NOT_FOUND, message, HttpResponseStatus.NOT_FOUND.code());
	}

}
