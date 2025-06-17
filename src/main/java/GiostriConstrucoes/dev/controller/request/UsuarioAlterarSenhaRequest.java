package GiostriConstrucoes.dev.controller.request;

public record UsuarioAlterarSenhaRequest(
        String id,
        String senhaAtual,
        String novaSenha) {
}
