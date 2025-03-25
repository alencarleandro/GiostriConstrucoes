document.addEventListener("DOMContentLoaded", function () {

  const botaoRegistrar = document.getElementById("botao-registrar");

  botaoRegistrar.addEventListener("click", function (event) {
    event.preventDefault();
    window.location.href = "../paginas/cadastro.html";
  });
});

const togglePassword = document.querySelector('#togglePassword');
const senha = document.querySelector('#senha');

togglePassword.addEventListener('click', function () {
  const type = senha.getAttribute('type') === 'password' ? 'text' : 'password';
  senha.setAttribute('type', type);

  this.classList.toggle('fa-eye-slash');
});