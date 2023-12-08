package fiap.testesnobackend.controller;

import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.utils.MensagemHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 29/11/2023
 * Project Name: testes-no-backend
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Habilita o profile de teste
@AutoConfigureTestDatabase
public class MensagemControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Nested
    class RegistrarMensagem {

        @Test
        void devePermitirRegistrarMensagem() {
            var mensagem = MensagemHelper.gerarMensagem();

            given()
                        .filter(new AllureRestAssured())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(mensagem)
//                        .log().all()
                    .when()
                        .post("/mensagens")
                    .then()
//                        .log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarMensagem_PayloadXML() {
            String xmlPayload = "<mensagem>" +
                    "<usuario>Ana</usuario>" +
                    "<conteudo>Mensagem do Conteudo</conteudo>" +
                    "</mensagem>";

            given()
                        .filter(new AllureRestAssured())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(xmlPayload)
//                        .log().all()
                    .when()
                        .post("/mensagens")
                    .then()
//                        .log().all()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
//                        .body("$", hasKey("timestamp"))
//                        .body("$", hasKey("status"))
//                        .body("$", hasKey("error"))
//                        .body("$", hasKey("path"))
                        .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                        .body("error", equalTo("Bad Request"))
                        .body("path", equalTo("/mensagens"));
        }
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagens() {
            var id = "22940b60-002f-466c-871b-6b134d0e288c";

            given()
                    .filter(new AllureRestAssured())
            .when()
                    .get("/mensagens/{id}", id)
            .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
            var id = "22940b60-002f-466c-871b-6b134d0e288";

            when()
                    .get("/mensagens/{id}", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class AlterarMensagem {

        @Test
        void devePermitirAlterarMensagem() {
            var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea893");
            var mensagem = Mensagem.builder()
                    .id(id)
                    .usuario("Eva")
                    .conteudo("Conteudo da mensagem")
                    .build();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mensagem)
            .when()
                    .put("/mensagens/{id}", id)
            .then()
                    .statusCode(HttpStatus.ACCEPTED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
            var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea89");
            var mensagem = Mensagem.builder()
                    .id(id)
                    .usuario("Eva")
                    .conteudo("Conteudo da mensagem")
                    .build();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mensagem)
            .when()
                    .put("/mensagens/{id}", id)
            .then()
//                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(equalTo("Mensagem não encontrada"));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
            var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea893");
            var mensagem = Mensagem.builder()
                    .id(UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea89"))
                    .usuario("Eva")
                    .conteudo("Conteudo da mensagem")
                    .build();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mensagem)
            .when()
                    .put("/mensagens/{id}", id)
            .then()
//                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(equalTo("Mensagem atualizada não apresenta o ID correto"));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_ApresentaPayloadComXML() {
            var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea893");
            String xmlPayload = "<mensagem>" +
                    "<id>59502d64-c3f7-41c3-a74c-8cfcbf0ea893</id>" +
                    "<usuario>Ana</usuario>" +
                    "<conteudo>Mensagem do Conteudo</conteudo>" +
                    "</mensagem>";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(xmlPayload)
            .when()
                    .put("/mensagens/{id}", id)
            .then()
//                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Bad Request"))
                    .body("path", equalTo("/mensagens/59502d64-c3f7-41c3-a74c-8cfcbf0ea893"))
                    .body("path", containsString("/mensagens"));
        }
    }

    @Nested
    class RemoverMensagem {

        @Test
        void devePermitirRemoverMensagem() {
            var id = UUID.fromString("97e1b03e-e0e0-4c2b-bca8-840e2110a385");

            when()
                    .delete("/mensagens/{id}", id)
            .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(equalTo("Mensagem removida"));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
            var id = UUID.fromString("97e1b03e-e0e0-4c2b-bca8-840e2110a38");

            when()
                    .delete("/mensagens/{id}", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(equalTo("Mensagem não encontrada"));
        }
    }

    @Nested
    class ListarMensagens {

        @Test
        void devePermitirListarMensagens() {
            given()
                    .queryParam("page", 0)
                    .queryParam("size", 10)
            .when()
                    .get("/mensagens")
            .then()
//                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/mensagem.page.schema.json"));
        }

        @Test
        void devePermitirListarMensagens_QuandoNaoInformadoPaginacao() {
            when()
                    .get("/mensagens")
            .then()
//                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/mensagem.page.schema.json"));
        }
    }
}
