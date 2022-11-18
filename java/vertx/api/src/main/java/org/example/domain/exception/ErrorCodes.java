package org.example.domain.exception;

public enum ErrorCodes {

	INTERNAL_SERVER_ERROR_CODE("001", "INTERNAL_SERVER_ERROR", "http://test.test"),
	INVALID_CONFIG_EXCEPTION_CODE("002", "INVALID_CONFIG", "http://test.test"),
	BAD_REQUEST_EXCEPTION_CODE("003", "BAD_REQUEST", "http://test.test"),
	UNAUTHORIZED_EXCEPTION_CODE("004", "UNAUTHORIZED", "http://test.test"),
	RESOURCE_NOT_FOUND("005", "NOT_FOUND", "http://test.test");

	private final String code;
	private final String title;
	private final String ref;

	ErrorCodes(String code, String title, String ref) {
		this.code = code;
		this.title = title;
		this.ref = ref;
	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public String getRef() {
		return ref;
	}
}
