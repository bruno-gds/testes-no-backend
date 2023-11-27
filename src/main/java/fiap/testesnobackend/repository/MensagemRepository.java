package fiap.testesnobackend.repository;

import fiap.testesnobackend.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 09/11/2023
 * Project Name: testes-no-backend
 */

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {

    @Query("SELECT m FROM Mensagem m ORDER BY m.dataCriacaoMensagem DESC")
    Page<Mensagem> listarMensagens(Pageable pageable);
}
