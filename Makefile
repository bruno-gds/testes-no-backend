build:
	mvn compile

unit-test:
	@echo "Executando testes unitários..."
	@mvn test

integration-test:
	mvn test -P integration-test

system-test:
	mvn test -P system-test

performance-test:
	mvn gatling:test -P performance-test

test: unit-test integration-test

start-app:
	mvn spring-boot:start

package:
	mvn package

docker-build:
	docker build -t testes-no-backend:dev -f ./Dockerfile .

docker-start:
	cd docker && docker-compose up -d

docker-stop:
	cd docker && docker-compose down