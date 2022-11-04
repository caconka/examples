package org.example;

import static java.util.Objects.isNull;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import java.util.HashMap;
import org.example.config.Config;
import org.example.http.handler.HealthCheckHandler;
import org.example.kafka.handler.UserUpdatedHandler;
import org.example.prometheus.Prometheus;

public class IoC {

	private static IoC instance = null;

	public final Config config;
	public final HealthCheckHandler healthCheckHandler;
	public final UserUpdatedHandler userUpdatedHandler;
	public final KafkaConsumer<String, JsonObject> kafkaConsumer;

	private IoC() {
		config = Config.getInstance();
		healthCheckHandler = new HealthCheckHandler();
		userUpdatedHandler = new UserUpdatedHandler(new Prometheus());
		kafkaConsumer = createKafkaConsumer();
	}

	public static synchronized IoC getInstance() {
		if (isNull(instance)) {
			instance = new IoC();
		}

		return instance;
	}

	private KafkaConsumer<String, JsonObject> createKafkaConsumer() {
		var kafkaConfig = new HashMap<String, String>();
		kafkaConfig.put("bootstrap.servers", config.getKafkaServer());
		kafkaConfig.put("key.deserializer", config.getKafkaKeyDeserializer());
		kafkaConfig.put("value.deserializer", config.getKafkaValueDeserializer());
		kafkaConfig.put("group.id", config.getKafkaConsumerGroupId());
		kafkaConfig.put("auto.offset.reset", config.getKafkaOffsetReset());
		kafkaConfig.put("enable.auto.commit", config.getKafkaAutoCommit());

		return KafkaConsumer.create(Vertx.currentContext().owner(), kafkaConfig);
	}

}
