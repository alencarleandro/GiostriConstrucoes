package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.config.PdfGenerator;
import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.model.Produto;
import GiostriConstrucoes.dev.model.Usuario;
import GiostriConstrucoes.dev.repository.PedidoRepository;
import GiostriConstrucoes.dev.repository.ProdutoRepository;
import GiostriConstrucoes.dev.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private PdfGenerator pdfGenerator = new PdfGenerator();

    public byte[] gerarRelatorioVendas() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pdfGenerator.gerarPdfRelatorioVendas(pedidos);
    }

    public byte[] gerarRelatorioProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return pdfGenerator.gerarPdfRelatorioEstoque(produtos);
    }

    public byte[] gerarRelatorioUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return pdfGenerator.gerarPdfRelatorioUsuarios(usuarios);
    }

}