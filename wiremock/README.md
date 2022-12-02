# Wiremock

## How to test

Run docker-compose:

```bash
docker-compose -f docker/docker-compose.yaml up -d
```

Send a request:

```bash
./scripts/customer-retrieve-by-id.sh
./scripts/customer-create.sh
```
