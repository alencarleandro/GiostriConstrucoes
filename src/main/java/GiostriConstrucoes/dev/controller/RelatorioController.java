package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.service.RelatorioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/giostri/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/vendas")
    public ResponseEntity<byte[]> gerarRelatorioVendas() {

        System.out.println("Entrou");

        byte[] pdfBytes = relatorioService.gerarRelatorioVendas();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_vendas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/estoque")
    public ResponseEntity<byte[]> gerarRelatorioEstoque() {

        System.out.println("Entrou");

        byte[] pdfBytes = relatorioService.gerarRelatorioProdutos();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_estoque.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> gerarRelatorioUsuarios() {

        System.out.println("Entrou");

        byte[] pdfBytes = relatorioService.gerarRelatorioUsuarios();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_usuarios.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
