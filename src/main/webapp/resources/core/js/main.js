// ------------------------------
// Funciones matemáticas reutilizables
// ------------------------------
function suma(a, b) {
  return a + b;
}

function multiplicar(a, b) {
  return a * b;
}

// ------------------------------
// Mostrar automáticamente todos los toasts de Bootstrap al cargar
// ------------------------------
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

// ------------------------------
// Imprimir boleta
// ------------------------------
function imprimirBoleta() {
  const originalTitle = document.title;
  document.title = "Boleta de Compra";
  window.print();
  document.title = originalTitle;
}

// ------------------------------
// Descargar boleta como PDF
// ------------------------------
function descargarBoletaPDF() {
  const elementoBoleta = document.getElementById("boleta");
  if (!elementoBoleta) return;

  const opciones = {
    margin: 0.5,
    filename: "boleta-compra.pdf",
    image: { type: "jpeg", quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: "in", format: "letter", orientation: "portrait" }
  };

  html2pdf().set(opciones).from(elementoBoleta).save();
}

// ------------------------------
// Iniciar todo al cargar
// ------------------------------
document.addEventListener("DOMContentLoaded", () => {
  mostrarToasts();
});