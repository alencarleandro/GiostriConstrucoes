package GiostriConstrucoes.dev.controller;

import GiostriConstrucoes.dev.config.TokenService;
import GiostriConstrucoes.dev.controller.request.*;
import GiostriConstrucoes.dev.model.Carrinho;
import GiostriConstrucoes.dev.model.DTO.UsuarioDTO;
import GiostriConstrucoes.dev.controller.util.ControllerCore;
import GiostriConstrucoes.dev.model.Endereco;
import GiostriConstrucoes.dev.model.Usuario;
import GiostriConstrucoes.dev.repository.EnderecoRepository;
import GiostriConstrucoes.dev.repository.UsuarioRepository;
import GiostriConstrucoes.dev.service.UsuarioService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/giostri/usuario")
@RequiredArgsConstructor
public class UsuarioController extends ControllerCore {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid UsuarioLogarRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailOrCpf(request.emailCPF());

            if (usuarioOpt.isPresent() && passwordEncoder.matches(request.senha(), usuarioOpt.get().getSenha())) {
                response.put("success", true);

                UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioOpt.get());
                usuarioDTO.setToken(tokenService.generateToken(usuarioOpt.get()));

                response.put("usuario", usuarioDTO);

                return ResponseEntity.ok(response);
            }

            return errorResponse(response, "Credenciais inválidas.", HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return errorResponse(response, "Erro durante o login: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrar(@RequestBody @Valid UsuarioRegistrarRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validações básicas
            if (request.senha() == null || request.senha().isEmpty()) {
                return errorResponse(response, "A senha não pode ser nula ou vazia.");
            }

            String cpfNumerico = request.cpf().replaceAll("\\D", "");
            if (!isCPF(cpfNumerico)) {
                return errorResponse(response, "CPF inválido.");
            }

            // Verifica existência prévia
            if (usuarioRepository.existsByCpf(cpfNumerico)) {
                return errorResponse(response, "CPF já cadastrado.");
            }
            if (usuarioRepository.existsByEmail(request.email())) {
                return errorResponse(response, "Email já cadastrado.");
            }

            // Cria e salva usuário
            Usuario usuario = criarUsuarioFromRequest(request, cpfNumerico);
            usuarioRepository.save(usuario);

            // Não retirar esse segundo salvar, ele serve pra associar um carrinho ao usuario criado, ele n foi associado antes pois o id do usuario não tinha sido gerado.
            usuario.setCarrinho(Carrinho.builder().usuario(usuario).build());
            usuarioRepository.save(usuario);

            // Resposta de sucesso
            response.put("success", true);
            response.put("message", "Usuário registrado com sucesso.");
            response.put("userId", usuario.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return errorResponse(response, "Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<Map<String, Object>> enviarEmail(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = payload.get("email");

            if (email == null || email.isEmpty()) {
                return errorResponse(response, "Email não fornecido.");
            }

            if (!usuarioService.existeUsuarioComEmail(email)) {
                return errorResponse(response, "Email não cadastrado.");
            }

            Resend resend = new Resend("re_B3JfZzvY_6wHDGhTMhBU4pdgwWj3wEjnX");

            String assunto = "Giostri Construções - Recuperar Senha";
            String mensagemHtml = "<p>Olá!</p>" +
                    "<p>Para redefinir sua senha, clique no link abaixo:</p>" +
                    "<a href='http://127.0.0.1:5500/paginas/redefinirSenhaRecuperacao.html?email=" + email + "'>Redefinir Senha</a>";

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Giostri <onboarding@resend.dev>")
                    .to(email)
                    .subject(assunto)
                    .html(mensagemHtml)
                    .build();

            CreateEmailResponse data = null;

            try {
                data = resend.emails().send(params);
                System.out.println("Email enviado com ID: " + data.getId());
            } catch (ResendException e) {
                e.printStackTrace();
                return errorResponse(response, "Falha ao enviar o email: " + e.getMessage());
            }

            if (data != null && data.getId() != null) {
                response.put("success", true);
                response.put("message", "Email enviado com sucesso.");
                return ResponseEntity.ok(response);
            } else {
                return errorResponse(response, "Falha ao enviar o email.");
            }

        } catch (Exception e) {
            return errorResponse(response, "Erro ao recuperar senha: " + e.getMessage());
        }
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<Map<String, Object>> updateSenha(@RequestBody UsuarioRedefinirSenhaRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            usuarioService.alterarSenha(request);
            return successResponse(response, "Senha alterada com sucesso.");

        } catch (Exception e) {
            return errorResponse(response, "Erro ao alterar senha: " + e.getMessage());
        }
    }

    @PutMapping("/alterar-dados-usuario")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Map<String, Object> response = new HashMap<>();

        try {

            Usuario usuario = usuarioService.sincronizar(usuarioDTO);
            usuarioService.alterarUsuario(usuario);

            UsuarioDTO user = new UsuarioDTO(usuario);
            user.setToken(tokenService.generateToken(usuario));

            response.put("usuario", user);

            return successResponse(response, "Usuário atualizado com sucesso.");

        } catch (Exception e) {
            return errorResponse(response, "Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @PutMapping("/alterar-senha-usuario")
    public ResponseEntity<Map<String, Object>> alterarSenha(@RequestBody UsuarioAlterarSenhaRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            usuarioService.alterarSenha(request);
            return successResponse(response, "Senha alterada com sucesso.");

        } catch (Exception e) {
            return errorResponse(response, "Erro ao alterar senha: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar-usuario")
    public ResponseEntity<Map<String, Object>> deletar(@RequestParam String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (usuarioService.excluirUsuario(id)) {
                return successResponse(response, "Usuário excluído com sucesso.");
            }
            return errorResponse(response, "Usuário não encontrado.", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return errorResponse(response, "Erro ao excluir usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/salvar-endereco")
    public ResponseEntity<Map<String, Object>> salvarEndereco(@RequestBody EnderecoRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {

            Usuario usuario = usuarioService.saveEndereco(request);

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailOrCpf(usuario.getCpf());
            usuario = usuarioOpt.get();

            UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
            usuarioDTO.setToken(tokenService.generateToken(usuario));

            response.put("usuario", usuarioDTO);
            System.out.println(usuarioDTO.getEnderecos().size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return errorResponse(response, "Erro ao salvar endereco: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover-endereco")
    public ResponseEntity<Map<String, Object>> removerEndereco(@RequestParam String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Usuario usuario = usuarioService.excluirEndereco(id);

            if (usuario != null) {

                UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
                usuarioDTO.setToken(tokenService.generateToken(usuario));

                response.put("usuario", usuarioDTO);

                return successResponse(response, "Endereco excluído com sucesso.");
            }
            return errorResponse(response, "Endereco não encontrado.", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return errorResponse(response, "Erro ao excluir endereco: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Métodos auxiliares
    private Usuario criarUsuarioFromRequest(UsuarioRegistrarRequest request, String cpfNumerico) {
        Usuario usuario = new Usuario();
        Endereco endereco = new Endereco();

        // Configura usuário
        usuario.setNome(request.nome());
        usuario.setSobrenome(request.sobrenome());
        usuario.setCpf(cpfNumerico);
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setTelefone(request.telefone().replaceAll("\\D", ""));
        usuario.setDataNascimento(toData(request.dataNascimento()));
        usuario.setGenero(request.genero());
        usuario.setAdmin(false);

        // Configura endereço
        endereco.setUsuario(usuario);
        endereco.setCep(request.cep().replaceAll("\\D", ""));
        endereco.setLogradouro(request.logradouro());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setBairro(request.bairro());
        endereco.setCidade(request.cidade());
        endereco.setUf(request.uf().toUpperCase());

        usuario.getEnderecos().add(endereco);
        return usuario;
    }

    private ResponseEntity<Map<String, Object>> successResponse(Map<String, Object> response, String message) {
        response.put("success", true);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Map<String, Object> response, String message) {
        return errorResponse(response, message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Map<String, Object> response, String message, HttpStatus status) {
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}