package org.example.controller;

import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.openapi.RouterBuilder;
import org.example.controller.handler.FailureHandler;
import org.example.controller.handler.HealthCheckHandler;
import org.example.controller.handler.LoggerContextHandler;
import org.example.controller.user.UserRouter;
import org.example.domain.logger.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Router {

	private static final Logger LOGGER = LoggerFactory.getLogger(Router.class);

	private static final String HEALTH_CHECK = "/health";

	private final HealthCheckHandler healthCheckHandler;
	private final FailureHandler failureHandler;
	private final UserRouter userRouter;

	public Router(HealthCheckHandler healthCheckHandler, FailureHandler failureHandler, UserRouter userRouter) {
		this.healthCheckHandler = healthCheckHandler;
		this.failureHandler = failureHandler;
		this.userRouter = userRouter;
	}

	public Single<io.vertx.rxjava3.ext.web.Router> configure() {

		return RouterBuilder.create(Vertx.currentContext().owner(), "openapi.yaml")
			.map(routerBuilder -> {
				routerBuilder.rootHandler(BodyHandler.create());
				routerBuilder.rootHandler(LoggerContextHandler.create());

				return routerBuilder;
			})
			.map(userRouter::configure)
			.map(routerBuilder -> {
				var router = routerBuilder.createRouter();

				router.get(HEALTH_CHECK).handler(healthCheckHandler);
				router.route().failureHandler(failureHandler);

				return router;
			})
			.doOnError(error -> LOGGER.error(Log.builder().setMessage(error.getMessage()).build().toJson()));
	}

}
