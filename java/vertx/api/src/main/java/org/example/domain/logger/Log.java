package org.example.domain.logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.json.Json;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Log {

	private final String time;
	private final String message;
	private final Map<String, String> metadata;
	@JsonInclude(Include.NON_NULL)
	private final String traceId;

	private Log(Builder builder) {
		time = builder.time;
		message = builder.message;
		metadata = builder.metadata;
		traceId = builder.traceId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public String toJson() {
		return Json.encode(this);
	}

	public String getTime() {
		return time;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public String getTraceId() {
		return traceId;
	}

	public static final class Builder {

		private final Map<String, String> metadata = new HashMap<>();
		private LogContext context;
		private String time;
		private String message;
		private String traceId;

		public Log build() {
			var dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			time = dateFormat.format(new Date());

			if (context != null) {
				traceId = context.getTraceId();
			}

			return new Log(this);
		}

		public Builder setContext(LogContext ctx) {
			context = ctx;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder addMetadata(String key, String value) {
			this.metadata.put(key, value);
			return this;
		}
	}

}
