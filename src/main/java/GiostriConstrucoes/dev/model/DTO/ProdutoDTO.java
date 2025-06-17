package GiostriConstrucoes.dev.model.DTO;

import GiostriConstrucoes.dev.model.Produto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProdutoDTO {

    private String id;
    @NotBlank(message = "De um nome ao produto.")
    private String nome;
    @NotBlank(message = "Insira um preço.")
    private double preco;
    @NotBlank(message = "Selecione a categoria que o produto mais se encaixa.")
    private String categoria;
    private String descricao;
    private int quantidadeEmEstoque;
    private String url;

    public static ProdutoDTO toProdutoDTO(Produto produto) {
        return ProdutoDTO.builder()
                .id(String.valueOf(produto.getId()))
                .nome(produto.getNome())
                .preco(produto.getPreco())
                .quantidadeEmEstoque(produto.getQuantidadeEmEstoque())
                .categoria(produto.getCategoria())
                .descricao(produto.getDescricao())
                .url(produto.getImgUrl())
                .build();
    }

}
