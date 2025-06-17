package GiostriConstrucoes.dev.model.DTO;

import GiostriConstrucoes.dev.model.ItemDeCarrinho;
import lombok.Builder;

@Builder
public record ItemDeCarrinhoDTO(String id,
                                String nome,
                                String preco,
                                String imagem,
                                String quantidade,
                                String estoque) {

    public static ItemDeCarrinhoDTO converter(ItemDeCarrinho itemDeCarrinho) {
        return ItemDeCarrinhoDTO.builder()
                .id(String.valueOf(itemDeCarrinho.getId()))
                .imagem(itemDeCarrinho.getProduto().getImgUrl())
                .nome(itemDeCarrinho.getProduto().getNome())
                .preco(String.valueOf(itemDeCarrinho.getProduto().getPreco()))
                .quantidade(String.valueOf(itemDeCarrinho.getQuantidade()))
                .estoque(String.valueOf(itemDeCarrinho.getProduto().getQuantidadeEmEstoque()))
                .build();
    }

}
