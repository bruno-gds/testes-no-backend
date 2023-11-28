package fiap.testesnobackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.service.MensagemService;
import fiap.testesnobackend.utils.MensagemHelper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 28/11/2023
 * Project Name: testes-no-backend
 */


public class MensagemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MensagemService mensagemService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        MensagemController mensagemController = new MensagemController(mensagemService);
        mockMvc = MockMvcBuilders.standaloneSetup(mensagemController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Nested
    class RegistrarMensagem {

        @Test
        void devePermitirRegistrarMensagem() throws Exception {
            // ARRANGE
            var mensagem = MensagemHelper.gerarMensagem();

            when(mensagemService.registrarMensagem(any(Mensagem.class)))
                    .thenAnswer( i -> i.getArgument(0) );

            // ACT & ASSERT
            mockMvc.perform(
                    post("/mensagens")
                    .content(asJsonString(mensagem))
            ).andExpect(status().isCreated());
            verify(mensagemService, times(1)).registrarMensagem(any(Mensagem.class));
        }
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(object);
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagens() {
            Assertions.fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
            Assertions.fail("Teste não implementado");
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
    }
}
