package GiostriConstrucoes.dev.model;

import GiostriConstrucoes.dev.model.util.GerenciavelSimples;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ItemDePedido")
public class ItemDePedido extends GerenciavelSimples {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Column(name = "quantidade")
    private int quantidade;

    public double valorTotal(){
        return quantidade * produto.getPreco();
    }

}
