#!/bin/bash

curl -X POST http://localhost:9999/customers \
	-H 'Content-Type: application/json' \
	-d '{
		"name": "Lope",
		"last_name": "de Vega",
		"email": "lopedevega@test.com"
	}'
