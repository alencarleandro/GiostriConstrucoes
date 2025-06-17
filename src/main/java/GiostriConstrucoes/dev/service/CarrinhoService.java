package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.controller.request.EnvioCarrinhoRequest;
import GiostriConstrucoes.dev.model.*;
import GiostriConstrucoes.dev.model.DTO.ItemDeCarrinhoDTO;
import GiostriConstrucoes.dev.model.DTO.alteracaoCarrinhoDTO;
import GiostriConstrucoes.dev.model.enums.EPedidoStatus;
import GiostriConstrucoes.dev.repository.*;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class CarrinhoService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CarrinhoRepository carrinhoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemDeCarrinhoRepository itemDeCarrinhoRepository;
    @Autowired
    private ItemDePedidoRepository itemDePedidoRepository;

    public void adicionarAoCarrinho(EnvioCarrinhoRequest request) {
        Optional<Usuario> usuario = usuarioRepository.findById(request.usuarioId());
        Optional<Produto> produto = produtoRepository.findById(Long.parseLong(request.produtoId()));

        if (usuario.isPresent() && produto.isPresent()) {
            ItemDeCarrinho itemDeCarrinho = ItemDeCarrinho.builder().carrinho(usuario.get().getCarrinho()).quantidade(Integer.parseInt(request.quantidade())).produto(produto.get()).build();
            usuario.get().getCarrinho().getItens().add(itemDeCarrinho);
            usuarioRepository.save(usuario.get());
        }
    }

    public List<ItemDeCarrinhoDTO> carrinhoDoUsuario(String id) {
        List<ItemDeCarrinhoDTO> resposta = new ArrayList<>();
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            for (ItemDeCarrinho itemDeCarrinho : usuario.get().getCarrinho().getItens()) {
                resposta.add(ItemDeCarrinhoDTO.converter(itemDeCarrinho));
            }
        }

        return resposta;
    }

    public void editarQuantidade(alteracaoCarrinhoDTO request) {
        Optional<ItemDeCarrinho> itemDeCarrinho = itemDeCarrinhoRepository.findById(request.id());
        if (itemDeCarrinho.isPresent()) {
            itemDeCarrinho.get().setQuantidade(Integer.parseInt(request.quantidade()));
            itemDeCarrinhoRepository.save(itemDeCarrinho.get());
        }
    }

    public boolean removerItemDeCarrinho(String id) {
        itemDeCarrinhoRepository.deleteById(id);
        return itemDeCarrinhoRepository.findById(id)
                .map(item -> {
                    Carrinho carrinho = item.getCarrinho();
                    carrinho.getItens().remove(item);
                    carrinhoRepository.save(carrinho);
                    return true;
                })
                .orElse(false);
    }

    public String finalizarCarrinho(String usuarioId, String taxa, String descricao) {
        return usuarioRepository.findById(usuarioId).map(usuario -> {
            ItemDePedido itemDePedido;

            Pedido pedido = Pedido.builder()
                    .cliente(usuario)
                    .dataDeLancamento(Instant.now())
                    .pedidoStatus(EPedidoStatus.PROCESSANDO)
                    .itens(new ArrayList<>())
                    .descricao(descricao)
                    .valorEntrega(Double.parseDouble(taxa))
                    .build();

            for (ItemDeCarrinho itemDeCarrinho : usuario.getCarrinho().getItens()) {

                itemDePedido = ItemDePedido.builder()
                        .produto(itemDeCarrinho.getProduto())
                        .quantidade(itemDeCarrinho.getQuantidade())
                        .pedido(pedido)
                        .build();

                pedido.getItens().add(itemDePedido);

                itemDeCarrinho.getProduto().setQuantidadeEmEstoque(itemDeCarrinho.getProduto().getQuantidadeEmEstoque() - itemDeCarrinho.getQuantidade());

                if(itemDeCarrinho.getProduto().getQuantidadeEmEstoque() < 10){
                    checarEstoque(itemDeCarrinho.getProduto());
                }

                produtoRepository.save(itemDeCarrinho.getProduto());
            }
            Pedido p = pedidoRepository.save(pedido);
            usuario.getCarrinho().getItens().clear();
            usuarioRepository.save(usuario);

            return String.valueOf(p.getId());
        }).orElse("-1");
    }

    private void checarEstoque(Produto produto) {

        try {

            String email = "letdevs.engsoftware@gmail.com";

            Resend resend = new Resend("re_B3JfZzvY_6wHDGhTMhBU4pdgwWj3wEjnX");

            String assunto = "Giostri Construções (Administrador) - Alerta de estoque baixo";
            String mensagemHtml = "<p>Olá Administrador!</p>" +
                    "<p>Seu estoque do produto \""+produto.getNome()+"\" esta baixo, restando somente "+produto.getQuantidadeEmEstoque()+" unidades.</p>";

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Giostri <onboarding@resend.dev>")
                    .to(email)
                    .subject(assunto)
                    .html(mensagemHtml)
                    .build();

            CreateEmailResponse data = null;
            data = resend.emails().send(params);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
