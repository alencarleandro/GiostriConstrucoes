package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.controller.request.FavoritoRequest;
import GiostriConstrucoes.dev.model.DTO.ProdutoDTO;
import GiostriConstrucoes.dev.model.Produto;
import GiostriConstrucoes.dev.model.Usuario;
import GiostriConstrucoes.dev.repository.ProdutoRepository;
import GiostriConstrucoes.dev.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritosService {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean favoritar(FavoritoRequest request){
        Optional<Usuario> usuario = usuarioRepository.findById(request.usuarioID());
        Optional<Produto> produto = produtoRepository.findById(Long.parseLong(request.produtoID()));
        if(usuario.isPresent() && produto.isPresent()){
            usuario.get().getProdutosFavoritos().add(produto.get());
            usuarioRepository.save(usuario.get());
            return true;
        }
        return false;
    }

    public boolean desfavoritar(FavoritoRequest request){
        Optional<Usuario> usuario = usuarioRepository.findById(request.usuarioID());
        Optional<Produto> produto = produtoRepository.findById(Long.parseLong(request.produtoID()));
        if(usuario.isPresent() && produto.isPresent()){
            for(int i = 0; i < usuario.get().getProdutosFavoritos().size(); i++){
                if(usuario.get().getProdutosFavoritos().get(i).getId() == produto.get().getId()){
                    usuario.get().getProdutosFavoritos().remove(i);
                    break;
                }
            }
                usuarioRepository.save(usuario.get());
            return true;
        }
        return false;
    }

    public List<ProdutoDTO> favoritos(String id){

        List<ProdutoDTO> resposta = new ArrayList<>();
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if(usuario.isPresent()){
            for(Produto produto : usuario.get().getProdutosFavoritos()){
                resposta.add(ProdutoDTO.toProdutoDTO(produto));
            }
        }

        return resposta;
    }


}
