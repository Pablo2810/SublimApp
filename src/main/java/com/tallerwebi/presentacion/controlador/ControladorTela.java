package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.TipoEnvio;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class ControladorTela {

    private final ServicioTela servicioTela;

    @Autowired
    public ControladorTela(ServicioTela servicioTela) {
        this.servicioTela = servicioTela;
    }

    private void cargarDatosTela(Model model, String color, String tipoTela, Double precio, Double metros, String imagenUrl) {
        model.addAttribute("color", color);
        model.addAttribute("tipoTela", tipoTela);
        model.addAttribute("precio", precio);
        model.addAttribute("metros", metros);
        model.addAttribute("imagenUrl", imagenUrl);
    }


    // 1. Mostrar catálogo de telas
    @GetMapping("/catalogo-telas")
    public String mostrarCatalogoTelas(Model model) {
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();
        List<MisTelas> telasCarrusel = servicioTela.obtenerTelasParaCarrusel();

        if (telas == null || telas.isEmpty()) {
            model.addAttribute("mensajeError", "No se pudieron cargar las telas de la fábrica.");
        } else {
            model.addAttribute("telas", telas);
            model.addAttribute("telasCarrusel", telasCarrusel);
        }

        return "catalogo-telas";
    }


    // 4. Mostrar detalle de una tela del catálogo
    @GetMapping("/detalle-tela-id/{id}")
    public String verDetalleTelaPorId(@PathVariable Long id, Model model) {
        MisTelas tela = servicioTela.obtenerTelasDeFabrica().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (tela == null) {
            model.addAttribute("mensajeError", "Tela no encontrada.");
            return "catalogo-telas";
        }

        model.addAttribute("tela", tela);
        return "detalle-tela";
    }

    // 5. Paso 1: Procesar datos antes de elegir método de pago
    @PostMapping("/procesar-compra")
    public String procesarCompra(@RequestParam Long idTela,
                                 @RequestParam String color,
                                 @RequestParam Double precio,
                                 @RequestParam Double metros,
                                 @RequestParam String tipoTela,
                                 @RequestParam String imagenUrl,
                                 RedirectAttributes redirectAttributes) {

        if (precio == null || precio <= 0) {
            redirectAttributes.addFlashAttribute("mensajeError", "Precio inválido.");
            return "redirect:/metodo-pago-tela";
        }

        redirectAttributes.addFlashAttribute("idTela", idTela);
        redirectAttributes.addFlashAttribute("color", color);
        redirectAttributes.addFlashAttribute("precio", precio);
        redirectAttributes.addFlashAttribute("metros", metros);
        redirectAttributes.addFlashAttribute("tipoTela", tipoTela);
        redirectAttributes.addFlashAttribute("imagenUrl", imagenUrl);

        return "redirect:/metodo-pago-tela";
    }

    // 6. Paso 2: Mostrar formulario de método de pago
    @GetMapping("/metodo-pago-tela")
    public String mostrarVistaPago() {
        return "metodo-pago-tela";
    }

    // 7. Paso 3: Confirmar pago y registrar tela
    @PostMapping("/confirmar-pago")
    public String confirmarPago(@RequestParam String metodoPago,
                                @RequestParam String numeroTarjeta,
                                @RequestParam String nombreTitular,
                                @RequestParam String vencimiento,
                                @RequestParam String cvv,
                                @RequestParam(required = false) Integer cuotas,
                                @RequestParam String color,
                                @RequestParam Double precio,
                                @RequestParam Double metros,
                                @RequestParam String tipoTela,
                                @RequestParam String imagenUrl,
                                @RequestParam Long idTela,
                                @RequestParam String tipoEnvio,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        // Validaciones de tarjeta
        if (numeroTarjeta == null || !numeroTarjeta.matches("\\d{16}")) {
            model.addAttribute("mensajeError", "Número de tarjeta inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            model.addAttribute("mensajeError", "CVV inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }
        if (nombreTitular == null || nombreTitular.trim().isEmpty()) {
            model.addAttribute("mensajeError", "El nombre del titular es obligatorio.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Validar fecha de vencimiento
        try {
            YearMonth fechaVencimiento = YearMonth.parse(vencimiento);
            YearMonth ahora = YearMonth.now();
            if (fechaVencimiento.isBefore(ahora)) {
                model.addAttribute("mensajeError", "La tarjeta está vencida.");
                cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
                return "metodo-pago-tela";
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("mensajeError", "Fecha de vencimiento inválida.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Validar método de pago y cuotas
        if (!metodoPago.equalsIgnoreCase("credito") && !metodoPago.equalsIgnoreCase("debito")) {
            model.addAttribute("mensajeError", "Método de pago inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }
        if (metodoPago.equalsIgnoreCase("debito")) {
            cuotas = 1;
        } else if (cuotas == null || !(cuotas == 1 || cuotas == 2 || cuotas == 3 || cuotas == 6 || cuotas == 12)) {
            model.addAttribute("mensajeError", "Debe seleccionar una cantidad de cuotas válida.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Validar tipoEnvio y convertir a enum
        TipoEnvio envioSeleccionado;
        try {
            envioSeleccionado = TipoEnvio.valueOf(tipoEnvio.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            model.addAttribute("mensajeError", "Método de envío inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Calcular total con costo de envío e intereses
        double interes;
        switch (cuotas) {
            case 2: interes = 0.05; break;
            case 3: interes = 0.08; break;
            case 6: interes = 0.12; break;
            case 12: interes = 0.20; break;
            default: interes = 0.0;
        }

        double subtotal = precio * metros;
        double costoEnvio = envioSeleccionado.getCosto();
        double totalConEnvio = subtotal + costoEnvio;
        double total = totalConEnvio * (1 + interes);
        double valorCuota = total / cuotas;

        // Intentar descontar stock y registrar la compra
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("mensajeError", "Debés iniciar sesión para realizar la compra.");
                return "redirect:/login";
            }
            servicioTela.comprarTelaDeFabrica(idTela, metros, usuario);
        } catch (TelaNoEncontrada e) {
            model.addAttribute("mensajeError", "Tela no encontrada.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        } catch (StockInsuficiente e) {
            model.addAttribute("mensajeError", "No hay suficiente stock de esta tela.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Preparar boleta
        String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        redirectAttributes.addFlashAttribute("fecha", fechaFormateada);
        redirectAttributes.addFlashAttribute("color", color);
        redirectAttributes.addFlashAttribute("tipoTela", tipoTela);
        redirectAttributes.addFlashAttribute("precio", precio);
        redirectAttributes.addFlashAttribute("metros", metros);
        redirectAttributes.addFlashAttribute("imagenUrl", imagenUrl);
        redirectAttributes.addFlashAttribute("metodoPago", metodoPago);
        redirectAttributes.addFlashAttribute("cuotas", cuotas);
        redirectAttributes.addFlashAttribute("valorCuota", valorCuota);
        redirectAttributes.addFlashAttribute("total", total);

        // Datos de envío para mostrar en boleta
        redirectAttributes.addFlashAttribute("envioDescripcion", envioSeleccionado.getDescripcion());
        redirectAttributes.addFlashAttribute("envioCosto", costoEnvio);
        redirectAttributes.addFlashAttribute("envioTiempo", envioSeleccionado.getTiempoEntrega());

        redirectAttributes.addFlashAttribute("mensaje", "Compra realizada con éxito");

        return "redirect:/boleta-tela";
    }

    // 8. Vista de boleta final
    @GetMapping("/boleta-tela")
    public String mostrarBoleta() {
        return "boleta-tela";
    }

}