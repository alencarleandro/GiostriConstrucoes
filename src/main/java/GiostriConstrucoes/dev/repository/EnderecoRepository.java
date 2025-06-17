package GiostriConstrucoes.dev.repository;

import GiostriConstrucoes.dev.model.Endereco;
import GiostriConstrucoes.dev.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

}