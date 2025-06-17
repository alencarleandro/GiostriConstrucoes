package GiostriConstrucoes.dev.model.util;

import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.model.Produto;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
@EqualsAndHashCode(of = {"pedido", "produto"})
@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class itemPedidoPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;


}
