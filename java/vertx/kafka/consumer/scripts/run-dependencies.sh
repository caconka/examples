#!/bin/bash

docker network create --driver bridge kafka 2>/dev/null || true

PWD="$(pwd)"
KAFKA_COMPOSE="${PWD%/examples*}/examples/java/vertx/kafka/consumer/docker/kafka.yaml"

docker-compose -f "$KAFKA_COMPOSE" up -d
