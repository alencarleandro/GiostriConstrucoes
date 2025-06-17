package GiostriConstrucoes.dev.model.DTO;

import GiostriConstrucoes.dev.model.Endereco;
import GiostriConstrucoes.dev.model.Usuario;
import GiostriConstrucoes.dev.util.IDataUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDTO implements IDataUtil {

    private Long id;

    @NotBlank(message = "Nome não pode estar vazio")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    private String duasPrimeirasLetrasDoNome;

    @NotBlank(message = "Sobrenome não pode estar vazio")
    @Size(max = 100, message = "Sobrenome pode ter no máximo 100 caracteres")
    private String sobrenome;

    @NotBlank(message = "CPF não pode estar vazio")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve conter 11 dígitos, e o CPF com máscara 14")
    private String cpf;

    @NotBlank(message = "Email não pode estar vazio")
    @Email(message = "Email deve ser válido")
    @Size(max = 60, message = "Email pode ter no máximo 60 caracteres")
    private String email;

    @NotBlank(message = "Telefone não pode estar vazio")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos numéricos")
    private String telefone;

    private String genero;

    @NotNull(message = "Data de nascimento não pode estar vazia")
    private String dataNascimento;

    private boolean isAdmin;

    private String token;

    @NotEmpty(message = "Pelo menos um endereço deve ser informado")
    private List<@Valid EnderecoDTO> enderecos;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.duasPrimeirasLetrasDoNome = extrairIniciais(usuario.getNome());
        this.sobrenome = usuario.getSobrenome();
        this.cpf = formatarCPF(usuario.getCpf());
        this.email = usuario.getEmail();
        this.telefone = formatarTelefone(usuario.getTelefone());
        this.genero = usuario.getGenero();
        this.dataNascimento = dataToString(usuario.getDataNascimento());
        this.isAdmin = usuario.isAdmin();
        this.enderecos = usuario.getEnderecos().stream()
                .map(EnderecoDTO::new)
                .collect(Collectors.toList());
    }

    public List<Endereco> getEnderecosOriginais(Usuario usuario) {
        return this.enderecos.stream()
                .map(dto -> dto.toEndereco(usuario))
                .collect(Collectors.toList());
    }

    private String extrairIniciais(String nome) {
        if (nome == null || nome.length() < 2) return "";
        return nome.substring(0, 2).toUpperCase();
    }

    private String formatarCPF(String cpf) {
        if (cpf == null) return null;
        cpf = cpf.replaceAll("\\D", ""); // Remove todos os caracteres não numéricos
        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
        }
        return cpf;
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null) return null;
        telefone = telefone.replaceAll("\\D", ""); // Remove todos os caracteres não numéricos
        if (telefone.length() == 11) {
            return telefone.substring(0, 2) + telefone.substring(2, 7) + telefone.substring(7);  // Formatação sem máscara
        } else if (telefone.length() == 10) {
            return telefone.substring(0, 2) + telefone.substring(2, 6) + telefone.substring(6);  // Formatação sem máscara
        }
        return telefone;
    }


    private String formatarData(LocalDate data) {
        return data != null ? data.toString() : null;
    }
}