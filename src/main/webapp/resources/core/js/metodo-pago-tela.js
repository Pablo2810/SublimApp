// ------------------------------
// Obtener cotización dólar (desde variable global o default)
// ------------------------------
function obtenerCotizacionDolar() {
  if (window.datosPago && window.datosPago.cotizacionDolar) {
    return window.datosPago.cotizacionDolar;
  }
  return 1270;
}

// ------------------------------
// Actualizar resumen de pago en la vista
// ------------------------------
function actualizarResumenPago() {
  const precio = parseFloat(document.querySelector('input[name="precio"]')?.value);
  const metros = parseFloat(document.querySelector('input[name="metros"]')?.value);
  const metodo = document.querySelector('input[name="metodoPago"]:checked')?.value;
  const enDolares = document.querySelector('input[name="pagoEnDolares"]:checked')?.value === "true";
  const cuotas = parseInt(document.getElementById("cuotas")?.value || "1");
  const resumen = document.getElementById("resumenPago");
  const detalleCuotas = document.getElementById("detalleCuotas");
  const totalResumen = document.getElementById("totalResumen");
  const tipoCambioDiv = document.getElementById("tipoCambioDolar");
  const valorDolarSpan = document.getElementById("valorDolar");

  if (isNaN(precio) || isNaN(metros) || !metodo) {
    resumen.style.display = "none";
    return;
  }

  const subtotal = precio * metros;
  let total = subtotal;

  if (metodo === "credito") {
    if (cuotas === 2) total *= 1.05;
    else if (cuotas === 3) total *= 1.08;
    else if (cuotas === 6) total *= 1.12;
    else if (cuotas === 12) total *= 1.20;
  }

  if (enDolares) {
    const cotizacion = obtenerCotizacionDolar();
    const totalUSD = total / cotizacion;
    tipoCambioDiv.style.display = "block";
    valorDolarSpan.textContent = `$${cotizacion.toFixed(2)}`;
    totalResumen.textContent = `USD ${totalUSD.toFixed(2)}`;
  } else {
    tipoCambioDiv.style.display = "none";
    totalResumen.textContent = `$${total.toFixed(2)}`;
  }

  if (metodo === "credito" && cuotas > 1) {
    const valorCuota = total / cuotas;
    detalleCuotas.textContent = `En ${cuotas} cuotas de $${valorCuota.toFixed(2)}.`;
  } else {
    detalleCuotas.textContent = "";
  }

  resumen.style.display = "block";
}

// ------------------------------
// Mostrar/ocultar campos según método de pago y envío
// ------------------------------
function toggleCamposFormulario() {
  const metodo = document.querySelector('input[name="metodoPago"]:checked')?.value;
  const envioSeleccionado = document.querySelector('input[name="opcionEnvio"]:checked')?.value;

  const formularioTarjeta = document.getElementById("formulario-tarjeta");
  const cuotasContainer = document.getElementById("cuotas-container");
  const contenedorDireccion = document.getElementById("contenedorDireccion");
  const cuotasSelect = document.getElementById("cuotas");

  if (formularioTarjeta) formularioTarjeta.style.display = "block";
  if (cuotasContainer) {
    if (metodo === "credito") {
      cuotasContainer.style.display = "block";
      cuotasSelect.setAttribute("required", "required");
    } else {
      cuotasContainer.style.display = "none";
      cuotasSelect.removeAttribute("required");
      cuotasSelect.value = "1";  // <-- evita que llegue vacío
    }
  }

  // Mostrar contenedorDireccion SOLO si NO es retiro en local
  if (contenedorDireccion) {
    if (envioSeleccionado && envioSeleccionado.toUpperCase() !== "LOCAL") {
      contenedorDireccion.style.display = "block";
    } else {
      contenedorDireccion.style.display = "none";
    }
  }
}

// ------------------------------
// Autocomplete dirección con Nominatim OpenStreetMap
// ------------------------------
function initAutocompleteDireccion() {
  const inputDireccion = document.getElementById('direccionEnvio');
  const sugerencias = document.getElementById('sugerenciasDireccion');
  const jsonDireccionInput = document.getElementById('jsonDireccion');
  const tipoEnvioInput = document.getElementById('tipoEnvio');

  let debounceTimeout;

  if (!inputDireccion || !sugerencias) return;

  inputDireccion.addEventListener('input', () => {
    clearTimeout(debounceTimeout);
    const query = inputDireccion.value.trim();

    if (query.length < 3) {
      sugerencias.innerHTML = '';
      return;
    }

    debounceTimeout = setTimeout(() => {
      fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&addressdetails=1&limit=5`)
        .then(res => res.json())
        .then(data => {
          sugerencias.innerHTML = '';
          data.forEach(item => {
            const li = document.createElement('li');
            li.textContent = item.display_name;
            li.classList.add('list-group-item');
            li.addEventListener('click', () => {
              inputDireccion.value = item.display_name;
              jsonDireccionInput.value = JSON.stringify(item);
              tipoEnvioInput.value = 'ENVIO';
              sugerencias.innerHTML = '';
            });
            sugerencias.appendChild(li);
          });
        });
    }, 300);
  });
}

// ------------------------------
// Setup listeners y acciones en DOMContentLoaded
// ------------------------------
document.addEventListener("DOMContentLoaded", () => {
  const cuotasSelect = document.getElementById('cuotas');
  const metodoPagoRadios = document.querySelectorAll('input[name="metodoPago"]');
  const pagoEnDolaresRadios = document.querySelectorAll('input[name="pagoEnDolares"]');
  const envioRadios = document.querySelectorAll('input[name="opcionEnvio"]');
  const precioInput = document.querySelector('input[name="precio"]');
  const metrosInput = document.querySelector('input[name="metros"]');

  cuotasSelect?.addEventListener('change', actualizarResumenPago);

  metodoPagoRadios.forEach(radio => {
    radio.addEventListener('change', () => {
      toggleCamposFormulario();
      actualizarResumenPago();
    });
  });

  pagoEnDolaresRadios.forEach(radio => {
    radio.addEventListener('change', actualizarResumenPago);
  });

  envioRadios.forEach(radio => {
    radio.addEventListener('change', () => {
      toggleCamposFormulario();
      actualizarResumenPago();
    });
  });

  precioInput?.addEventListener('input', actualizarResumenPago);
  metrosInput?.addEventListener('input', actualizarResumenPago);

  toggleCamposFormulario();
  actualizarResumenPago();
  initAutocompleteDireccion();
});