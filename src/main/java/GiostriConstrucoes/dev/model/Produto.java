package GiostriConstrucoes.dev.model;

import GiostriConstrucoes.dev.model.DTO.ProdutoDTO;
import GiostriConstrucoes.dev.model.util.GerenciavelSimples;
import GiostriConstrucoes.dev.repository.ProdutoRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "produto")
public class Produto extends GerenciavelSimples {

    @Column(name = "nome")
    private String nome;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "preco")
    private double preco;
    @Column(name = "imgUrl")
    private String imgUrl;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "quantidadeEmEstoque")
    private int quantidadeEmEstoque;

    @ManyToMany(mappedBy = "produtosFavoritos", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Usuario> usuariosQueFavoritaram = new ArrayList<>();


    public static Produto criarProdutoFromDTO(ProdutoDTO DTO) {
        Produto produto = new Produto();

        produto.setQuantidadeEmEstoque(DTO.getQuantidadeEmEstoque());
        produto.setCategoria(DTO.getCategoria());
        produto.setDescricao(DTO.getDescricao());
        produto.setNome(DTO.getNome());
        produto.setPreco(DTO.getPreco());
        produto.setImgUrl(DTO.getUrl());

        return produto;
    }

    public Produto editarProdutoFromDTO(ProdutoDTO DTO) {

        this.setQuantidadeEmEstoque(DTO.getQuantidadeEmEstoque());
        this.setCategoria(DTO.getCategoria());
        this.setDescricao(DTO.getDescricao());
        this.setNome(DTO.getNome());
        this.setPreco(DTO.getPreco());
        this.setImgUrl(DTO.getUrl());

        return this;
    }

}
