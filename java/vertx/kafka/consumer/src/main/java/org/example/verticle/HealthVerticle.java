package org.example.verticle;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;
import org.example.IoC;
import org.example.entity.logger.Log;

public class HealthVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthVerticle.class);

	private static final String HEALTH_CHECK = "/health";

	@Override
	public Completable rxStart() {
		var ioc = IoC.getInstance();
		var httpHost = ioc.config.getHost();
		var httpPort = ioc.config.getPort();

		var baseRouter = Router.router(vertx);
		baseRouter.get(HEALTH_CHECK).handler(ioc.healthCheckHandler);

		return vertx.createHttpServer().requestHandler(baseRouter).rxListen(httpPort, httpHost)
			.doOnError(failure -> {
				LOGGER.error(
					Log.builder().setMessage("Error trying to start server: " + failure.getMessage()).build().toJson());
				System.exit(1);
			})
			.doOnSuccess(
				httpServer ->
					LOGGER.info(
						Log.builder().setMessage("HTTP server started on http://" + httpHost + ":" + httpPort).build().toJson()))
			.ignoreElement();
	}
}
