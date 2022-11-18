package org.example.domain.logger;

public class LogContext {

	private final String traceId;
	private final String correlationId;

	public LogContext(Builder builder) {
		traceId = builder.traceId;
		correlationId = builder.correlationId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public String getTraceId() {
		return traceId;
	}

	public static class Builder {

		private String traceId;
		private String correlationId;

		public LogContext build() {
			return new LogContext(this);
		}

		public Builder setTraceId(String traceId) {
			this.traceId = traceId;
			return this;
		}

		public Builder setCorrelationId(String correlationId) {
			this.correlationId = correlationId;
			return this;
		}
	}
}
