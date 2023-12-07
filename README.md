# Backend - API REST

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
mvn test -P system-test -Dcucumber.filter.tags=@smoke
```