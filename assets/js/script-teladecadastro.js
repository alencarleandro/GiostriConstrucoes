document.addEventListener('DOMContentLoaded', function () {
  const pessoaFisicaRadio = document.getElementById('pessoa-fisica');
  const pessoaJuridicaRadio = document.getElementById('pessoa-juridica');
  const camposPessoaFisica = document.getElementById('campos-pessoa-fisica');
  const camposPessoaJuridica = document.getElementById('campos-pessoa-juridica');

  pessoaFisicaRadio.addEventListener('change', function () {
      if (this.checked) {
          camposPessoaFisica.style.display = 'block';
          camposPessoaJuridica.style.display = 'none';
      }
  });

  pessoaJuridicaRadio.addEventListener('change', function () {
      if (this.checked) {
          camposPessoaFisica.style.display = 'none';
          camposPessoaJuridica.style.display = 'block';
      }
  });
});

const botaoEntrar = document.getElementById('botao-entrar');

botaoEntrar.addEventListener('click', (event) =>{
    event.preventDefault();
    window.location.href = "../paginas/login.html";
})
