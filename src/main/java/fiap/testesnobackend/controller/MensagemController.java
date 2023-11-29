package fiap.testesnobackend.controller;

import fiap.testesnobackend.exception.MensagemNotFoundException;
import fiap.testesnobackend.model.Mensagem;
import fiap.testesnobackend.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds)
 * Email: brunog.damascena@gmail.com
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


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mensagem> registrarMensagem(
            @RequestBody Mensagem mensagem) {
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> buscarMensagem(@PathVariable String id) {
        var uuid = UUID.fromString(id);

        try {
            var mensagemEncontrada = mensagemService.buscarMensagem(uuid);
            return new ResponseEntity<>(mensagemEncontrada, HttpStatus.OK);
        } catch (MensagemNotFoundException mensagemNotFoundException) {
            return new ResponseEntity<>("ID Inválido", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Mensagem>> listarMensagens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var mensagens = mensagemService.listarMensagens(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(mensagens);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> alterarMensagem(
            @PathVariable String id,
            @RequestBody Mensagem mensagem) {
        var uuid = UUID.fromString(id);

        try {
            var mensagemAtualizada = mensagemService.alterarMensagem(uuid, mensagem);
            return new ResponseEntity<>(mensagemAtualizada, HttpStatus.ACCEPTED);
        } catch (MensagemNotFoundException mensagemNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mensagemNotFoundException.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletarMensagem(@PathVariable String id) {
        var uuid = UUID.fromString(id);

        try {
            mensagemService.removerMensagem(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("Mensagem removida");
        } catch (MensagemNotFoundException mensagemNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mensagemNotFoundException.getMessage());
        }
    }
}
