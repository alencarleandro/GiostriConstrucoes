package GiostriConstrucoes.dev.model.DTO;

import GiostriConstrucoes.dev.model.Endereco;
import GiostriConstrucoes.dev.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoDTO {

    private String id;

    @NotBlank(message = "CEP não pode estar vazio")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;

    @NotBlank(message = "Logradouro não pode estar vazio")
    private String logradouro;

    @NotBlank(message = "Número não pode estar vazio")
    private String numero;

    private String complemento;

    @NotBlank(message = "Bairro não pode estar vazio")
    private String bairro;

    @NotBlank(message = "Cidade não pode estar vazio")
    private String cidade;

    @NotBlank(message = "UF não pode estar vazio")
    @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
    private String uf;

    public EnderecoDTO(Endereco endereco) {
        this.id = String.valueOf(endereco.getId());
        this.cep = formatarCEP(endereco.getCep());
        this.logradouro = endereco.getLogradouro();
        this.numero = endereco.getNumero();
        this.complemento = endereco.getComplemento();
        this.bairro = endereco.getBairro();
        this.cidade = endereco.getCidade();
        this.uf = endereco.getUf();
    }

    public Endereco toEndereco(Usuario usuario) {
        return Endereco.builder()
                .usuario(usuario)
                .cep(this.cep.replace("-", ""))
                .logradouro(this.logradouro)
                .numero(this.numero)
                .complemento(this.complemento)
                .bairro(this.bairro)
                .cidade(this.cidade)
                .uf(this.uf.toUpperCase())
                .build();
    }

    private String formatarCEP(String cep) {
        if (cep == null) return null;
        cep = cep.replaceAll("\\D", "");
        if (cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5);
        }
        return cep;
    }
}