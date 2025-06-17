package GiostriConstrucoes.dev.repository;

import GiostriConstrucoes.dev.model.DTO.ProdutoDTO;
import GiostriConstrucoes.dev.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT DISTINCT p FROM Produto p LEFT JOIN FETCH p.usuariosQueFavoritaram")
    List<Produto> findAllWithFavoritos();

    @Query("SELECT new GiostriConstrucoes.dev.model.DTO.ProdutoDTO(" +
            "CAST(p.id AS string), p.nome, p.preco, p.categoria, " +
            "p.descricao, p.quantidadeEmEstoque, p.imgUrl) " +
            "FROM Produto p")
    List<ProdutoDTO> findAllAsDTO();
}