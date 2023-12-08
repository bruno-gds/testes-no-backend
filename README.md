# Testes no Backend - API REST

## Execução dos testes

- Testes unitários:
```sh
mvn test
```

- Testes integrados:
```sh
mvn test -P integration-test
```

- Testes de sistema:
```sh
mvn test -P system-test
```
```sh
mvn test -P system-test -Dcucumber.filter.tags=@smoke
```

## Lib - Allure

- Instalação:
```sh
npm install -g allure-commandline
```

- Execução:
```sh
allure serve target/allure-results
```