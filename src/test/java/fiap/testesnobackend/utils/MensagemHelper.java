package fiap.testesnobackend.utils;

import fiap.testesnobackend.model.Mensagem;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 22/11/2023
 * Project Name: testes-no-backend
 */

public abstract class MensagemHelper {

    public static Mensagem gerarMensagem() {
        return Mensagem.builder()
                .usuario("Bruno")
                .conteudo("Conteúdo da Mensagem")
                .build();
    }
}
