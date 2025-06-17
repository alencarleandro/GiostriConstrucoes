package GiostriConstrucoes.dev.repository;

import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, String> {

}
