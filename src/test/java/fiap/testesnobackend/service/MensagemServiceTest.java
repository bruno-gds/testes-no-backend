package fiap.testesnobackend.service;

import fiap.testesnobackend.exception.MensagemNotFoundException;
import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.repository.MensagemRepository;
import fiap.testesnobackend.utils.MensagemHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 14/11/2023
 * Project Name: testes-no-backend
 */

class MensagemServiceTest {

    private MensagemService mensagemService;

    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImpl(mensagemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Test
    void devePermitirRegistrarMensagem() {
        // ARRANGE
        var mensagem = MensagemHelper.gerarMensagem();

        when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(i -> i.getArgument(0));

        // ACT
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        // ASSERT
        assertThat(mensagemRegistrada)
                .isInstanceOf(Mensagem.class)
                .isNotNull();
        assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRegistrada.getUsuario()).isEqualTo(mensagem.getUsuario());
        assertThat(mensagem.getId()).isNotNull();
        verify(mensagemRepository, times(1)).save(any(Mensagem.class));
    }

    @Test
    void devePermitirBuscarMensagens() {
        // ARRANGE
        var id = UUID.fromString("a8bfcb1e-2339-421b-8c15-af7aa09ef672");
        var mensagem = MensagemHelper.gerarMensagem();

        mensagem.setId(id);
        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagem));

        // ACT
        var mensagemRecebida = mensagemService.buscarMensagem(id);

        // ASSERT
        assertThat(mensagemRecebida).isEqualTo(mensagem);
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
        var id = UUID.fromString("b55fda17-8192-4d05-be22-8f66ecb1af54");

        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemService.buscarMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void devePermitirAlterarMensagem() {
        // ARRANGE
        var id = UUID.fromString("282c928d-1d38-4a31-ae00-189516929909");

        var mensagemAntiga = MensagemHelper.gerarMensagem();
        mensagemAntiga.setId(id);

        var mensagemNova = new Mensagem();
        mensagemNova.setId(mensagemAntiga.getId());
        mensagemNova.setUsuario(mensagemAntiga.getUsuario());
        mensagemNova.setConteudo("ABCD 12345");

        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagemAntiga));
        when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(i -> i.getArgument(0));

        // ACT
        var mensagemObtida = mensagemService.alterarMensagem(id, mensagemNova);

        // ASSERT
        assertThat(mensagemObtida).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemObtida.getId()).isEqualTo(mensagemNova.getId());
        assertThat(mensagemObtida.getUsuario()).isEqualTo(mensagemNova.getUsuario());
        assertThat(mensagemObtida.getConteudo()).isEqualTo(mensagemNova.getConteudo());
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, times(1)).save(any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
        // ARRANGE
        var id = UUID.fromString("3325f50d-e5c9-4d6d-980e-a09aa7e8cc5e");

        var mensagem = MensagemHelper.gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagem))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, never()).save(any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
        // ARRANGE
        var id = UUID.fromString("da0b811b-66ef-439b-8de4-318a758efc00");

        var mensagemAntiga = MensagemHelper.gerarMensagem();
        mensagemAntiga.setId(id);

        var mensagemNova = MensagemHelper.gerarMensagem();
        mensagemNova.setId(UUID.randomUUID());
        mensagemNova.setConteudo("ABCD 123");

        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagemAntiga));

        // ACT & ASSERT
        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemNova))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem atualizada não apresenta o ID correto");
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, never()).save(any(Mensagem.class));
    }

    @Test
    void devePermitirRemoverMensagem() {
        // ARRANGE
        var id = UUID.fromString("cb1acf9b-c025-4309-b9d1-f4e240df8810");

        var mensagem = MensagemHelper.gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagem));
        doNothing().when(mensagemRepository).deleteById(id);

        // ACT
        var mensagemFoiRemovida = mensagemService.removerMensagem(id);

        // ASSERT
        assertThat(mensagemFoiRemovida).isTrue();
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
        // ARRANGE
        var id = UUID.fromString("21cf771d-4a30-4915-9b7d-c9cacf2e817c");

        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> mensagemService.removerMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void devePermitirListarMensagens() {
        // ARRANGE
        Page<Mensagem> listaDeMensagens = new PageImpl<>(Arrays.asList(
                MensagemHelper.gerarMensagem(),
                MensagemHelper.gerarMensagem()
        ));

        when(mensagemRepository.listarMensagens(any(Pageable.class))).thenReturn(listaDeMensagens);

        // ACT
        var resultadoObtido = mensagemService.listarMensagens(Pageable.unpaged());

        // ASSERT
        assertThat(resultadoObtido).hasSize(2);
        assertThat(resultadoObtido.getContent())
                .asList()
                .allSatisfy(mensagem -> {
                    assertThat(mensagem)
                            .isNotNull()
                            .isInstanceOf(Mensagem.class);
                });
        verify(mensagemRepository, times(1)).listarMensagens(any(Pageable.class));
    }
}
