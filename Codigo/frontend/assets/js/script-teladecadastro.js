document.addEventListener("DOMContentLoaded", function () {
    function formatarCPF(cpf) {
        cpf = cpf.replace(/\D/g, "");
        cpf = cpf.slice(0, 11);
        cpf = cpf.replace(/(\d{3})(\d)/, "$1.$2");
        cpf = cpf.replace(/(\d{3})(\d)/, "$1.$2");
        cpf = cpf.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
        return cpf;
    }

    function validarCPF(cpf) {
        cpf = cpf.replace(/\D/g, "");
        if (cpf.length !== 11 || !/^\d{11}$/.test(cpf)) {
            return false;
        }
        return true;
    }

    function validarEmail(email) {
        const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return regex.test(email);
    }

    function limitarCaracteres(input, maxLength) {
        if (input.value.length > maxLength) {
            input.value = input.value.slice(0, maxLength);
        }
    }

    function validarSenhaForte(senha) {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
        return regex.test(senha);
    }

    function exibirAvisoSenhaFraca() {
        if (!document.getElementById('aviso-senha')) {
            const aviso = document.createElement('div');
            aviso.id = 'aviso-senha';
            aviso.style.color = 'red';
            aviso.style.marginTop = '5px';
            aviso.innerHTML = `
                <strong>Atenção:</strong> A senha deve conter:
                <ul>
                    <li>Pelo menos 8 caracteres</li>
                    <li>Pelo menos uma letra maiúscula</li>
                    <li>Pelo menos uma letra minúscula</li>
                    <li>Pelo menos um número</li>
                    <li>Pelo menos um caractere especial (!@#$%^&*)</li>
                </ul>
            `;
            const campoSenha = document.getElementById("password");
            campoSenha.parentNode.insertBefore(aviso, campoSenha.nextSibling);
        }
    }

    function removerAvisoSenhaFraca() {
        const aviso = document.getElementById('aviso-senha');
        if (aviso) {
            aviso.remove();
        }
    }

    function togglePasswordVisibility(fieldId) {
        const campo = document.getElementById(fieldId);
        const icone = document.querySelector(`#toggle-${fieldId} i`);

        if (campo.type === "password") {
            campo.type = "text";
            icone.classList.remove("fa-eye");
            icone.classList.add("fa-eye-slash");
        } else {
            campo.type = "password";
            icone.classList.remove("fa-eye-slash");
            icone.classList.add("fa-eye");
        }
    }

    function formatarCEP(cep) {
        cep = cep.replace(/\D/g, "");
        cep = cep.slice(0, 8);
        if (cep.length > 5) {
            cep = cep.replace(/(\d{5})(\d)/, "$1-$2");
        }
        return cep;
    }

    function buscarEnderecoPorCEP(cep) {
        cep = cep.replace(/\D/g, "");
        if (cep.length === 8) {
            const url = `https://viacep.com.br/ws/${cep}/json/`;
            fetch(url)
                .then(response => response.json())
                .then(data => {
                    if (!data.erro) {
                        document.getElementById("rua").value = data.logradouro;
                        document.getElementById("bairro").value = data.bairro;
                        document.getElementById("cidade").value = data.localidade;
                        document.getElementById("estado").value = data.uf;
                    } else {
                        alert("CEP não encontrado. Verifique o CEP digitado.");
                    }
                })
                .catch(error => {
                    console.error("Erro ao buscar CEP:", error);
                    alert("Erro ao buscar CEP. Tente novamente.");
                });
        }
    }

    let cepTimeout;
    document.getElementById("cep").addEventListener("input", function () {
        this.value = formatarCEP(this.value);
        clearTimeout(cepTimeout);
        cepTimeout = setTimeout(() => {
            if (this.value.length === 9) {
                buscarEnderecoPorCEP(this.value);
            }
        }, 500);
    });

    function validarEndereco() {
        const cep = document.getElementById("cep").value.trim();
        const rua = document.getElementById("rua").value.trim();
        const numero = document.getElementById("numero").value.trim();
        const bairro = document.getElementById("bairro").value.trim();
        const cidade = document.getElementById("cidade").value.trim();
        const estado = document.getElementById("estado").value.trim();

        if (!cep || !rua || !numero || !bairro || !cidade || !estado) {
            alert("Por favor, preencha todos os campos obrigatórios de endereço.");
            return false;
        }

        if (cep.length !== 9 || !/^\d{5}-\d{3}$/.test(cep)) {
            alert("Por favor, insira um CEP válido no formato XXXXX-XXX.");
            return false;
        }

        return true;
    }

    const formulario = document.querySelector(".editar-formulario");
    const botaoContinuar = document.querySelector('.botao-continuar button');
    const botaoVoltar = document.getElementById('botao-voltar');
    const camposPessoaFisica = document.getElementById('campos-pessoa-fisica');
    const camposEndereco = document.getElementById('campos-endereco');

    let naTelaEndereco = false;

    function salvarDadosPessoaFisica() {
        const dados = {
            nome: document.getElementById("firstname").value.trim(),
            sobrenome: document.getElementById("lastname").value.trim(),
            cpf: document.getElementById("cpf").value.trim(),
            dataNascimento: document.getElementById("birthdate").value.trim(),
            email: document.getElementById("email").value.trim(),
            telefone: document.getElementById("telefone").value.trim(),
            senha: document.getElementById("password").value.trim(),
            confirmarSenha: document.getElementById("confirm-password").value.trim(),
        };
        localStorage.setItem("dadosPessoaFisica", JSON.stringify(dados));
    }

    function salvarDadosEndereco() {
        const dados = {
            cep: document.getElementById("cep").value.trim(),
            rua: document.getElementById("rua").value.trim(),
            numero: document.getElementById("numero").value.trim(),
            complemento: document.getElementById("complemento").value.trim(),
            bairro: document.getElementById("bairro").value.trim(),
            cidade: document.getElementById("cidade").value.trim(),
            estado: document.getElementById("estado").value.trim(),
        };
        localStorage.setItem("dadosEndereco", JSON.stringify(dados));
    }

    function carregarDadosPessoaFisica() {
        const dados = JSON.parse(localStorage.getItem("dadosPessoaFisica"));
        if (dados) {
            document.getElementById("firstname").value = dados.nome;
            document.getElementById("lastname").value = dados.sobrenome;
            document.getElementById("cpf").value = dados.cpf;
            document.getElementById("birthdate").value = dados.dataNascimento;
            document.getElementById("email").value = dados.email;
            document.getElementById("telefone").value = dados.telefone;
            document.getElementById("password").value = dados.senha;
            document.getElementById("confirm-password").value = dados.confirmarSenha;
        }
    }

    function carregarDadosEndereco() {
        const dados = JSON.parse(localStorage.getItem("dadosEndereco"));
        if (dados) {
            document.getElementById("cep").value = dados.cep;
            document.getElementById("rua").value = dados.rua;
            document.getElementById("numero").value = dados.numero;
            document.getElementById("complemento").value = dados.complemento;
            document.getElementById("bairro").value = dados.bairro;
            document.getElementById("cidade").value = dados.cidade;
            document.getElementById("estado").value = dados.estado;
        }
    }

    botaoContinuar.addEventListener('click', function (event) {
        event.preventDefault();

        if (!naTelaEndereco) {
            const nome = document.getElementById("firstname").value.trim();
            const sobrenome = document.getElementById("lastname").value.trim();
            const cpf = document.getElementById("cpf").value.trim();
            const dataNascimento = document.getElementById("birthdate").value.trim();
            const email = document.getElementById("email").value.trim();
            const telefone = document.getElementById("telefone").value.trim();
            const senha = document.getElementById("password").value.trim();
            const confirmarSenha = document.getElementById("confirm-password").value.trim();

            if (!nome || !sobrenome || !cpf || !dataNascimento || !email || !telefone || !senha || !confirmarSenha) {
                alert("Por favor, preencha todos os campos obrigatórios.");
                return;
            }

            if (!validarCPF(cpf)) {
                alert("Por favor, insira um CPF válido.");
                return;
            }

            if (!validarEmail(email)) {
                alert("Por favor, insira um e-mail válido.");
                return;
            }

            if (senha !== confirmarSenha) {
                alert("As senhas não coincidem. Por favor, digite as senhas iguais.");
                return;
            }

            if (!validarSenhaForte(senha)) {
                exibirAvisoSenhaFraca();
                return;
            } else {
                removerAvisoSenhaFraca();
            }

            salvarDadosPessoaFisica();

            camposPessoaFisica.style.display = 'none';
            camposEndereco.style.display = 'block';
            naTelaEndereco = true;
            botaoContinuar.textContent = "Finalizar Cadastro";
            botaoVoltar.style.display = 'inline-block';

            carregarDadosEndereco();
        } else {
            if (!validarEndereco()) {
                return;
            }

            salvarDadosEndereco();

            alert("Cadastro finalizado com sucesso!");

            setTimeout(() => {
                window.location.href = "tela-de-login.html";
            }, 1000);
        }
    });

    botaoVoltar.addEventListener('click', function (event) {
        event.preventDefault();

        salvarDadosEndereco();

        camposPessoaFisica.style.display = 'block';
        camposEndereco.style.display = 'none';
        naTelaEndereco = false;
        botaoContinuar.textContent = "Continuar";
        botaoVoltar.style.display = 'none';

        carregarDadosPessoaFisica();
    });

    document.getElementById("cpf").addEventListener("input", function () {
        this.value = formatarCPF(this.value);
    });

    document.getElementById("firstname").addEventListener("input", function () {
        limitarCaracteres(this, 100);
    });

    document.getElementById("lastname").addEventListener("input", function () {
        limitarCaracteres(this, 100);
    });

    document.getElementById("email").addEventListener("input", function () {
        limitarCaracteres(this, 60);
    });

    botaoVoltar.style.display = 'none';

    carregarDadosPessoaFisica();

    document.querySelectorAll('.toggle-password').forEach(function(icon) {
        icon.addEventListener('click', function() {
            const fieldId = this.getAttribute('data-field');
            togglePasswordVisibility(fieldId);
        });
    });
});