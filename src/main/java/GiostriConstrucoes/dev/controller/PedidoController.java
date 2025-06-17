package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.model.DTO.*;
import GiostriConstrucoes.dev.model.ItemDePedido;
import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.repository.UsuarioRepository;
import GiostriConstrucoes.dev.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping(value = "/giostri/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Pedido> list = pedidoService.listarPedidos();
        List<CarregarPedidoDTO> response = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.systemDefault());

        for (Pedido pedido : list) {
            CarregarPedidoDTO pedidoDTO = CarregarPedidoDTO.builder()
                    .total(String.valueOf(pedido.valorTotal()))
                    .cliente(pedido.getCliente().getNome())
                    .data(formatter.format(pedido.getDataDeLancamento()))
                    .status(pedido.getPedidoStatus().name().toLowerCase())
                    .id(String.valueOf(pedido.getId()))
                    .build();
            response.add(pedidoDTO);
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("meusPedidos/{id}")
    public ResponseEntity<?> findAllById(@PathVariable String id) {
        List<MeusPedidosDTO> response = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.systemDefault());

       usuarioRepository.findById(id).map(usuario -> {
            for (Pedido pedido : usuario.getPedidos()) {

                List<StatusDTO> lista = List.of(StatusDTO.builder()
                        .status(pedido.getPedidoStatus().name().toLowerCase())
                        .texto("Pedido Recebido")
                        .data(formatter.format(pedido.getDataDeLancamento()))
                        .concluido(true)
                        .build());
                MeusPedidosDTO pedidoDTO = MeusPedidosDTO.builder()
                        .numero("CONSTRU2025" + pedido.getId())
                        .data(formatter.format(pedido.getDataDeLancamento()))
                        .status(pedido.getPedidoStatus().name().toLowerCase())
                        .produtos(String.valueOf(pedido.getItens().size()))
                        .endereco(usuario.getEnderecos().get(0).getLogradouro() + ", " + usuario.getEnderecos().get(0).getNumero() + ", " + usuario.getEnderecos().get(0).getUf())
                        .pagamento("PIX")
                        .cliente(usuario.getNome())
                        .total(String.valueOf(pedido.valorTotal()))
                        .statusSteps(lista)
                        .build();
                response.add(pedidoDTO);

            }

            return true;
        });

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Pedido pedido = pedidoService.getPedidoById(id);

        CarregarPedidoDTO pedidoDTO = CarregarPedidoDTO.builder()
                .total(String.valueOf(pedido.valorTotal()))
                .cliente(pedido.getCliente().getNome())
                .data(pedido.getDataDeLancamento().toString())
                .status(pedido.getPedidoStatus().name().toLowerCase())
                .id(String.valueOf(pedido.getId()))
                .build();

        return ResponseEntity.ok(pedidoDTO);
    }

    @GetMapping("todosProdutos/{id}")
    public ResponseEntity<?> todosProdutos(@PathVariable String id) {
        Pedido pedido = pedidoService.getPedidoById(id);
        List<CarregarProdutosDoPedidoDTO> response = new ArrayList<>();

        for (ItemDePedido itemDePedido : pedido.getItens()) {
            CarregarProdutosDoPedidoDTO item = CarregarProdutosDoPedidoDTO.builder()
                    .nome(itemDePedido.getProduto().getNome())
                    .preco(String.valueOf(itemDePedido.getProduto().getPreco()))
                    .quantidade(String.valueOf(itemDePedido.getQuantidade()))
                    .build();
            response.add(item);
        }

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody String status) {
        System.out.println(id);
        System.out.println(status);

        pedidoService.updateStatus(id, status);
        return ResponseEntity.ok().body("Pedido atualizado com sucesso");
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Map<String, Object> response, String message, HttpStatus status) {
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}