package org.example.http;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.openapi.RouterBuilder;
import org.example.entity.logger.Log;
import org.example.http.handler.FailureHandler;
import org.example.http.handler.HealthCheckHandler;
import org.example.http.handler.LoggerContextHandler;
import org.example.http.handler.UserHandler;

public class Router {

	private static final Logger LOGGER = LoggerFactory.getLogger(Router.class);

	private static final String HEALTH_CHECK = "/health";

	private final HealthCheckHandler healthCheckHandler;
	private final FailureHandler failureHandler;
	private final UserHandler userHandler;

	public Router(HealthCheckHandler healthCheckHandler, FailureHandler failureHandler,
		UserHandler userHandler) {

		this.healthCheckHandler = healthCheckHandler;
		this.failureHandler = failureHandler;
		this.userHandler = userHandler;
	}

	public Single<io.vertx.rxjava3.ext.web.Router> configure() {

		return RouterBuilder.create(Vertx.currentContext().owner(), "openapi.yaml")
			.map(routerBuilder -> {

				routerBuilder.rootHandler(BodyHandler.create());
				routerBuilder.rootHandler(LoggerContextHandler.create());

				routerBuilder.operation("createUser")
					.handler(userHandler::createUser);

				routerBuilder.operation("getUserById")
					.handler(userHandler::findUserById);

				routerBuilder.operation("deleteUser")
					.handler(userHandler::deleteUser);

				var router = routerBuilder.createRouter();
				router.get(HEALTH_CHECK).handler(healthCheckHandler);

				router.route().failureHandler(failureHandler::handle);

				return router;
			})
			.doOnError(error -> LOGGER.error(Log.builder().setMessage(error.getMessage()).build().toJson()));
	}

}
