document.addEventListener("DOMContentLoaded", function () {

  const botaoRegistrar = document.getElementById("botao-registrar");

  botaoRegistrar.addEventListener("click", function (event) {
    event.preventDefault();
    window.location.href = "../paginas/cadastro.html";
  });
});




