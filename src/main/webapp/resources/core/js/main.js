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

const costosEnvio = {
  LOCAL: { costo: 0, texto: "Retiro en local (gratis) - Retiro en 1 a 2 días" },
  CABA: { costo: 1000, texto: "Envío a CABA ($1000) - 2 a 4 días hábiles" },
  INTERIOR: { costo: 2000, texto: "Envío al interior ($2000) - 4 a 7 días hábiles" }
};

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
  const tipoEnvioSelect = document.getElementById("tipoEnvio");
  const detalleEnvio = document.getElementById("detalleEnvio");

  const precio = parseFloat(document.querySelector('input[name="precio"]').value);
  const metros = parseFloat(document.querySelector('input[name="metros"]').value);

  const cuotasSelect = document.getElementById("cuotas");

  function actualizarResumen() {
    const envioSeleccionado = tipoEnvioSelect ? tipoEnvioSelect.value : "LOCAL";

    let metodo = metodoCredito.checked ? "credito" : "debito";
    let cuotas = metodo === "credito" ? parseInt(cuotasSelect.value) : 1;

    let costoEnvio = costosEnvio[envioSeleccionado]?.costo || 0;
    let textoEnvio = costosEnvio[envioSeleccionado]?.texto || "";

    let total = precio * metros + costoEnvio;
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
    detalleCuotas.textContent = cuotas > 1
      ? `${cuotas} cuotas de $${valorCuota.toFixed(2)} cada una.`
      : metodo === "credito"
        ? "Pago único con crédito sin cuotas."
        : "Pago único con débito.";

    if (detalleEnvio) {
      detalleEnvio.textContent = textoEnvio;
    }
  }

  if (tipoEnvioSelect) {
    tipoEnvioSelect.addEventListener("change", actualizarResumen);
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

    const envioSeleccionado = tipoEnvioSelect ? tipoEnvioSelect.value : "LOCAL";
    let costoEnvio = costosEnvio[envioSeleccionado]?.costo || 0;

    let total = precio * metros + costoEnvio;
    totalResumen.textContent = "$" + total.toFixed(2);
    detalleCuotas.textContent = "Pago único con débito.";

    if (detalleEnvio) {
      detalleEnvio.textContent = costosEnvio[envioSeleccionado]?.texto || "";
    }
  });

  cuotasSelect.addEventListener("change", actualizarResumen);

  // Ejecutar al cargar la página por si ya hay selección
  actualizarResumen();
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

// perfil-usuario.js
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formulario-perfil");
  form.addEventListener("submit", function (event) {
    // Resetea clases de validación
    form.classList.remove("was-validated");

    // Validación manual simple
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
      form.classList.add("was-validated");
    }
  }, false);
});

document.addEventListener("DOMContentLoaded", () => {
  const todas = window.todasLasTelas || [];
  const cont = document.getElementById("contenedorTelas");
  const btn = document.getElementById("btnVerMas");
  const filtroTipo = document.getElementById("filtroTipoTela");
  const filtroColor = document.getElementById("filtroColor");
  const ordenPrecio = document.getElementById("ordenarPorPrecio");
  const ordenStock = document.getElementById("ordenarPorStock");

  let mostrados = 6;
  let filtradas = [...todas];

  function renderizar() {
    cont.innerHTML = "";

    if (filtradas.length === 0) {
      cont.innerHTML = `
        <div class="col-12 text-center text-danger fs-5">
          No se encontraron telas con esos filtros.
        </div>`;
      btn.style.display = "none";
      return;
    }

    const visibles = filtradas.slice(0, mostrados);
    visibles.forEach(t => {
      const sinStock = t.metros <= 0;
      const card = document.createElement("div");
      card.className = "col-md-4 col-sm-6 animate";
      card.innerHTML = `
        <div class="card shadow h-100 border border-light${sinStock ? " sin-stock" : ""}">
          <img src="${t.imagenUrl}" class="card-img-top" style="height:200px;object-fit:cover;" />
          <div class="card-body d-flex flex-column justify-content-between">
            <div>
              <h5 class="card-title">${t.tipoTela}</h5>
              <p><strong>Color:</strong> ${t.color}</p>
              <p><strong>Precio:</strong> $${t.precio}</p>
              <p><strong>Stock:</strong> ${t.metros.toFixed(2)}</p>
            </div>
            ${
              sinStock
                ? `<button class="btn btn-secondary mt-3" disabled>Sin stock</button>`
                : `<a href="/detalle-tela-id/${t.id}" class="btn btn-primary mt-3">Comprar</a>`
            }
          </div>
        </div>
      `;
      cont.appendChild(card);
    });

    if (filtradas.length > 6) {
      btn.style.display = "inline-block";
      btn.textContent = mostrados >= filtradas.length ? "Ver menos" : "Ver más";
    } else {
      btn.style.display = "none";
    }
  }

  function filtrar() {
    const tipo = filtroTipo.value;
    const color = filtroColor.value;
    const ordenP = ordenPrecio.value;
    const ordenS = ordenStock.value;

    filtradas = todas.filter(t =>
      (tipo === "" || t.tipoTela === tipo) &&
      (color === "" || t.color === color)
    );

    if (ordenP) {
      if (ordenP === "precio-desc") {
        filtradas.sort((a, b) => b.precio - a.precio);
      } else if (ordenP === "precio-asc") {
        filtradas.sort((a, b) => a.precio - b.precio);
      }
    } else if (ordenS) {
      if (ordenS === "desc") {
        filtradas.sort((a, b) => b.metros - a.metros);
      } else if (ordenS === "asc") {
        filtradas.sort((a, b) => a.metros - b.metros);
      }
    }

    mostrados = 6;
    renderizar();
  }

  btn.addEventListener("click", () => {
    if (mostrados >= filtradas.length) {
      mostrados = 6;
    } else {
      mostrados += 6;
    }
    renderizar();
  });

  filtroTipo.addEventListener("change", filtrar);
  filtroColor.addEventListener("change", filtrar);
  ordenPrecio.addEventListener("change", filtrar);
  ordenStock.addEventListener("change", filtrar);

  filtrar();
});

document.addEventListener("DOMContentLoaded", function () {
        const inner = document.getElementById("carrusel-inner");
        const items = inner.children;
        const prev = document.getElementById("btn-prev");
        const next = document.getElementById("btn-next");

        let currentIndex = 0;

        function updateCarousel() {
            const itemWidth = items[0].offsetWidth + 16; // ancho + margin
            inner.style.transform = `translateX(-${currentIndex * itemWidth}px)`;
        }

        next.addEventListener("click", () => {
            if (currentIndex < items.length - 3) {
                currentIndex++;
                updateCarousel();
            }
        });

        prev.addEventListener("click", () => {
            if (currentIndex > 0) {
                currentIndex--;
                updateCarousel();
            }
        });

        window.addEventListener("resize", updateCarousel);
});