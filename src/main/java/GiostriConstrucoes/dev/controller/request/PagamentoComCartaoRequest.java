package GiostriConstrucoes.dev.controller.request;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public record PagamentoComCartaoRequest(
        String pedidoId,
        String usuarioId,
        String payerEmail,
        String tokenCartao,
        String issuerId,
        int parcelas,
        String metodoPagamento,  // "credit_card" ou "debit_card"
        String bandeira,        // "visa", "master", "elo", etc.
        BigDecimal valorTotal
) {
    // Validação adicional pode ser feita aqui
    public PagamentoComCartaoRequest {
        Objects.requireNonNull(pedidoId, "ID do pedido não pode ser nulo");
        Objects.requireNonNull(payerEmail, "Email não pode ser nulo");
        Objects.requireNonNull(tokenCartao, "Token do cartão não pode ser nulo");

        if (parcelas < 1) {
            throw new IllegalArgumentException("Número de parcelas inválido");
        }

        if (!Set.of("credit_card", "debit_card").contains(metodoPagamento)) {
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total deve ser positivo");
        }
    }
}