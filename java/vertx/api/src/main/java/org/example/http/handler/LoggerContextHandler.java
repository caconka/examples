package org.example.http.handler;

import io.vertx.core.Handler;
import io.vertx.rxjava3.ext.web.RoutingContext;
import org.example.http.adapter.LogContextAdapter;

public class LoggerContextHandler implements Handler<RoutingContext> {

	public static final String LOG_CONTEXT = "log_context";

	public static LoggerContextHandler create() {
		return new LoggerContextHandler();
	}

	@Override
	public void handle(RoutingContext routingCtx) {
		if (routingCtx.get(LOG_CONTEXT) == null) {
			routingCtx.put(LOG_CONTEXT, LogContextAdapter.from(routingCtx));
		}

		routingCtx.next();
	}
}
