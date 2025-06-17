package GiostriConstrucoes.dev.model;

import GiostriConstrucoes.dev.model.util.GerenciavelSimples;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "endereco")
@Entity(name = "endereco")
@EqualsAndHashCode(of = "id")
public class Endereco extends GerenciavelSimples {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "cep", nullable = false)
    private String cep;
    @Column(name = "logradouro", nullable = false)
    private String logradouro;
    @Column(name = "numero", nullable = false)
    private String numero;
    @Column(name = "complemento", nullable = false)
    private String complemento;
    @Column(name = "bairro")
    private String bairro;
    @Column(name = "cidade", nullable = false)
    private String cidade;
    @Column(name = "uf", nullable = false)
    private String uf;

}
