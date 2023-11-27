package fiap.testesnobackend.exception;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 14/11/2023
 * Project Name: testes-no-backend
 */

public class MensagemNotFoundException extends RuntimeException {

    public MensagemNotFoundException(String mensagem) {
        super(mensagem);
    }
}
