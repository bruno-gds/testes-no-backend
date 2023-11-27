package fiap.testesnobackend.service;

import fiap.testesnobackend.exception.MensagemNotFoundException;
import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.repository.MensagemRepository;
import fiap.testesnobackend.utils.MensagemHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 22/11/2023
 * Project Name: testes-no-backend
 */

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class MensagemServiceIT {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private MensagemService mensagemService;


    @Test
    void devePermitirRegistrarMensagem() {
        var mensagem = MensagemHelper.gerarMensagem();
        var resultadoObtido = mensagemService.registrarMensagem(mensagem);

        assertThat(resultadoObtido)
                .isNotNull()
                .isInstanceOf(Mensagem.class);
        assertThat(resultadoObtido.getId()).isNotNull();
        assertThat(resultadoObtido.getDataCriacaoMensagem()).isNotNull();
        assertThat(resultadoObtido.getGostei()).isZero();
    }

    @Test
    void devePermitirBuscarMensagens() {
        var id = UUID.fromString("22940b60-002f-466c-871b-6b134d0e288c");
        var resultadoObtido = mensagemService.buscarMensagem(id);

        assertThat(resultadoObtido)
                .isNotNull()
                .isInstanceOf(Mensagem.class);
        assertThat(resultadoObtido.getId())
                .isNotNull()
                .isEqualTo(id);
        assertThat(resultadoObtido.getUsuario())
                .isNotNull()
                .isEqualTo("Adam");
        assertThat(resultadoObtido.getConteudo())
                .isNotNull()
                .isEqualTo("Conteudo da mensagem 01");
        assertThat(resultadoObtido.getDataCriacaoMensagem()).isNotNull();
        assertThat(resultadoObtido.getGostei()).isZero();
    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
        var id = UUID.fromString("b4b405b2-23f0-4691-bc7a-8f951d043550");

        assertThatThrownBy(() -> mensagemService.buscarMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
    }

    @Test
    void devePermitirAlterarMensagem() {
        var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea893");

        var mensagemAtualizada = MensagemHelper.gerarMensagem();
        mensagemAtualizada.setId(id);

        var resultadoObtido = mensagemService.alterarMensagem(id, mensagemAtualizada);

        assertThat(resultadoObtido.getId()).isEqualTo(id);
        assertThat(resultadoObtido.getConteudo()).isEqualTo(mensagemAtualizada.getConteudo());

        assertThat(resultadoObtido.getUsuario()).isNotEqualTo(mensagemAtualizada.getUsuario());
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
        var id = UUID.fromString("12e1d2be-a7cd-4fb3-b308-b6b4c4d90180");

        var mensagemAtualizada = MensagemHelper.gerarMensagem();
        mensagemAtualizada.setId(id);

        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemAtualizada))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
        var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea893");

        var mensagemAtualizada = MensagemHelper.gerarMensagem();
        mensagemAtualizada.setId(UUID.fromString("b773c133-afe9-4c4b-8f7a-353cbeadfdef"));

        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemAtualizada))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem atualizada não apresenta o ID correto");
    }

    @Test
    void devePermitirRemoverMensagem() {
        var id = UUID.fromString("97e1b03e-e0e0-4c2b-bca8-840e2110a385");

        var resultadoObtido = mensagemService.removerMensagem(id);

        assertThat(resultadoObtido).isTrue();
    }

    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
        var id = UUID.fromString("30d8848a-98e0-4c5f-9258-9ac4fecd69e2");

        assertThatThrownBy(() -> mensagemService.removerMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
    }

    @Test
    void devePermitirListarMensagens() {
        Page<Mensagem> listaDeMensagensObtida = mensagemService.listarMensagens(Pageable.unpaged());

        assertThat(listaDeMensagensObtida.getContent())
                .hasSize(3)
                .asList()
                .allSatisfy(mensagem -> assertThat(mensagem).isNotNull());
    }
}
