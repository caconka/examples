package org.example.entity.exception;

public abstract class BackendException extends RuntimeException {

	private final Error error;
	private final int httpCode;

	protected BackendException(String code, String title, String ref, String details, int httpCode) {
		super(details);
		error = new Error(code, title, ref, details);
		this.httpCode = httpCode;
	}

	protected BackendException(ErrorCodes errorCode, String details, int httpCode) {
		this(errorCode.getCode(), errorCode.getTitle(), errorCode.getRef(), details, httpCode);
	}

	public Error getError() {
		return error;
	}

	public int getHttpCode() {
		return httpCode;
	}

}
