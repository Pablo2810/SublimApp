function suma(a, b) {
  return a + b
}

function multiplicar(a, b) {
  return a * b;
}

document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;
    const toggle = document.getElementById("modoOscuroToggle");

    // Leer el modo guardado en localStorage
    const modoGuardado = localStorage.getItem("modo-oscuro");

    // Si el modo guardado es "activado", aplicar modo oscuro
    if (modoGuardado === "activado") {
        body.classList.add("modo-oscuro");
    }

    // Agregar el evento de clic al botón
    if (toggle) {
        toggle.addEventListener("click", () => {
            // Alternar la clase
            body.classList.toggle("modo-oscuro");

            // Guardar el nuevo estado
            if (body.classList.contains("modo-oscuro")) {
                localStorage.setItem("modo-oscuro", "activado");
            } else {
                localStorage.setItem("modo-oscuro", "desactivado");
            }
        });
    }
});
