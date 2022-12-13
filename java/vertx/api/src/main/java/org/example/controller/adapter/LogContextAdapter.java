package org.example.controller.adapter;

import io.vertx.rxjava3.ext.web.RoutingContext;
import org.example.domain.logger.LogContext;

public class LogContextAdapter {

	public static final String TRACE_ID = "X-Trace-ID";

	private LogContextAdapter() {
	}

	public static LogContext from(RoutingContext routingContext) {
		var ctxBuilder = LogContext.builder();

		var traceId = routingContext.request().getHeader(TRACE_ID);
		if (traceId != null && !traceId.isEmpty()) {
			ctxBuilder.setTraceId(traceId);
		}

		return ctxBuilder.build();
	}

}
