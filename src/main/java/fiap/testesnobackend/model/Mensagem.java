package fiap.testesnobackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 09/11/2023
 * Project Name: testes-no-backend
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {

    @Id
    private UUID id;

    @Column(nullable = false)
    @NotEmpty(message = "O usuário não pode estar vazio")
    private String usuario;

    @Column(nullable = false)
    @NotEmpty(message = "O conteúdo não pode estar vazio")
    private String conteudo;

    @Builder.Default // PARA JÁ INICIAR COM ESTE VALOR
    private LocalDateTime dataCriacaoMensagem = LocalDateTime.now();

    @Builder.Default
    private int gostei = 0;
}
