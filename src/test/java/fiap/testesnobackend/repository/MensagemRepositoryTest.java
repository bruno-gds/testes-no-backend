package fiap.testesnobackend.repository;

import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.utils.MensagemHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 09/11/2023
 * Project Name: testes-no-backend
 */

public class MensagemRepositoryTest {

    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRegistarMensagem() {
        // ARRANGE
        var mensagem = MensagemHelper.gerarMensagem();

//        when(mensagemRepository.save(mensagem)).thenReturn(mensagem);
        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        // ACT
        var mensagemRegistrada = mensagemRepository.save(mensagem);

        // ASSERT
        assertThat(mensagemRegistrada).isNotNull().isEqualTo(mensagem);
//        verify(mensagemRepository, times(1)).save(mensagem);
        verify(mensagemRepository, times(1)).save(any(Mensagem.class));
    }

    @Test
    void devePermitirBuscarMensagens() {
        // ARRANGE
        var id = UUID.randomUUID();
        var mensagem = MensagemHelper.gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));

        // ACT
        var mensagemRecebidaOptional = mensagemRepository.findById(id);

        // ASSERT
        assertThat(mensagemRecebidaOptional).isPresent().containsSame(mensagem);
        mensagemRecebidaOptional.ifPresent(mensagemRecebida -> {
            assertThat(mensagemRecebida.getId()).isEqualTo(mensagem.getId());
            assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
        });
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void devePermitirRemoverMensagem() {
        // ARRANGE
        var id = UUID.randomUUID();

        doNothing().when(mensagemRepository).deleteById(any(UUID.class));

        // ACT
        mensagemRepository.deleteById(id);

        // ASSERT
        verify(mensagemRepository, times(1)).deleteById(id);
    }

    @Test
    void devePermitirListarMensagens() {
        // ARRANGE
        var mensagem1 = MensagemHelper.gerarMensagem();
        var mensagem2 = MensagemHelper.gerarMensagem();
        var listaMensagens = Arrays.asList(mensagem1, mensagem2);

        when(mensagemRepository.findAll()).thenReturn(listaMensagens);

        // ACT
        var mensagensRecebidas = mensagemRepository.findAll();

        // ASSERT
        assertThat(mensagensRecebidas)
                .hasSize(2)
                .containsExactlyInAnyOrder(mensagem1, mensagem2);
        verify(mensagemRepository, times(1)).findAll();
    }
}
