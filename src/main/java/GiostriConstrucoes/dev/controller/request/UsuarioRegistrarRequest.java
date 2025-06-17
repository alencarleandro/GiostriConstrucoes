package GiostriConstrucoes.dev.controller.request;

public record UsuarioRegistrarRequest(String nome,
                                      String sobrenome,
                                      String cpf,
                                      String email,
                                      String telefone,
                                      String senha,
                                      String dataNascimento,
                                      String genero,
                                      String cep,
                                      String logradouro,
                                      String numero,
                                      String complemento,
                                      String bairro,
                                      String cidade,
                                      String uf) {
}
