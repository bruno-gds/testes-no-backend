package fiap.testesnobackend.service;

import fiap.testesnobackend.exception.MensagemNotFoundException;
import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 14/11/2023
 * Project Name: testes-no-backend
 */

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService {

    private final MensagemRepository mensagemRepository;

    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());

        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return mensagemRepository.findById(id)
                .orElseThrow(() -> new MensagemNotFoundException("Mensagem não encontrada"));
    }

    @Override
    public Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada) {
        var mensagem = buscarMensagem(id);

        if (!mensagem.getId().equals(mensagemAtualizada.getId())) {
            throw new MensagemNotFoundException("Mensagem atualizada não apresenta o ID correto");
        }

        mensagem.setConteudo(mensagemAtualizada.getConteudo());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public boolean removerMensagem(UUID id) {
        buscarMensagem(id);
        mensagemRepository.deleteById(id);

        return true;
    }

    @Override
    public Page<Mensagem> listarMensagens(Pageable pageable) {
        return mensagemRepository.listarMensagens(pageable);
    }
}
