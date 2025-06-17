package GiostriConstrucoes.dev.service;

import GiostriConstrucoes.dev.controller.request.EnderecoRequest;
import GiostriConstrucoes.dev.controller.request.UsuarioAlterarSenhaRequest;
import GiostriConstrucoes.dev.controller.request.UsuarioRedefinirSenhaRequest;
import GiostriConstrucoes.dev.model.DTO.UsuarioDTO;
import GiostriConstrucoes.dev.model.Endereco;
import GiostriConstrucoes.dev.model.Usuario;
import GiostriConstrucoes.dev.repository.EnderecoRepository;
import GiostriConstrucoes.dev.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean excluirUsuario(String id) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            if (usuario.isPresent()) {
                usuarioRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }

    public Usuario excluirEndereco(String id) {
        try {
            Optional<Endereco> enderecoOpt = enderecoRepository.findById(id);

            if (enderecoOpt.isPresent()) {
                Endereco endereco = enderecoOpt.get();
                Usuario usuario = endereco.getUsuario();

                if (usuario != null) {
                    usuario.getEnderecos().remove(endereco);
                    // salvando o usuário para que o orphanRemoval funcione corretamente
                    usuarioRepository.save(usuario);
                }

                // não é necessário chamar delete no repositório
                System.out.println("Endereço removido da lista e excluído pelo JPA");
                return usuario;
            }

            System.out.println("Endereço não encontrado");
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir endereço: " + e.getMessage(), e);
        }
    }

    public boolean existeUsuarioComEmail(String email){

        List<Usuario> lista = usuarioRepository.findAll();

        for (Usuario usuario : lista){
            if(usuario.getEmail().equalsIgnoreCase(email.toLowerCase()))
                return true;
        }
        return false;
    }

    public Usuario sincronizar(UsuarioDTO usuarioDTO) {
        try {
            String id = String.valueOf(usuarioDTO.getId());
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            if (usuario.isPresent()) {
                usuario.get().getEnderecos().clear();
                usuario.get().getEnderecos().addAll(usuarioDTO.getEnderecosOriginais(usuario.get()));
                usuario.get().setEmail(usuarioDTO.getEmail());
                usuario.get().setTelefone(usuarioDTO.getTelefone());
                usuario.get().setNome(usuarioDTO.getNome());

                return usuario.get();
            }

            throw new NoSuchElementException("Usuário não encontrado com ID: " + id);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao sincronizar usuário: " + e.getMessage(), e);
        }
    }

    public Usuario saveEndereco(EnderecoRequest request){
        Optional<Usuario> usuario = usuarioRepository.findById(request.usuarioId());

        if (usuario.isPresent()) {

            if(!request.endereco().getId().isEmpty()){
                System.out.println("Aqui: "+request.endereco().getId());
                Optional<Endereco> endereco = enderecoRepository.findById(request.endereco().getId());
                if (endereco.isPresent()) {
                    for(int i = 0; i < usuario.get().getEnderecos().size(); i++){
                        if(endereco.get().getId() == usuario.get().getEnderecos().get(i).getId()){
                            usuario.get().getEnderecos().remove(i);
                            break;
                        }
                    }
                }
            }

            usuario.get().getEnderecos().add(request.endereco().toEndereco(usuario.get()));

            usuarioRepository.save(usuario.get());


            return usuario.get();
        }else{
            throw new RuntimeException("Usuario não existe");
        }
    }


    public Usuario alterarUsuario(Usuario usuario) {
        try {
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não pode ser nulo");
            }

            String id = String.valueOf(usuario.getId());
            Optional<Usuario> existingUser = usuarioRepository.findById(id);

            if (existingUser.isEmpty()) {
                throw new NoSuchElementException("Usuário não encontrado com ID: " + id);
            }

            usuarioRepository.save(usuario);
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao alterar usuário: " + e.getMessage(), e);
        }
    }

    public void alterarSenha(UsuarioRedefinirSenhaRequest user) {
        try {
            if (user == null || user.email() == null || user.senha() == null) {
                throw new IllegalArgumentException("Dados de alteração de senha inválidos");
            }

            String email = user.email();
            String senha = passwordEncoder.encode(user.senha());
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (usuario.isPresent()) {
                usuario.get().setSenha(senha);
                usuarioRepository.save(usuario.get());
            } else {
                throw new NoSuchElementException("Usuário não encontrado com email: " + email);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao alterar senha: " + e.getMessage(), e);
        }
    }

    public void alterarSenha(UsuarioAlterarSenhaRequest user) {
        try {
            if (user == null || user.novaSenha() == null || user.senhaAtual() == null) {
                throw new IllegalArgumentException("Dados de alteração de senha inválidos");
            }

            String id = user.id();
            String senhaNova = passwordEncoder.encode(user.novaSenha());
            String senhaAntiga = user.senhaAtual();
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            if (usuario.isPresent()) {
                if(passwordEncoder.matches(senhaAntiga, usuario.get().getSenha())){
                    usuario.get().setSenha(senhaNova);
                    usuarioRepository.save(usuario.get());
                }else{
                    throw new RuntimeException("Senha registrada não bate com a senha antiga inserida.");
                }
            } else {
                throw new NoSuchElementException("Usuário não encontrado com id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao alterar senha: " + e.getMessage(), e);
        }
    }

    public void registrarUsuario(Usuario usuario) {
        try {
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não pode ser nulo");
            }

            // Verifica se email já existe
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("Email já cadastrado");
            }

            // Verifica se CPF já existe
            if (usuarioRepository.existsByCpf(usuario.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }

            // Criptografa a senha
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            // Salva o usuário
            usuarioRepository.save(usuario);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar usuário: " + e.getMessage(), e);
        }
    }

    public Usuario findById(String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        try {
            if (!usuario.isPresent()) {
                throw new RuntimeException("Usuário não encontrado");
            }
            return usuario.get();
        }catch (Exception e) {
            throw new RuntimeException("erro: "+ e.getMessage());
        }
    }
}