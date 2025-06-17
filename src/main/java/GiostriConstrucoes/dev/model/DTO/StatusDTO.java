package GiostriConstrucoes.dev.model.DTO;

import lombok.Builder;

@Builder
public record StatusDTO (String status,
                         String texto,
                         String data,
                         boolean concluido) {
}
