package GiostriConstrucoes.dev.controller.request;

import lombok.Builder;

@Builder
public record FinalizarCarrinhoRequest (String usuarioId,
                                       String taxa,
                                       String descricao){}
