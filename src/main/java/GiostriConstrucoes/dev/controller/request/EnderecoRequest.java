package GiostriConstrucoes.dev.controller.request;

import GiostriConstrucoes.dev.model.DTO.EnderecoDTO;

public record EnderecoRequest(
        String usuarioId,
        EnderecoDTO endereco){
}
