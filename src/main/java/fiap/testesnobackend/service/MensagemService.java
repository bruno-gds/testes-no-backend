package fiap.testesnobackend.service;

import fiap.testesnobackend.model.Mensagem;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 14/11/2023
 * Project Name: testes-no-backend
 */

public interface MensagemService {
    Mensagem registrarMensagem(Mensagem mensagem);
    Mensagem buscarMensagem(UUID id);
    Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada);
    boolean removerMensagem(UUID id);
    Page<Mensagem> listarMensagens(Pageable pageable);
}
