function suma(a, b) {
  return a + b
}

function multiplicar(a, b) {
  return a * b;
}

document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;
    const toggle = document.getElementById("modoOscuroToggle");

    const modoGuardado = localStorage.getItem("modo-oscuro");

    if (modoGuardado === "activado") {
        body.classList.add("modo-oscuro");
    }

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

 function calcularPrecio() {
     const precio = parseFloat(document.querySelector('input[name=precio]').value);
     const metros = parseFloat(document.getElementById('metros')?.value || 0);
     const totalElemento = document.getElementById('precioTotal');

     if (totalElemento) {
         totalElemento.innerText = 'Precio total: $' + (precio * metros).toFixed(2);
     }
 }

 document.addEventListener('DOMContentLoaded', () => {
     const metrosInput = document.getElementById('metros');
     if (metrosInput) {
         metrosInput.addEventListener('input', calcularPrecio);
     }
 });




