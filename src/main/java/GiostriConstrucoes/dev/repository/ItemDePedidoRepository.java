package GiostriConstrucoes.dev.repository;

import GiostriConstrucoes.dev.model.ItemDeCarrinho;
import GiostriConstrucoes.dev.model.ItemDePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDePedidoRepository extends JpaRepository<ItemDePedido, String> {

}
