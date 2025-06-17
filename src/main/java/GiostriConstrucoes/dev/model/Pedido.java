package GiostriConstrucoes.dev.model;

import GiostriConstrucoes.dev.model.enums.EPedidoStatus;
import GiostriConstrucoes.dev.model.util.GerenciavelSimples;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido extends GerenciavelSimples {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "dataDeLancamento")
    private Instant dataDeLancamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "pedidoStatus")
    private EPedidoStatus pedidoStatus;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @Column(name = "valorEntrega")
    private double valorEntrega;
    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemDePedido> itens = new ArrayList<>();

    public double valorTotal(){
        double resposta = 0;

        for(ItemDePedido itemDePedido : itens){
            resposta += itemDePedido.valorTotal();
        }

        return resposta;
    }

}

