package GiostriConstrucoes.dev.config;

import lombok.Builder;

@Builder
public record JwtUserData(Long id, String email, String nome, boolean isAdmin) {
}
