#!/bin/bash

script=$(realpath -s "$0")
scriptPath=$(dirname "$script")
wiremockPath="${scriptPath/scripts/wiremock}"

docker cp ./wiremock/mappings/. wiremock:/home/wiremock/mappings
docker cp ./wiremock/__files/. wiremock:/home/wiremock/__files
echo "Files copied successfully!"

docker restart wiremock
echo "Restart completed!"
