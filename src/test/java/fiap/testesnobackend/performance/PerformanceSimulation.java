package fiap.testesnobackend.performance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 08/12/2023
 * Project Name: testes-no-backend
 */

public class PerformanceSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080")
                    .header("Content-Type", "application/json");

    ActionBuilder adicionarMensagemRequest = http("Adicionar Mensagem")
            .post("/mensagens")
            .body(StringBody("{\"usuario\": \"user\", \"conteudo\": \"Conteudo da mensagem\"}"))
            .check(status().is(201));

    ScenarioBuilder cenarioAdicionarMensagem = scenario("Adicionar Mensagem")
            .exec(adicionarMensagemRequest);

    {
        setUp(
                cenarioAdicionarMensagem.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10).during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10).to(1).during(Duration.ofSeconds(10))
                )
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(50)
                );
    }
}
