package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.controller.request.PagamentoComCartaoRequest;
import GiostriConstrucoes.dev.controller.request.PagamentoComPixRequest;
import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.model.Usuario;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MercadoPagoService {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;

    public Payment pagarComPix(PagamentoComPixRequest pagamento) throws MPApiException, MPException {

        Pedido pedido = pedidoService.getPedidoById(pagamento.pedidoId());
        Usuario usuario = usuarioService.findById(pagamento.usuarioId());

        System.out.println("Valor total pedido: " + pedido.valorTotal());
        System.out.println("Valor entrega: " + pedido.getValorEntrega());

        BigDecimal valorTotal = BigDecimal.valueOf(pedido.valorTotal());
        BigDecimal valorEntrega = BigDecimal.valueOf(pedido.getValorEntrega());
        BigDecimal total = valorTotal.add(valorEntrega).setScale(2, RoundingMode.HALF_UP);

        //double totalPedido = (pedido.valorTotal() + pedido.getValorEntrega());

        PaymentPayerRequest payer = PaymentPayerRequest.builder()
                .email(usuario.getEmail())
                .build();

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(total)
                .description("Compra GiostriConstruções - pix")
                .paymentMethodId("pix")
                .payer(payer)
                .build();

        PaymentClient client = new PaymentClient();
        Payment payment = client.create(paymentCreateRequest);

        return payment;
    }

    public Payment pagarComCartao(PagamentoComCartaoRequest request) throws MPException, MPApiException {
        PaymentPayerRequest payer = PaymentPayerRequest.builder()
                .email(request.payerEmail())
                .build();

        PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
                .transactionAmount(request.valorTotal())
                .token(request.tokenCartao())
                .description("Pedido " + request.pedidoId())
                .issuerId(request.issuerId())
                .installments(request.metodoPagamento().equals("credit_card") ? request.parcelas() : 1)
                .paymentMethodId(request.bandeira())
                .payer(payer)
                .build();

        return new PaymentClient().create(paymentRequest);
    }

}
