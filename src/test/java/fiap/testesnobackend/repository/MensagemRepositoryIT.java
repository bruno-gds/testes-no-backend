package fiap.testesnobackend.repository;

import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.utils.MensagemHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 10/11/2023
 * Project Name: testes-no-backend
 */

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test") // Habilita o profile de teste
@Transactional
class MensagemRepositoryIT {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Test
    void devePermitirCriarTabela() {
        var totalDeRegistros = mensagemRepository.count();

        assertThat(totalDeRegistros).isPositive();
    }

    @Test
    void devePermitirRegistarMensagem() {
        // ARRANGE
        var id = UUID.randomUUID();
        var mensagem = MensagemHelper.gerarMensagem();

        mensagem.setId(id);

        // ACT
        var mensagemRecebida = mensagemRepository.save(mensagem);

        // ASSERT
        assertThat(mensagemRecebida)
                .isInstanceOf(Mensagem.class)
                .isNotNull();
        assertThat(mensagemRecebida.getId()).isEqualTo(id);
        assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagem.getUsuario());
    }

    @Test
    void devePermitirBuscarMensagens() {
        // ARRANGE
        var id = UUID.fromString("22940b60-002f-466c-871b-6b134d0e288c");

        // ACT
        var mensagemRecebidaOptional = mensagemRepository.findById(id);

        // ASSERT
        assertThat(mensagemRecebidaOptional).isPresent();

        mensagemRecebidaOptional.ifPresent(mensagemRecebida -> {
            assertThat(mensagemRecebida.getId()).isEqualTo(id);
        });
    }

    @Test
    void devePermitirRemoverMensagem() {
        // ARRANGE
        var id = UUID.fromString("59502d64-c3f7-41c3-a74c-8cfcbf0ea893");

        // ACT
        mensagemRepository.deleteById(id);
        var mensagemRecebidaOptional = mensagemRepository.findById(id);

        // ASSERT
        assertThat(mensagemRecebidaOptional).isEmpty();
    }

    @Test
    void devePermitirListarMensagens() {
        // ACT
        var resultadosObtidos = mensagemRepository.findAll();

        // ASSERT
        assertThat(resultadosObtidos).hasSizeGreaterThan(0);
    }
}
