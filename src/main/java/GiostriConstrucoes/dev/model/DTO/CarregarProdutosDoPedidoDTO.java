package GiostriConstrucoes.dev.model.DTO;

import lombok.Builder;

@Builder
public record CarregarProdutosDoPedidoDTO (String nome,
                                           String quantidade,
                                           String preco){}
