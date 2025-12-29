# spring-cassandra

# Local testing

`cd src/test/resources/docker/`

`docker compose -f docker-compose.yml up -d`

`docker exec -it cassandra cqlsh localhost 9042 local.cql`

# Run cql query

`docker exec -it cassandra cqlsh`

# Sample payload

`{
    "userId": "59145e4c-20da-4bf2-88fc-c8ebd418e9c3",
    "timestamp": 1766939607091,
    "eventId":"ORDER_SHIPPED",
    "items": "[{\"skuId\":\"981371\", \"price\": 1866.00}]"
}`