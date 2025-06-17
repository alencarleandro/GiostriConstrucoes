package GiostriConstrucoes.dev.config;

import java.math.BigDecimal;

public class RelatorioFinanceiroResumo {

    private long totalPedidos;
    private BigDecimal receitaTotal;

    public RelatorioFinanceiroResumo(long totalPedidos, BigDecimal receitaTotal) {
        this.totalPedidos = totalPedidos;
        this.receitaTotal = receitaTotal;
    }

    public long getTotalPedidos() {
        return totalPedidos;
    }

    public BigDecimal getReceitaTotal() {
        return receitaTotal;
    }
}
