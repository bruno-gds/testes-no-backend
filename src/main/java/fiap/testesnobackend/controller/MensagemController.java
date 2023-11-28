package fiap.testesnobackend.controller;

import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 28/11/2023
 * Project Name: testes-no-backend
 */

@RestController
@RequestMapping("mensagens")
@RequiredArgsConstructor
public class MensagemController {

    /**
     * Injetando o serviço de Mensagem.
     */
    private final MensagemService mensagemService;


    @PostMapping()
    public ResponseEntity<Mensagem> registrarMensagem(Mensagem mensagem) {
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);
        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }
}
