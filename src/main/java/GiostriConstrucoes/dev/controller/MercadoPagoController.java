package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.controller.request.PagamentoComCartaoRequest;
import GiostriConstrucoes.dev.controller.request.PagamentoComPixRequest;
import GiostriConstrucoes.dev.service.MercadoPagoService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/giostri/pagamento")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/pix")
    public ResponseEntity<?> pagamentoPix(@RequestBody PagamentoComPixRequest request) throws MPException, MPApiException {
        try {

            System.out.println("Resposta do js: " + request);

            Payment payment = mercadoPagoService.pagarComPix(request);

            System.out.println("Resposta do Mercado Pago: " + payment);
            return ResponseEntity.ok().body(Map.of(
                    "qrcode", payment.getPointOfInteraction().getTransactionData().getQrCode(),
                    "qrCodeBase64", payment.getPointOfInteraction().getTransactionData().getQrCodeBase64(),
                    "status", payment.getStatus(),
                    "id", payment.getId(),
                    "descricao",payment.getDescription()
            ));
        }catch (MPApiException e){
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "mensagem", "Erro ao processar pagamento",
                    "erroDetalhado", e.getApiResponse().getContent()
            ));
        }
    }

    @PostMapping("/cartao")
    public ResponseEntity<?> pagamentoCartao(@RequestBody @Valid PagamentoComCartaoRequest request) {
        try {

            System.out.println(request);

            // Validação específica para débito
            if (request.metodoPagamento().equals("debit_card")) {
                if (request.parcelas() > 1) {
                    return ResponseEntity.badRequest().body(
                            Map.of("erro", "Pagamento com débito não pode ser parcelado")
                    );
                }

                if (!Set.of("visa", "master", "elo").contains(request.bandeira())) {
                    return ResponseEntity.badRequest().body(
                            Map.of("erro", "Bandeira não suportada para débito")
                    );
                }
            }

            Payment payment = mercadoPagoService.pagarComCartao(request);

            return ResponseEntity.ok(Map.of(
                    "status", payment.getStatus(),
                    "id", payment.getId(),
                    "descricao", payment.getDescription(),
                    "metodo", payment.getPaymentMethodId(),
                    "valor", payment.getTransactionAmount(),
                    "data", LocalDateTime.now().toString()
            ));

        } catch (MPApiException e) {
            return ResponseEntity.status(e.getStatusCode()).body(
                    Map.of("erro", "Falha no processamento",
                            "detalhes", e.getApiResponse().getContent())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("erro", "Falha interna", "mensagem", e.getMessage())
            );
        }
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<?> verificarStatusPagamento(@PathVariable String paymentId) throws MPException, MPApiException {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));
            return ResponseEntity.ok(payment.getStatus());
        } catch (MPApiException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "mensagem", "Erro ao verificar status do pagamento",
                    "erroDetalhado", e.getApiResponse().getContent()
            ));
        }
    }
}
