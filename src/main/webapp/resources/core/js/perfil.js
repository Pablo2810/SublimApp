// ------------------------------
// Validación bootstrap de formulario perfil
// ------------------------------
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formulario-perfil");
  if (!form) return;

  form.addEventListener("submit", function (event) {
    form.classList.remove("was-validated");
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
      form.classList.add("was-validated");
    }
  }, false);
});
