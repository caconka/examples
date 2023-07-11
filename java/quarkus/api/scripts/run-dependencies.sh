#!/bin/bash

podman run --rm -d --name examples-postgres -e POSTGRES_USER=test \
	-e POSTGRES_PASSWORD=test -e POSTGRES_DB=examples \
	-p 5432:5432 postgres:15.3
