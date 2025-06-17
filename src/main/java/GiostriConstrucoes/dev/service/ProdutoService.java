package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.model.DTO.ProdutoDTO;
import GiostriConstrucoes.dev.model.Produto;
import GiostriConstrucoes.dev.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoDTO> listarProdutosOtimizado() {
        return produtoRepository.findAllAsDTO();
    }

    public List<ProdutoDTO> listarProdutosComFavoritos() {
        List<Produto> produtos = produtoRepository.findAllWithFavoritos();
        return produtos.stream()
                .map(ProdutoDTO::toProdutoDTO)
                .toList();
    }

    public List<Produto> getProdutosByNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Produto> getProdutoById(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    public Optional<Produto> getProdutoById(String codigo) {
        return produtoRepository.findById(Long.parseLong(codigo));
    }

    public Produto addProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void addProduto(ProdutoDTO produto) {
        produtoRepository.save(Produto.criarProdutoFromDTO(produto));
    }

    public void deleteProdutoById(Long codigo) {
        Optional<Produto> produtoById = produtoRepository.findById(codigo);
        if (produtoById.isPresent()) {
            produtoRepository.delete(produtoById.get());
        }
    }

    public Optional<Produto> updateProduto(ProdutoDTO DTO, Produto produto) {
        return Optional.of(produtoRepository.save(produto.editarProdutoFromDTO(DTO)));
    }


}
