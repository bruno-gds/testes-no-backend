package fiap.testesnobackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.testesnobackend.exception.MensagemNotFoundException;
import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.service.MensagemService;
import fiap.testesnobackend.utils.MensagemHelper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMvc = MockMvcBuilders.standaloneSetup(mensagemController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                })
                .build();
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
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem))
            ).andExpect(status().isCreated());
            verify(mensagemService, times(1)).registrarMensagem(any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarMensagem_PayloadXML() throws Exception {
            String xmlPayload = "<mensagem><usuario>Ana</usuario><conteudo>Mensagem do Conteudo</conteudo></mensagem>";

            mockMvc.perform(post("/mensagens")
                            .contentType(MediaType.APPLICATION_XML)
                            .content(xmlPayload))
                    .andExpect(status().isUnsupportedMediaType());
            verify(mensagemService, never()).registrarMensagem(any(Mensagem.class));
        }
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagens() throws Exception {
            // ARRANGE
            var id = UUID.fromString("f3a04364-be8a-4682-b467-abde4189574d");
            var mensagem = MensagemHelper.gerarMensagem();

            when(mensagemService.buscarMensagem(any(UUID.class))).thenReturn(mensagem);

            // ACT & ASSERT
            mockMvc.perform(get("/mensagens/{id}", id))
                    .andExpect(status().isOk());
            verify(mensagemService, times(1)).buscarMensagem(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() throws Exception {
            var id = UUID.fromString("16d50898-4321-4eb2-ade3-2bccb6718911");

            when(mensagemService.buscarMensagem(id)).thenThrow(MensagemNotFoundException.class);

            mockMvc.perform(get("/mensagens/{id}", id))
                    .andExpect(status().isBadRequest());
            verify(mensagemService, times(1)).buscarMensagem(id);
        }
    }

    @Nested
    class AlterarMensagem {

        @Test
        void devePermitirAlterarMensagem() throws Exception {
            var id = UUID.fromString("e932d556-c9c9-4636-acda-e0b345635859");
            var mensagem = MensagemHelper.gerarMensagem();
            mensagem.setId(id);

            when(mensagemService.alterarMensagem(id, mensagem))
                    .thenReturn(mensagem);

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
                    .andExpect(status().isAccepted());
            verify(mensagemService, times(1)).alterarMensagem(id, mensagem);
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() throws Exception {
            var id = UUID.fromString("ef827641-526e-4e65-bb49-da0c6b9de56f");
            var mensagem = MensagemHelper.gerarMensagem();
            mensagem.setId(id);
            var conteudoDaExcecao = "Mensagem não encontrada";

            when(mensagemService.alterarMensagem(id, mensagem))
                    .thenThrow(new MensagemNotFoundException(conteudoDaExcecao));

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
//                    .andDo(print()) // Muito bom para realizar Debug
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(conteudoDaExcecao));
            verify(mensagemService, times(1))
                    .alterarMensagem(any(UUID.class), any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() throws Exception {
            var id = UUID.fromString("ef827641-526e-4e65-bb49-da0c6b9de56f");
            var mensagem = MensagemHelper.gerarMensagem();
            mensagem.setId(UUID.fromString("7d861894-14e9-47f4-aad3-f0220d3e687d"));
            var conteudoDaExcecao = "Mensagem atualizada não apresenta o ID correto";

            when(mensagemService.alterarMensagem(id, mensagem))
                    .thenThrow(new MensagemNotFoundException(conteudoDaExcecao));

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
//                    .andDo(print()) // Muito bom para realizar Debug
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(conteudoDaExcecao));
            verify(mensagemService, times(1))
                    .alterarMensagem(any(UUID.class), any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_ApresentaPayloadComXML() throws Exception {
            var id = UUID.fromString("e932d556-c9c9-4636-acda-e0b345635859");
            String xmlPayload = "<mensagem>" +
                    "<id>" + id.toString() + "</id>" +
                    "<usuario>Ana</usuario>" +
                    "<conteudo>Mensagem do Conteudo</conteudo>" +
                    "</mensagem>";

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_XML)
                            .content(xmlPayload))
                    .andExpect(status().isUnsupportedMediaType());
            verify(mensagemService, never()).alterarMensagem(any(UUID.class), any(Mensagem.class));
        }
    }

    @Nested
    class RemoverMensagem {

        @Test
        void devePermitirRemoverMensagem() throws Exception {
            var id = UUID.fromString("8ac25ae6-6483-41d9-b0c8-0c98b0d10981");

            when(mensagemService.removerMensagem(id)).thenReturn(true);

            mockMvc.perform(delete("/mensagens/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Mensagem removida"));
            verify(mensagemService, times(1)).removerMensagem(id);
        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() throws Exception {
            var id = UUID.fromString("c591475a-11e7-468f-bb23-c31e28a00a48");
            var mensagemDaExcecao = "Mensagem não encontrada";

            when(mensagemService.removerMensagem(id))
                    .thenThrow(new MensagemNotFoundException(mensagemDaExcecao));

            mockMvc.perform(delete("/mensagens/{id}", id))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(mensagemDaExcecao));
            verify(mensagemService, times(1)).removerMensagem(id);
        }
    }

    @Nested
    class ListarMensagens {

        @Test
        void devePermitirListarMensagens() throws Exception {
            var mensagem = MensagemHelper.gerarMensagem();
            var page = new PageImpl<>(Collections.singletonList(mensagem));

            when(mensagemService.listarMensagens(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/mensagens")
                            .param("page", "0")
                            .param("size", "10"))
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", not(empty())))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

        @Test
        void devePermitirListarMensagens_QuandoNaoInformadoPaginacao() throws Exception {
            var mensagem = MensagemHelper.gerarMensagem();
            var page = new PageImpl<>(Collections.singletonList(mensagem));

            when(mensagemService.listarMensagens(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/mensagens"))
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", not(empty())))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(object);
    }
}
