package GiostriConstrucoes.dev.model;

import GiostriConstrucoes.dev.model.util.GerenciavelSimples;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "carrinho")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho extends GerenciavelSimples {
    @OneToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemDeCarrinho> itens;

    public double valorTotal(){
        double resposta = 0;

        for(ItemDeCarrinho itemDeCarrinho : itens){
            resposta += itemDeCarrinho.valorTotal();
        }

        return resposta;
    }

}