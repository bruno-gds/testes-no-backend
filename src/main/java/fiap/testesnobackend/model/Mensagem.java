package fiap.testesnobackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

//    @Builder.Default // PARA JÁ INICIAR COM ESTE VALOR
    @CreationTimestamp // Carregar a data de criação de forma automatica
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSS")
    private LocalDateTime dataCriacaoMensagem = LocalDateTime.now();

    @Builder.Default
    private int gostei = 0;


    @PrePersist
    public void prePersist() {
        var timestamp = LocalDateTime.now();

        this.dataCriacaoMensagem = timestamp;
    }
}
