package org.example.kafka;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import java.util.HashMap;

public class KafkaProducerTester {

	public static void main(String[] args) {
		var config = new HashMap<String, String>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
		config.put("acks", "1");

		KafkaProducer<String, JsonObject> producer = KafkaProducer.create(Vertx.vertx(), config);

		var userUpdatedJson = new JsonObject()
			.put("id", "12345")
			.put("name", "John");

		KafkaProducerRecord<String, JsonObject> userUpdated = KafkaProducerRecord.create("user-updated", userUpdatedJson);

		producer
			.rxSend(userUpdated)
			.doOnError(Throwable::printStackTrace)
			.subscribe(
				recordMetadata -> {
					System.out.println("Successfully produced message to topic: " + recordMetadata.getTopic());
					System.exit(0);
				},
				throwable -> {
					System.err.println("Error producing message: " + throwable.getMessage());
					System.exit(1);
				});
	}

}
