package GiostriConstrucoes.dev.model.DTO;

import lombok.Builder;

@Builder
public record CarregarPedidoDTO (String id,
                                 String cliente,
                                 String data,
                                 String status,
                                 String total) {
}
