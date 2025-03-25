// script-sidebar.js
document.addEventListener("DOMContentLoaded", function() {
    fetch('../componentes/sidebar.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('sidebar').innerHTML = data;
        });
});