// Funciones matemáticas reutilizables
function suma(a, b) {
  return a + b;
}

function multiplicar(a, b) {
  return a * b;
}

// Modo oscuro con persistencia en localStorage
/*function inicializarModoOscuro() {
  const body = document.body;
  const toggle = document.getElementById("modoOscuroToggle");
  const modoGuardado = localStorage.getItem("modo-oscuro");

  if (modoGuardado === "activado") {
    body.classList.add("modo-oscuro");
  }

  if (toggle) {
    toggle.addEventListener("click", () => {
      body.classList.toggle("modo-oscuro");
      const estado = body.classList.contains("modo-oscuro") ? "activado" : "desactivado";
      localStorage.setItem("modo-oscuro", estado);
    });
  }
} */

// Cálculo dinámico del precio total
function calcularPrecio() {
  const precioInput = document.querySelector('input[name=precio]');
  const metrosInput = document.getElementById('metros');
  const totalElemento = document.getElementById('precioTotal');

  if (precioInput && metrosInput && totalElemento) {
    const precio = parseFloat(precioInput.value) || 0;
    const metros = parseFloat(metrosInput.value) || 0;
    const total = multiplicar(precio, metros);
    totalElemento.innerText = `Precio total: $${total.toFixed(2)}`;
  }
}

// Inicializar evento de cálculo dinámico
function inicializarCalculoPrecio() {
  const metrosInput = document.getElementById('metros');
  if (metrosInput) {
    metrosInput.addEventListener('input', calcularPrecio);
  }
}

// Mostrar automáticamente todos los toasts de Bootstrap al cargar
function mostrarToasts() {
  const toastElList = Array.from(document.querySelectorAll('.toast'));
  toastElList.forEach(toastEl => {
    try {
      const toast = new bootstrap.Toast(toastEl);
      toast.show();
    } catch (e) {
      console.warn("Toast falló:", e);
    }
  });
}

// Iniciar todas las funciones al cargar el DOM
document.addEventListener('DOMContentLoaded', () => {
  inicializarModoOscuro();
  inicializarCalculoPrecio();
  mostrarToasts();
});

document.addEventListener("DOMContentLoaded", function () {
    const metodoInputs = document.querySelectorAll('input[name="metodoPago"]');
    const cuotasContainer = document.getElementById('cuotas-container');

    if (!metodoInputs || !cuotasContainer) return;

    metodoInputs.forEach(input => {
        input.addEventListener('change', function () {
            if (this.value === 'credito') {
                cuotasContainer.style.display = 'block';
            } else {
                cuotasContainer.style.display = 'none';
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const debitoRadio = document.getElementById("debito");
    const creditoRadio = document.getElementById("credito");
    const tarjetaForm = document.getElementById("formulario-tarjeta");
    const cuotasContainer = document.getElementById("cuotas-container");

    function actualizarFormulario() {
        if (debitoRadio.checked) {
            tarjetaForm.style.display = "block";
            cuotasContainer.style.display = "none";
        } else if (creditoRadio.checked) {
            tarjetaForm.style.display = "block";
            cuotasContainer.style.display = "block";
        } else {
            tarjetaForm.style.display = "none";
            cuotasContainer.style.display = "none";
        }
    }

    debitoRadio.addEventListener("change", actualizarFormulario);
    creditoRadio.addEventListener("change", actualizarFormulario);

    // Ejecutar al cargar la página por si ya hay selección
    actualizarFormulario();
});

document.addEventListener("DOMContentLoaded", function () {
    const metodoDebito = document.getElementById("debito");
    const metodoCredito = document.getElementById("credito");
    const formularioTarjeta = document.getElementById("formulario-tarjeta");
    const cuotasContainer = document.getElementById("cuotas-container");
    const resumenPago = document.getElementById("resumenPago");
    const totalResumen = document.getElementById("totalResumen");
    const detalleCuotas = document.getElementById("detalleCuotas");

    const precio = parseFloat(document.querySelector('input[name="precio"]').value);
    const metros = parseFloat(document.querySelector('input[name="metros"]').value);

    const cuotasSelect = document.getElementById("cuotas");

    function actualizarResumen() {
        let metodo = metodoCredito.checked ? "credito" : "debito";
        let cuotas = metodo === "credito" ? parseInt(cuotasSelect.value) : 1;

        let total = precio * metros;
        let interes = 0;

        if (metodo === "credito") {
            if (cuotas === 2) interes = 0.05;
            else if (cuotas === 3) interes = 0.08;
            else if (cuotas === 6) interes = 0.12;
            else if (cuotas === 12) interes = 0.20;
        }

        let totalConInteres = total * (1 + interes);
        let valorCuota = totalConInteres / cuotas;

        resumenPago.style.display = "block";
        totalResumen.textContent = "$" + totalConInteres.toFixed(2);

        if (cuotas > 1) {
            detalleCuotas.textContent = cuotas + " cuotas de $" + valorCuota.toFixed(2) + " cada una.";
        } else {
            detalleCuotas.textContent = metodo === "credito"
                ? "Pago único con crédito sin cuotas."
                : "Pago único con débito.";
        }
    }

    metodoCredito.addEventListener("change", function () {
        formularioTarjeta.style.display = "block";
        cuotasContainer.style.display = "block";
        actualizarResumen();
    });

    metodoDebito.addEventListener("change", function () {
        formularioTarjeta.style.display = "block";
        cuotasContainer.style.display = "none";
        resumenPago.style.display = "block";

        let total = precio * metros;
        totalResumen.textContent = "$" + total.toFixed(2);
        detalleCuotas.textContent = "Pago único con débito.";
    });

    cuotasSelect.addEventListener("change", actualizarResumen);
});

function imprimirBoleta() {
  window.print();
}

function descargarBoletaPDF() {
  const boleta = document.getElementById("boleta");
  const opciones = {
    margin: 0.5,
    filename: "boleta-tela.pdf",
    image: { type: "jpeg", quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: "in", format: "letter", orientation: "portrait" }
  };
  html2pdf().set(opciones).from(boleta).save();
}

