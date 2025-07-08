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

async function obtenerCotizacionDolar() {
  try {
    const res = await fetch("https://dolarapi.com/v1/dolares/blue");
    if (!res.ok) throw new Error("Error al obtener cotización dólar");
    const data = await res.json();
    const venta = data.venta;
    if (venta && venta > 0) {
      return venta;
    } else {
      throw new Error("Cotización inválida");
    }
  } catch (e) {
    console.warn("No se pudo obtener la cotización dólar:", e);
    return null;
  }
}

async function actualizarResumenPago() {
  const pagoEnDolaresCheckbox = document.getElementById('pagoEnDolares');
  const cuotasSelect = document.getElementById('cuotas');
  const totalResumenSpan = document.getElementById('totalResumen');
  const detalleCuotasSpan = document.getElementById('detalleCuotas');
  const tipoEnvioSelect = document.getElementById('tipoEnvio');
  const detalleEnvioSpan = document.getElementById('detalleEnvio');

  const precioInput = document.querySelector('input[name="precio"]');
  const metrosInput = document.querySelector('input[name="metros"]');
  const metodoPago = document.querySelector('input[name="metodoPago"]:checked')?.value || "debito";
  const cuotas = metodoPago === "credito" ? parseInt(cuotasSelect.value) : 1;

  if (!precioInput || !metrosInput) return;

  const precio = parseFloat(precioInput.value);
  const metros = parseFloat(metrosInput.value);
  if (isNaN(precio) || isNaN(metros)) {
    totalResumenSpan.textContent = "";
    detalleCuotasSpan.textContent = "";
    detalleEnvioSpan.textContent = "";
    return;
  }

  const costosEnvio = {
    LOCAL: { costo: 0, descripcion: "Retiro en local", tiempo: "1 a 2 días" },
    CABA: { costo: 1000, descripcion: "Envío a CABA", tiempo: "2 a 4 días hábiles" },
    INTERIOR: { costo: 2000, descripcion: "Envío al interior", tiempo: "4 a 7 días hábiles" }
  };

  const tipoEnvio = tipoEnvioSelect?.value || "LOCAL";
  let costoEnvioARS = costosEnvio[tipoEnvio]?.costo || 0;
  let descripcionEnvio = costosEnvio[tipoEnvio]?.descripcion || "";
  let tiempoEnvio = costosEnvio[tipoEnvio]?.tiempo || "";

  // Calcular interés según cuotas
  let interes = 0;
  switch (cuotas) {
    case 2: interes = 0.05; break;
    case 3: interes = 0.08; break;
    case 6: interes = 0.12; break;
    case 12: interes = 0.20; break;
  }

  // Calcular subtotal y total en pesos
  const subtotalARS = precio * metros;
  const totalConEnvioARS = subtotalARS + costoEnvioARS;
  const totalARS = totalConEnvioARS * (1 + interes);
  const valorCuotaARS = totalARS / cuotas;

  // Si paga en dólares, convertir todos los valores
  if (pagoEnDolaresCheckbox.checked) {
    const cotizacionDolar = await obtenerCotizacionDolar();

    if (cotizacionDolar) {
      // Convertir todos los montos a USD
      const precioUSD = precio / cotizacionDolar;
      const costoEnvioUSD = costoEnvioARS / cotizacionDolar;
      const subtotalUSD = subtotalARS / cotizacionDolar;
      const totalConEnvioUSD = subtotalUSD + costoEnvioUSD;
      const totalUSD = totalConEnvioUSD * (1 + interes);
      const valorCuotaUSD = totalUSD / cuotas;

      // Mostrar todo en dólares con formato correcto
      totalResumenSpan.textContent = `Total: U$D ${totalUSD.toFixed(2)}`;
      detalleCuotasSpan.textContent = cuotas > 1
        ? `${cuotas} cuotas de U$D ${valorCuotaUSD.toFixed(2)} cada una`
        : "Pago único en U$D";

      detalleEnvioSpan.textContent = `${descripcionEnvio} (U$D ${costoEnvioUSD.toFixed(2)}) - ${tiempoEnvio}`;
    } else {
      // Si no se pudo obtener cotización, mostrar en pesos y un aviso
      totalResumenSpan.textContent = `Total: $${totalARS.toFixed(2)} (No se pudo convertir a USD)`;
      detalleCuotasSpan.textContent = cuotas > 1
        ? `${cuotas} cuotas de $${valorCuotaARS.toFixed(2)} cada una`
        : "Pago único en pesos";

      detalleEnvioSpan.textContent = `${descripcionEnvio} ($${costoEnvioARS.toFixed(2)}) - ${tiempoEnvio}`;
    }
  } else {
    // Pago en pesos: mostrar todo en pesos
    totalResumenSpan.textContent = `Total: $${totalARS.toFixed(2)}`;
    detalleCuotasSpan.textContent = cuotas > 1
      ? `${cuotas} cuotas de $${valorCuotaARS.toFixed(2)} cada una`
      : "Pago único en pesos";

    detalleEnvioSpan.textContent = `${descripcionEnvio} ($${costoEnvioARS.toFixed(2)}) - ${tiempoEnvio}`;
  }
}

