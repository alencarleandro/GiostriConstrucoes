import { renderFavorites } from './favorites-teladousuario.js';
import { renderOrders } from './orders-teladousuario.js';
import { renderProfile } from './profile-teladousuario.js';

// Page routing
function handleNavigation() {
  const currentPath = window.location.hash || '#home';
  
  document.querySelectorAll('.sidebar a').forEach(link => {
    link.classList.remove('active');
    if (link.getAttribute('href') === currentPath) {
      link.classList.add('active');
    }
  });

  switch (currentPath) {
    case '#orders':
      renderOrders();
      break;
    case '#profile':
      renderProfile();
      break;
    case '#favorites':
      renderFavorites();
      break;
    default:
      // Render home page
      break;
  }
}

// Initialize navigation
window.addEventListener('hashchange', handleNavigation);
window.addEventListener('load', handleNavigation);

// Keyboard shortcut for search
document.addEventListener('keydown', (e) => {
  if (e.key.toLowerCase() === 'g' && (e.metaKey || e.ctrlKey)) {
    e.preventDefault();
    document.querySelector('.search-bar input').focus();
  }
});

// Add active class to sidebar items on click
document.querySelectorAll('.sidebar a').forEach(item => {
  item.addEventListener('click', (e) => {
    document.querySelectorAll('.sidebar a').forEach(i => i.classList.remove('active'));
    item.classList.add('active');
  });
});

// Enhanced hover effects for cards
document.querySelectorAll('.shortcut-card').forEach(card => {
  card.addEventListener('mouseenter', () => {
    card.style.transform = 'translateY(-4px)';
    const icon = card.querySelector('i');
    icon.style.transform = 'scale(1.1)';
  });

  card.addEventListener('mouseleave', () => {
    card.style.transform = 'translateY(0)';
    const icon = card.querySelector('i');
    icon.style.transform = 'scale(1)';
  });

  card.addEventListener('click', () => {
    const href = card.getAttribute('data-href');
    if (href) {
      window.location.hash = href;
    }
  });
});

// Interactive navigation items
document.querySelectorAll('.nav-bar a, .departments-btn').forEach(item => {
  item.addEventListener('mouseenter', () => {
    item.style.transform = 'translateY(-2px)';
  });

  item.addEventListener('mouseleave', () => {
    item.style.transform = 'translateY(0)';
  });
});

// User profile interaction
const userProfile = document.querySelector('.user-profile');
userProfile.addEventListener('mouseenter', () => {
  userProfile.style.transform = 'scale(1.05)';
});

userProfile.addEventListener('mouseleave', () => {
  userProfile.style.transform = 'scale(1)';
});

// Mobile menu toggle
let isMobileMenuOpen = false;
const toggleMobileMenu = () => {
  const sidebar = document.querySelector('.sidebar');
  if (window.innerWidth <= 768) {
    sidebar.style.transform = isMobileMenuOpen ? 'translateX(0)' : 'translateX(-100%)';
    isMobileMenuOpen = !isMobileMenuOpen;
  }
};

// Responsive sidebar
window.addEventListener('resize', () => {
  const sidebar = document.querySelector('.sidebar');
  if (window.innerWidth > 768) {
    sidebar.style.transform = 'translateX(0)';
    isMobileMenuOpen = false;
  }
});

// Add ripple effect to buttons
document.querySelectorAll('button').forEach(button => {
  button.addEventListener('click', function(e) {
    const x = e.clientX - e.target.offsetLeft;
    const y = e.clientY - e.target.offsetTop;

    const ripples = document.createElement('span');
    ripples.style.left = x + 'px';
    ripples.style.top = y + 'px';
    this.appendChild(ripples);

    setTimeout(() => {
      ripples.remove();
    }, 1000);
  });
});