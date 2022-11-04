package org.example.verticle;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.rxjava3.core.AbstractVerticle;
import org.example.IoC;
import org.example.entity.logger.Log;

public class KafkaConsumerVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerVerticle.class.getName());

	@Override
	public Completable rxStart() {
		var ioc = IoC.getInstance();

		return ioc.kafkaConsumer
			.handler(ioc.userUpdatedHandler::consume)
			.subscribe(ioc.config.getKafkaUserUpdatedTopic())
			.doOnError(err -> {
				err.printStackTrace();
				LOGGER.error(Log.builder().setMessage(err.getMessage()).build().toJson());
			});
	}

}
