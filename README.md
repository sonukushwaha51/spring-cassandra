# spring-cassandra

# Local testing

`cd src/test/resources/docker/`

`docker compose -f docker-compose.yml up -d`

`docker exec -it cassandra cqlsh localhost 9042 local.cql`