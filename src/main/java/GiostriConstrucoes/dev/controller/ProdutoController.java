package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.controller.util.ControllerCore;
import GiostriConstrucoes.dev.model.DTO.ProdutoDTO;
import GiostriConstrucoes.dev.model.Produto;
import GiostriConstrucoes.dev.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/giostri/produto")
public class ProdutoController extends ControllerCore {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/all")
    public ResponseEntity<List<ProdutoDTO>> listar() {
        List<ProdutoDTO> resposta = produtoService.listarProdutosOtimizado();
        return resposta.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.getProdutoById(id);
        if (produto.isPresent()) {
            return ResponseEntity.ok(ProdutoDTO.toProdutoDTO(produto.get()));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Produto não encontrado");
    }

    @GetMapping("/nome")
    public ResponseEntity<?> buscarPorNomeContendo(@RequestParam String nome) {
        List<Produto> produtos = produtoService.getProdutosByNome(nome);

        if (!produtos.isEmpty()) {
            List<ProdutoDTO> dtos = produtos.stream().map(ProdutoDTO::toProdutoDTO).toList();
            return ResponseEntity.ok(dtos);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto encontrado com esse nome");
    }


    @PostMapping("/add")
    public ResponseEntity<?> addProduto(@RequestBody ProdutoDTO produto) {
        Map<String, Object> response = new HashMap<>();

        try {
            produtoService.addProduto(produto);
            return ResponseEntity.ok().body("Produto cadastrado com sucesso" + produto);

        } catch (Exception e) {
            return errorResponse(response, "Erro ao adicionar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.getProdutoById(id);
        if (produto.isPresent()) {
            produtoService.deleteProdutoById(id);
            return ResponseEntity.ok().body("Produto deletado com sucesso");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Falha ao deletar item, produto não encontrado");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody ProdutoDTO DTO) {
        Optional<Produto> produto = produtoService.getProdutoById(DTO.getId());
        if (produto.isPresent()) {
            Optional<Produto> novoProduto =  produtoService.updateProduto(DTO,produto.get());
            if (novoProduto.isPresent()) {
                return ResponseEntity.ok("Produto atualizado com sucesso" + novoProduto.get().getNome());
            }
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Erro ao atualizar item, produto não encontrado");
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Map<String, Object> response, String message) {
        return errorResponse(response, message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Map<String, Object> response, String message, HttpStatus status) {
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

}
