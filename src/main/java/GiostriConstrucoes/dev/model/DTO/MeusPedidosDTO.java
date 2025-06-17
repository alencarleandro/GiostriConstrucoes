package GiostriConstrucoes.dev.model.DTO;

import lombok.Builder;

import java.util.List;

@Builder
public record MeusPedidosDTO (String numero,
                              String data,
                              String total,
                              String status,
                              String produtos,
                              String endereco,
                              String pagamento,
                              String cliente,
                              List<StatusDTO> statusSteps){
}
