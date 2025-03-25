function alternarMenuMobile() {
  const listaNavegacao = document.getElementById('listaNavegacao');
  listaNavegacao.classList.toggle('ativo');
  
  const spans = document.querySelectorAll('.botao-menu-mobile span');
  if (listaNavegacao.classList.contains('ativo')) {
      spans[0].style.transform = 'rotate(45deg) translate(6px, 6px)';
      spans[1].style.opacity = '0';
      spans[2].style.transform = 'rotate(-45deg) translate(6px, -6px)';
  } else {
      spans[0].style.transform = 'none';
      spans[1].style.opacity = '1';
      spans[2].style.transform = 'none';
  }
}

const botaoMenuMobile = document.getElementById('botaoMenuMobile');
if (botaoMenuMobile) {
  botaoMenuMobile.addEventListener('click', alternarMenuMobile);
}

const linksMenu = document.querySelectorAll('.lista-navegacao a');
linksMenu.forEach(link => {
  link.addEventListener('click', () => {
      const listaNavegacao = document.getElementById('listaNavegacao');
      if (listaNavegacao.classList.contains('ativo')) {
          alternarMenuMobile();
      }
  });
});

document.getElementById("botaoCadastrar").addEventListener("click", function() {
  window.location.href = "paginas/cadastro.html"; 
});

document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener('click', function(e) {
      e.preventDefault();
      
      const targetId = this.getAttribute('href');
      const targetElement = document.querySelector(targetId);
      
      if (targetElement) {
          const cabecalhoAltura = document.querySelector('.cabecalho').offsetHeight;
          const targetPosition = targetElement.getBoundingClientRect().top + window.pageYOffset - cabecalhoAltura;
          
          window.scrollTo({
              top: targetPosition,
              behavior: 'smooth'
          });
      }
  });
});

function animarElementosAoRolar() {
  const elementos = document.querySelectorAll('.card, .card-depoimento');
  
  elementos.forEach(elemento => {
      const posicaoElemento = elemento.getBoundingClientRect().top;
      const alturaTela = window.innerHeight;
      
      if (posicaoElemento < alturaTela - 100) {
          elemento.style.opacity = '1';
          elemento.style.transform = 'translateY(0)';
      }
  });
}

document.addEventListener('DOMContentLoaded', () => {
  const elementos = document.querySelectorAll('.card, .card-depoimento');
  
  elementos.forEach(elemento => {
      elemento.style.opacity = '0';
      elemento.style.transform = 'translateY(20px)';
      elemento.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
  });
  
  animarElementosAoRolar();
});

window.addEventListener('scroll', animarElementosAoRolar);
