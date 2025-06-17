package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.controller.request.FavoritoRequest;
import GiostriConstrucoes.dev.model.DTO.ProdutoDTO;
import GiostriConstrucoes.dev.service.FavoritosService;
import GiostriConstrucoes.dev.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/giostri/favoritos")
public class ProdutosFavoritosController {
    @Autowired
    private FavoritosService favoritosService;
    @Autowired
    private ProdutoService produtoService;

    @PutMapping("/favoritar")
    public ResponseEntity<?> favoritar(@RequestBody FavoritoRequest request) {
        System.out.println(request.produtoID() + request.usuarioID());
        if (favoritosService.favoritar(request))
            return ResponseEntity.ok(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/desfavoritar")
    public ResponseEntity<?> desfavoritar(@RequestBody FavoritoRequest request) {
        if (favoritosService.desfavoritar(request))
            return ResponseEntity.ok(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/produtosFavoritos")
    public ResponseEntity<List<ProdutoDTO>> produtosFavoritos(@RequestParam String id) {

        List<ProdutoDTO> resposta = favoritosService.favoritos(id);

        if (!resposta.isEmpty()) {
            return ResponseEntity.ok(resposta);
        }

        return ResponseEntity.noContent().build();
    }
}
