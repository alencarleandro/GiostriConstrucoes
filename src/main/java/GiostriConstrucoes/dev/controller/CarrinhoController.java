package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.controller.request.EnvioCarrinhoRequest;
import GiostriConstrucoes.dev.controller.request.FinalizarCarrinhoRequest;
import GiostriConstrucoes.dev.model.DTO.ItemDeCarrinhoDTO;
import GiostriConstrucoes.dev.model.DTO.alteracaoCarrinhoDTO;
import GiostriConstrucoes.dev.repository.ProdutoRepository;
import GiostriConstrucoes.dev.repository.UsuarioRepository;
import GiostriConstrucoes.dev.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/giostri/carrinho")
@RequiredArgsConstructor
public class  CarrinhoController {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final CarrinhoService carrinhoService;

    @PostMapping("/enviar")
    public ResponseEntity<?> enviar(@RequestBody EnvioCarrinhoRequest request) {
        Map<String, Object> response = new HashMap<>();
        carrinhoService.adicionarAoCarrinho(request);
        return ResponseEntity.ok().body("Produto adicionado ao carrinho.");
    }

    @GetMapping("/itensCarrinho/{id}")
    public List<ItemDeCarrinhoDTO> itensCarrinho(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        return carrinhoService.carrinhoDoUsuario(id);
    }

    @PutMapping("/atualizarItem")
    public ResponseEntity<?> atualizarItem(@RequestBody alteracaoCarrinhoDTO request) {
        Map<String, Object> response = new HashMap<>();
        carrinhoService.editarQuantidade(request);
        return ResponseEntity.ok().body("Item atualizado com sucesso.");
    }

    @DeleteMapping("/removerItem/{id}")
    public ResponseEntity<?> removerItem(@PathVariable String id) {
        try {
            boolean removido = carrinhoService.removerItemDeCarrinho(id);
            if (removido) {
                return ResponseEntity.ok().body("Item removido com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Erro: Item com ID '" + id + "' não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover o item: " + e.getMessage());
        }
    }

    @PostMapping("/finalizarCarrinho")
    public ResponseEntity<Map<String, Object>> finalizarCarrinho(@RequestBody FinalizarCarrinhoRequest request) {
        String pedidoId = carrinhoService.finalizarCarrinho(request.usuarioId(), request.taxa(), request.descricao());

        if (pedidoId.equals("-1")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Não foi possível finalizar o carrinho."));
        }

        return ResponseEntity.ok(Map.of("id", pedidoId));
    }




}
