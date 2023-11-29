package fiap.testesnobackend.controller;

import fiap.testesnobackend.utils.MensagemHelper;
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

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 29/11/2023
 * Project Name: testes-no-backend
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

            when()
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
            Assertions.fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
            Assertions.fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
            Assertions.fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_ApresentaPayloadComXML() {
            Assertions.fail("Teste não implementado");
        }
    }

    @Nested
    class RemoverMensagem {

        @Test
        void devePermitirRemoverMensagem() {
            Assertions.fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
            Assertions.fail("Teste não implementado");
        }
    }

    @Nested
    class ListarMensagens {

        @Test
        void devePermitirListarMensagens() {
            Assertions.fail("Teste não implementado");
        }

        @Test
        void devePermitirListarMensagens_QuandoNaoInformadoPaginacao() {
            Assertions.fail("Teste não implementado");
        }
    }
}
