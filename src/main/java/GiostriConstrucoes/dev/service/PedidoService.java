package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.model.enums.EPedidoStatus;
import GiostriConstrucoes.dev.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    //Listar todos os Pedidos do BD
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(String id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {return pedido;})
                .orElse(null);
    }

    public Pedido addPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void deletePedidoById(String codigo) {
        Optional<Pedido> pedidoById = pedidoRepository.findById(codigo);
        if (pedidoById.isPresent()) {
            pedidoRepository.delete(pedidoById.get());
        }
    }

    /*
    public Optional<Pedido> updatePedido(Pedido pedidoAtual, Pedido pedido) {
        return Optional.of(pedidoRepository.save(pedido.editarProdutoFromDTO(DTO)));
    }
    */

    public boolean updateStatus(String id, String status) {
        return pedidoRepository.findById(id).map(pedido -> {
            try {
                EPedidoStatus novoStatus = EPedidoStatus.valueOf(status.toUpperCase());
                pedido.setPedidoStatus(novoStatus);
                pedidoRepository.save(pedido);
                return true;
            } catch (IllegalArgumentException e) {
                System.out.println("Status inválido: {} " + status + " "+ e);
                return false;
            }
        }).orElse(false);
    }

}
