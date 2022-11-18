package org.example.domain.exception;

public class Error {

	private final String code;
	private final String title;
	private final String ref;
	private final String details;

	public Error(String code, String title, String ref, String details) {
		this.code = code;
		this.title = title;
		this.ref = ref;
		this.details = details;
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

	public String getDetails() {
		return details;
	}
}
