package GiostriConstrucoes.dev.repository;

import GiostriConstrucoes.dev.model.Carrinho;
import GiostriConstrucoes.dev.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, String> {

}