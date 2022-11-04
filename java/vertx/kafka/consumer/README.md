# Vertx kafka consumer example

## How to test

1. Run dependencies in detached mode:
```
./scripts/run-kafka.sh
```

2. If you want to show the dependency logs run:
```
docker-compose -f docker/kafka.yaml logs -f
```

3. Run consumer:
```
mvn clean package && java -jar target/*-fat.jar
```

4. Produce UserUpdated kafka event with the [KafkaProducerTester](src/test/java/org/example/kafka/KafkaProducerTester.java)
