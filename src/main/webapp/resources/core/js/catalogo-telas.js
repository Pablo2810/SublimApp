// ------------------------------
// Variables y paginación catálogo
// ------------------------------
const telasPorPagina = 6;
let telasMostradas = 0;

// ------------------------------
// Filtrar y ordenar telas según filtros seleccionados
// ------------------------------
function filtrarYOrdenarTelas() {
  let telasFiltradas = window.todasLasTelas || [];

  const filtroTipo = document.getElementById('filtroTipoTela')?.value || "";
  const filtroColor = document.getElementById('filtroColor')?.value || "";
  const ordenPrecio = document.getElementById('ordenarPorPrecio')?.value || "";
  const ordenStock = document.getElementById('ordenarPorStock')?.value || "";

  if (filtroTipo) {
    telasFiltradas = telasFiltradas.filter(t => t.tipoTela.toUpperCase() === filtroTipo.toUpperCase());
  }

  if (filtroColor) {
    telasFiltradas = telasFiltradas.filter(t => t.color.toLowerCase() === filtroColor.toLowerCase());
  }

  if (ordenPrecio === "precio-asc") {
    telasFiltradas.sort((a, b) => a.precio - b.precio);
  } else if (ordenPrecio === "precio-desc") {
    telasFiltradas.sort((a, b) => b.precio - a.precio);
  }

  if (ordenStock === "asc") {
    telasFiltradas.sort((a, b) => a.metros - b.metros);
  } else if (ordenStock === "desc") {
    telasFiltradas.sort((a, b) => b.metros - a.metros);
  }

  return telasFiltradas;
}

// ------------------------------
// Mostrar telas en el contenedor con paginación
// ------------------------------
function mostrarTelas() {
  const contenedor = document.getElementById('contenedorTelas');
  if (!contenedor) return;

  const telasFiltradas = filtrarYOrdenarTelas();

  const telasParaMostrar = telasFiltradas.slice(0, telasMostradas + telasPorPagina);
  telasMostradas = telasParaMostrar.length;

  contenedor.innerHTML = "";

  telasParaMostrar.forEach(tela => {
    const div = document.createElement('div');
    div.className = "col-6 col-md-4 col-lg-3";

    div.innerHTML = `
      <div class="card h-100 shadow-sm border border-light ${tela.metros === 0 ? "sin-stock" : ""}">
        <img src="${tela.imagenUrl}" class="card-img-top" style="height: 160px; object-fit: cover;" alt="Imagen tela" />
        <div class="card-body text-center d-flex flex-column justify-content-between">
          <div>
            <h6 class="fw-bold text-primary">${tela.tipoTela} - ${tela.color}</h6>
            <p>$${tela.precio}</p>
            ${tela.metros === 0
              ? '<p class="text-danger fw-bold">Sin stock</p>'
              : `<p class="text-success">Stock: <span>${tela.metros}</span> mts</p>`
            }
          </div>
          <div>
            ${tela.metros > 0
              ? `<a href="/detalle-tela-id/${tela.id}" class="btn btn-success btn-sm mt-2">Comprar</a>`
              : `<button class="btn btn-secondary btn-sm mt-2" disabled>Sin stock</button>`
            }
          </div>
        </div>
      </div>
    `;

    contenedor.appendChild(div);
  });

  const btnVerMas = document.getElementById('btnVerMas');
  if (!btnVerMas) return;

  if (telasMostradas >= filtrarYOrdenarTelas().length) {
    btnVerMas.style.display = "none";
  } else {
    btnVerMas.style.display = "inline-block";
  }
}

// ------------------------------
// Carrusel personalizado
// ------------------------------
function initCarrusel() {
  const carruselInner = document.getElementById('carrusel-inner');
  const btnPrev = document.getElementById('btn-prev');
  const btnNext = document.getElementById('btn-next');
  if (!carruselInner || !btnPrev || !btnNext) return;

  let pos = 0;
  const maxPos = carruselInner.children.length - 1;

  btnPrev.addEventListener('click', () => {
    if (pos > 0) {
      pos--;
      carruselInner.style.transform = `translateX(-${pos * 34}%)`;
    }
  });

  btnNext.addEventListener('click', () => {
    if (pos < maxPos - 2) {
      pos++;
      carruselInner.style.transform = `translateX(-${pos * 34}%)`;
    }
  });
}

// ------------------------------
// Setup listeners para filtros y botones
// ------------------------------
document.addEventListener("DOMContentLoaded", () => {
  const filtroTipoTela = document.getElementById('filtroTipoTela');
  const filtroColor = document.getElementById('filtroColor');
  const ordenarPorPrecio = document.getElementById('ordenarPorPrecio');
  const ordenarPorStock = document.getElementById('ordenarPorStock');
  const btnVerMas = document.getElementById('btnVerMas');

  [filtroTipoTela, filtroColor, ordenarPorPrecio, ordenarPorStock].forEach(select => {
    select?.addEventListener('change', () => {
      telasMostradas = 0; // reset contador
      mostrarTelas();
    });
  });

  btnVerMas?.addEventListener('click', () => {
    mostrarTelas();
  });

  mostrarTelas();
  initCarrusel();
});