// Registrar eventos para actualizar resumen cada vez que cambia algo
document.addEventListener("DOMContentLoaded", () => {
  const pagoEnDolaresCheckbox = document.getElementById('pagoEnDolares');
  const cuotasSelect = document.getElementById('cuotas');
  const tipoEnvioSelect = document.getElementById('tipoEnvio');
  const metodoPagoRadios = document.querySelectorAll('input[name="metodoPago"]');

  // Actualizar cuando cambie método de pago, cuotas, envío o checkbox dólares
  pagoEnDolaresCheckbox?.addEventListener('change', actualizarResumenPago);
  cuotasSelect?.addEventListener('change', actualizarResumenPago);
  tipoEnvioSelect?.addEventListener('change', actualizarResumenPago);
  metodoPagoRadios.forEach(radio => radio.addEventListener('change', actualizarResumenPago));

  // También cuando cambie precio o metros
  const precioInput = document.querySelector('input[name="precio"]');
  const metrosInput = document.querySelector('input[name="metros"]');
  precioInput?.addEventListener('input', actualizarResumenPago);
  metrosInput?.addEventListener('input', actualizarResumenPago);

  // Ejecutar una vez al cargar
  actualizarResumenPago();
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
  const pagoEnDolaresCheckbox = document.getElementById("pagoEnDolares");
  const tipoCambioDolarDiv = document.getElementById("tipoCambioDolar");
  const valorDolarSpan = document.getElementById("valorDolar");
  const totalResumenDiv = document.getElementById("resumenPago");
  const totalResumenSpan = document.getElementById("totalResumen");
  const detalleCuotasSpan = document.getElementById("detalleCuotas");
  const cuotasSelect = document.getElementById("cuotas");

  // Obtener valores ocultos de la tela
  const precioInput = document.querySelector('input[name="precio"]');
  const metrosInput = document.querySelector('input[name="metros"]');

  let tipoCambio = null;

  function actualizarFormulario() {
    if (debitoRadio.checked) {
      tarjetaForm.style.display = "block";
      cuotasContainer.style.display = "none";
      cuotasSelect.value = "1";
    } else if (creditoRadio.checked) {
      tarjetaForm.style.display = "block";
      cuotasContainer.style.display = "block";
    } else {
      tarjetaForm.style.display = "none";
      cuotasContainer.style.display = "none";
      cuotasSelect.value = "1";
    }
    actualizarResumenPago();
  }

  async function obtenerTipoCambio() {
    try {
      const res = await fetch("https://dolarapi.com/v1/dolares/blue");
      if (!res.ok) throw new Error("Error al obtener cotización dólar");
      const data = await res.json();
      const venta = data.venta;
      if (venta && venta > 0) {
        tipoCambio = venta;
        valorDolarSpan.textContent = venta.toFixed(2);
        tipoCambioDolarDiv.style.display = "block";
      } else {
        tipoCambio = null;
        tipoCambioDolarDiv.style.display = "none";
      }
    } catch {
      tipoCambio = null;
      tipoCambioDolarDiv.style.display = "none";
    }
  }

  function actualizarResumenPago() {
    // Validar que precio y metros sean números válidos
    const precio = parseFloat(precioInput.value);
    const metros = parseFloat(metrosInput.value);
    if (isNaN(precio) || isNaN(metros)) {
      totalResumenDiv.style.display = "none";
      return;
    }

    const cuotas = parseInt(cuotasSelect.value) || 1;

    // Costo de envío estimado según selección
    const tipoEnvioSelect = document.getElementById("tipoEnvio");
    let costoEnvio = 0;
    if (tipoEnvioSelect) {
      switch (tipoEnvioSelect.value) {
        case "LOCAL":
          costoEnvio = 0; break;
        case "CABA":
          costoEnvio = 1000; break;
        case "INTERIOR":
          costoEnvio = 2000; break;
        default:
          costoEnvio = 0;
      }
    }

    // Intereses según cuotas
    let interes = 0;
    switch (cuotas) {
      case 2: interes = 0.05; break;
      case 3: interes = 0.08; break;
      case 6: interes = 0.12; break;
      case 12: interes = 0.20; break;
      default: interes = 0.0;
    }

    let subtotal = precio * metros;
    let totalConEnvio = subtotal + costoEnvio;
    let total = totalConEnvio * (1 + interes);
    let valorCuota = total / cuotas;

    if (pagoEnDolaresCheckbox.checked && tipoCambio) {
      // Mostrar en dólares
      const totalUsd = total / tipoCambio;
      totalResumenSpan.textContent = `U$D ${totalUsd.toFixed(2)}`;
      if (cuotas > 1) {
        const cuotaUsd = valorCuota / tipoCambio;
        detalleCuotasSpan.textContent = `${cuotas} cuotas de U$D ${cuotaUsd.toFixed(2)} cada una`;
      } else {
        detalleCuotasSpan.textContent = "";
      }
    } else {
      // Mostrar en pesos
      totalResumenSpan.textContent = `$ ${total.toFixed(2)}`;
      if (cuotas > 1) {
        detalleCuotasSpan.textContent = `${cuotas} cuotas de $${valorCuota.toFixed(2)} cada una`;
      } else {
        detalleCuotasSpan.textContent = "";
      }
    }

    totalResumenDiv.style.display = "block";
  }

  // Escuchar cambios
  debitoRadio.addEventListener("change", actualizarFormulario);
  creditoRadio.addEventListener("change", actualizarFormulario);
  cuotasSelect.addEventListener("change", actualizarResumenPago);
  pagoEnDolaresCheckbox.addEventListener("change", () => {
    if (pagoEnDolaresCheckbox.checked) {
      obtenerTipoCambio().then(actualizarResumenPago);
    } else {
      tipoCambioDolarDiv.style.display = "none";
      tipoCambio = null;
      actualizarResumenPago();
    }
  });

  // También actualizar cuando cambia tipoEnvio para actualizar costo envío
  const tipoEnvioSelect = document.getElementById("tipoEnvio");
  if (tipoEnvioSelect) {
    tipoEnvioSelect.addEventListener("change", actualizarResumenPago);
  }

  // Ejecutar al cargar la página por si ya hay selección
  actualizarFormulario();

  // Si ya estaba chequeado pagoEnDolares al cargar
  if (pagoEnDolaresCheckbox.checked) {
    obtenerTipoCambio().then(actualizarResumenPago);
  }
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

document.addEventListener('DOMContentLoaded', () => {
  // === BLOQUE 1 - Catálogo dinámico ===
  const todas = window.todasLasTelas || [];
  const cont = document.getElementById("contenedorTelas");
  const btn = document.getElementById("btnVerMas");
  const filtroTipo = document.getElementById("filtroTipoTela");
  const filtroColor = document.getElementById("filtroColor");
  const ordenPrecio = document.getElementById("ordenarPorPrecio");
  const ordenStock = document.getElementById("ordenarPorStock");

  if (cont && btn && filtroTipo && filtroColor && ordenPrecio && ordenStock) {
    let mostrados = 6;
    let filtradas = [...todas];

    function renderizar() {
      cont.innerHTML = "";

      if (filtradas.length === 0) {
        cont.innerHTML = `<div class="col-12 text-center text-danger fs-5">No se encontraron telas con esos filtros.</div>`;
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

      btn.style.display = filtradas.length > 6 ? "inline-block" : "none";
      btn.textContent = mostrados >= filtradas.length ? "Ver menos" : "Ver más";
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
        filtradas.sort((a, b) => ordenP === "precio-desc" ? b.precio - a.precio : a.precio - b.precio);
      } else if (ordenS) {
        filtradas.sort((a, b) => ordenS === "desc" ? b.metros - a.metros : a.metros - b.metros);
      }

      mostrados = 6;
      renderizar();
    }

    btn.addEventListener("click", () => {
      mostrados = mostrados >= filtradas.length ? 6 : mostrados + 6;
      renderizar();
    });

    filtroTipo.addEventListener("change", filtrar);
    filtroColor.addEventListener("change", filtrar);
    ordenPrecio.addEventListener("change", filtrar);
    ordenStock.addEventListener("change", filtrar);

    filtrar();
  }

  // === BLOQUE 2 - Carrusel ===
  const inner = document.getElementById("carrusel-inner");
  const prev = document.getElementById("btn-prev");
  const next = document.getElementById("btn-next");

  if (inner && prev && next) {
    const items = inner.children;
    let currentIndex = 0;

    function getVisibleCount() {
      const container = inner.parentElement;
      if (!container || items.length === 0) return 1;
      const containerWidth = container.offsetWidth;
      const itemWidth = items[0].offsetWidth + 16;
      return Math.floor(containerWidth / itemWidth);
    }

    function updateCarousel() {
      const itemWidth = items[0]?.offsetWidth + 16 || 0;
      inner.style.transform = `translateX(-${currentIndex * itemWidth}px)`;
    }

    next.addEventListener("click", () => {
      const visibleCount = getVisibleCount();
      if (currentIndex < items.length - visibleCount) {
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
    updateCarousel();
  }

  // === BLOQUE 3 - Método de Pago ===
  const pagoEnDolaresCheckbox = document.getElementById('pagoEnDolares');
  const valorDolarSpan = document.getElementById('valorDolar');
  const tipoCambioDolarDiv = document.getElementById('tipoCambioDolar');
  const metodoPagoRadios = document.querySelectorAll('input[name="metodoPago"]');
  const formularioTarjeta = document.getElementById('formulario-tarjeta');
  const cuotasContainer = document.getElementById('cuotas-container');
  const cuotasSelect = document.getElementById('cuotas');
  const resumenPago = document.getElementById('resumenPago');
  const totalResumen = document.getElementById('totalResumen');
  const detalleCuotas = document.getElementById('detalleCuotas');

  const precioInput = document.querySelector('input[name="precio"]');
  const metrosInput = document.querySelector('input[name="metros"]');

  if (precioInput && metrosInput) {
    const precio = parseFloat(precioInput.value) || 0;
    const metros = parseFloat(metrosInput.value) || 0;
    let cotizacionDolar = 350; // reemplazable por Thymeleaf

    function actualizarResumen() {
      const metodoPago = document.querySelector('input[name="metodoPago"]:checked')?.value;
      const cuotas = parseInt(cuotasSelect?.value) || 1;

      if (!metodoPago) return;

      let interes = 0;
      switch (cuotas) {
        case 2: interes = 0.05; break;
        case 3: interes = 0.08; break;
        case 6: interes = 0.12; break;
        case 12: interes = 0.20; break;
      }

      const subtotal = precio * metros;
      const total = subtotal * (1 + interes);

      if (pagoEnDolaresCheckbox?.checked) {
        const totalUsd = total / cotizacionDolar;
        totalResumen.textContent = `$${total.toFixed(2)} ARS / U$D ${totalUsd.toFixed(2)}`;
      } else {
        totalResumen.textContent = `$${total.toFixed(2)} ARS`;
      }

      if (cuotas > 1) {
        const valorCuota = total / cuotas;
        detalleCuotas.textContent = `Pago en ${cuotas} cuotas de $${valorCuota.toFixed(2)} cada una`;
      } else {
        detalleCuotas.textContent = '';
      }

      resumenPago.style.display = 'block';
    }

    pagoEnDolaresCheckbox?.addEventListener('change', () => {
      tipoCambioDolarDiv.style.display = pagoEnDolaresCheckbox.checked ? 'block' : 'none';
      valorDolarSpan.textContent = cotizacionDolar.toFixed(2);
      actualizarResumen();
    });

    metodoPagoRadios.forEach(radio => {
      radio.addEventListener('change', () => {
        formularioTarjeta.style.display = 'block';
        cuotasContainer.style.display = radio.value === 'credito' ? 'block' : 'none';
        if (radio.value !== 'credito') cuotasSelect.value = '1';
        actualizarResumen();
      });
    });

    cuotasSelect?.addEventListener('change', actualizarResumen);

    // Estado inicial
    const metodoSeleccionado = document.querySelector('input[name="metodoPago"]:checked');
    if (metodoSeleccionado) {
      formularioTarjeta.style.display = 'block';
      if (metodoSeleccionado.value === 'credito') cuotasContainer.style.display = 'block';
    }

    if (pagoEnDolaresCheckbox?.checked) {
      tipoCambioDolarDiv.style.display = 'block';
      valorDolarSpan.textContent = cotizacionDolar.toFixed(2);
    }

    actualizarResumen();
  }
});