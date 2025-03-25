// script-header.js
document.addEventListener("DOMContentLoaded", function() {
    fetch('../componentes/header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
        });
});