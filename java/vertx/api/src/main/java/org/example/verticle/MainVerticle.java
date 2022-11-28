package org.example.verticle;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;
import java.util.Arrays;
import org.example.IoC;
import org.example.domain.logger.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public Completable rxStart() {
		vertx.exceptionHandler(err ->
			LOGGER.info(
				Log.builder()
					.setMessage(err.getMessage() + " " + err.getCause() + " " + Arrays.toString(err.getStackTrace())
						+ " " + err.getLocalizedMessage())
					.build().toJson()));

		return IoC.getInstance().router.configure()
			.flatMap(router ->
				vertx.createHttpServer()
					.requestHandler(router)
					.rxListen(IoC.getInstance().config.getPort()))
			.doOnSuccess(
				httpServer ->
					LOGGER.info(
						Log.builder().setMessage("HTTP server started on port " + httpServer.actualPort()).build().toJson()))
			.ignoreElement();
	}

}
