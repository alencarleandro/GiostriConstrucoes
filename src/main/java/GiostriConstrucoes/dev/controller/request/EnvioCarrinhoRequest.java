package GiostriConstrucoes.dev.controller.request;

public record EnvioCarrinhoRequest (String produtoId,
                                    String usuarioId,
                                    String quantidade){}
