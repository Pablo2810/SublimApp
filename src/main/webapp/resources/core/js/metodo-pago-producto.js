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
  const precio = parseFloat(document.querySelector('input[name="precioUnitario"]')?.value);
  const cantidad = parseInt(document.querySelector('input[name="cantidad"]')?.value);
  const metodo = document.querySelector('input[name="metodoPago"]:checked')?.value;
  const enDolares = document.querySelector('input[name="pagoEnDolares"]:checked')?.value === "true";
  const cuotas = parseInt(document.getElementById("cuotas")?.value || "1");
  const resumen = document.getElementById("resumenPago");
  const detalleCuotas = document.getElementById("detalleCuotas");
  const totalResumen = document.getElementById("totalResumen");
  const tipoCambioDiv = document.getElementById("tipoCambioDolar");
  const valorDolarSpan = document.getElementById("valorDolar");

  if (isNaN(precio) || isNaN(cantidad) || !metodo) {
    resumen.style.display = "none";
    return;
  }

  const subtotal = precio * cantidad;
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

    if (metodo === "credito" && cuotas > 1) {
      const valorCuota = totalUSD / cuotas;
      detalleCuotas.textContent = `En ${cuotas} cuotas de USD ${valorCuota.toFixed(2)}.`;
    } else {
      detalleCuotas.textContent = "";
    }
  } else {
    tipoCambioDiv.style.display = "none";
    totalResumen.textContent = `$${total.toFixed(2)}`;

    if (metodo === "credito" && cuotas > 1) {
      const valorCuota = total / cuotas;
      detalleCuotas.textContent = `En ${cuotas} cuotas de $${valorCuota.toFixed(2)}.`;
    } else {
      detalleCuotas.textContent = "";
    }
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

  if (formularioTarjeta) formularioTarjeta.style.display = "block"; // siempre mostrar formulario tarjeta

  if (cuotasContainer) {
    if (metodo === "credito") {
      cuotasContainer.style.display = "block";
      cuotasSelect.setAttribute("required", "required");
    } else {
      cuotasContainer.style.display = "none";
      cuotasSelect.removeAttribute("required");
      cuotasSelect.value = "1";  // evita que llegue vacío
    }
  }

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
      sugerencias.style.display = 'none';
      return;
    }

    debounceTimeout = setTimeout(() => {
      fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&addressdetails=1&limit=5`)
        .then(res => res.json())
        .then(data => {
          sugerencias.innerHTML = '';
          if (data.length === 0) {
            sugerencias.style.display = 'none';
            return;
          }
          data.forEach(item => {
            const li = document.createElement('li');
            li.textContent = item.display_name;
            li.classList.add('list-group-item');
            li.addEventListener('click', () => {
              inputDireccion.value = item.display_name;
              jsonDireccionInput.value = JSON.stringify(item);
              sugerencias.innerHTML = '';
              sugerencias.style.display = 'none';

              // Llamar backend para determinar tipo de envío real y setearlo
              fetch('/calcular-envio-pedido', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: `jsonDireccion=${encodeURIComponent(JSON.stringify(item))}`
              })
              .then(res => res.json())
              .then(data => {
                if (data.tipo) {
                  tipoEnvioInput.value = data.tipo;

                  // Ajustar radio de envío para que coincida con el tipo obtenido
                  const envioRadio = document.querySelector(`input[name="opcionEnvio"][value="${data.tipo}"]`);
                  if (envioRadio) {
                    envioRadio.checked = true;
                    toggleCamposFormulario();
                    actualizarResumenPago();
                  }
                }
              })
              .catch(() => {
                tipoEnvioInput.value = 'CABA'; // fallback si falla
              });
            });
            sugerencias.appendChild(li);
          });
          sugerencias.style.display = 'block';
        })
        .catch(() => {
          sugerencias.innerHTML = '';
          sugerencias.style.display = 'none';
        });
    }, 300);
  });
}

// ------------------------------
// Validación antes de enviar el formulario
// ------------------------------
function validarFormularioAntesDeEnviar() {
  const form = document.querySelector('form');
  form.addEventListener('submit', e => {
    const envioSeleccionado = document.querySelector('input[name="opcionEnvio"]:checked')?.value;
    const direccion = document.getElementById('direccionEnvio')?.value.trim();

    if (envioSeleccionado && envioSeleccionado.toUpperCase() !== "LOCAL" && (!direccion || direccion.length < 3)) {
      e.preventDefault();
      alert('Por favor ingrese una dirección válida para el envío.');
    }
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
  const precioInput = document.querySelector('input[name="precioUnitario"]');
  const cantidadInput = document.querySelector('input[name="cantidad"]');

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
  cantidadInput?.addEventListener('input', actualizarResumenPago);

  toggleCamposFormulario();
  actualizarResumenPago();
  initAutocompleteDireccion();
  validarFormularioAntesDeEnviar();
});