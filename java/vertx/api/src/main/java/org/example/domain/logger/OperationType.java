package org.example.domain.logger;

public enum OperationType {
	INPUT("input"),
	OUTPUT("output"),
	REQUEST("request"),
	RESPONSE("response"),
	REDIS_REQUEST("redis-request"),
	REDIS_RESPONSE("redis-response"),
	DB_QUERY("database_query"),
	DB_RESULT("database_result");

	private final String name;

	OperationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
