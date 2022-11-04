package org.example.kafka.handler;

import static org.example.prometheus.Prometheus.CONSUMED;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumerRecord;
import org.example.entity.logger.Log;
import org.example.prometheus.Prometheus;

public class UserUpdatedHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdatedHandler.class);

	private final Prometheus prometheus;

	public UserUpdatedHandler(Prometheus prometheus) {
		this.prometheus = prometheus;
	}

	public void consume(KafkaConsumerRecord<String, JsonObject> userUpdatedRecord) {
		LOGGER.info(
			Log.builder()
				.setMessage("UserUpdated consumed")
				.addMetadata("kafka-key", userUpdatedRecord.key())
				.addMetadata("payload", userUpdatedRecord.value().toString())
				.addMetadata("topic", userUpdatedRecord.topic())
				.build().toJson());

		prometheus.writeMetrics(CONSUMED);
	}

}
