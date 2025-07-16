package com.tallerwebi.presentacion.controlador;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TipoEnvio;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.repositorio.RepositorioCompraTela;
import com.tallerwebi.dominio.servicio.ServicioEnvio;
import com.tallerwebi.dominio.servicio.ServicioPago;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import com.tallerwebi.dominio.servicio.ServicioCotizacionDolar;

@Controller
public class ControladorTela {

    private final ServicioTela servicioTela;
    private final ServicioEnvio servicioEnvio;
    private final ServicioCotizacionDolar servicioCotizacionDolar;
    private final ServicioPago servicioPago;
    private final RepositorioCompraTela repositorioCompraTela;

    @Autowired
    public ControladorTela(ServicioTela servicioTela,
                           ServicioEnvio servicioEnvio,
                           ServicioCotizacionDolar servicioCotizacionDolar,
                           ServicioPago servicioPago, RepositorioCompraTela repositorioCompraTela) {
        this.servicioTela = servicioTela;
        this.servicioEnvio = servicioEnvio;
        this.servicioCotizacionDolar = servicioCotizacionDolar;
        this.servicioPago = servicioPago;
        this.repositorioCompraTela = repositorioCompraTela;
    }

    private void cargarDatosTela(Model model, String color, String tipoTela, Double precio, Double metros, String imagenUrl) {
        model.addAttribute("color", color);
        model.addAttribute("tipoTela", tipoTela);
        model.addAttribute("precio", precio);
        model.addAttribute("metros", metros);
        model.addAttribute("imagenUrl", imagenUrl);
    }

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

    @PostMapping("/procesar-compra")
    public String procesarCompra(@RequestParam Long idTela,
                                 @RequestParam String color,
                                 @RequestParam Double precio,
                                 @RequestParam Double metros,
                                 @RequestParam String tipoTela,
                                 @RequestParam String imagenUrl,
                                 RedirectAttributes redirectAttributes) {

        // Validaciones básicas
        if (precio == null || precio <= 0) {
            redirectAttributes.addFlashAttribute("mensajeError", "Precio inválido.");
            return "redirect:/metodo-pago-tela";
        }

        if (metros == null || metros <= 0) {
            redirectAttributes.addFlashAttribute("mensajeError", "Metros inválidos.");
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

    @GetMapping("/metodo-pago-tela")
    public String mostrarVistaPago(@ModelAttribute("idTela") Long idTela,
                                   @ModelAttribute("precio") Double precio,
                                   @ModelAttribute("metros") Double metros,
                                   @ModelAttribute("tipoTela") String tipoTela,
                                   @ModelAttribute("color") String color,
                                   @ModelAttribute("imagenUrl") String imagenUrl,
                                   Model model) {
        try {
            double cotizacionDolar = servicioCotizacionDolar.obtenerCotizacionDolar();
            model.addAttribute("cotizacionDolar", cotizacionDolar);
        } catch (Exception e) {
            model.addAttribute("cotizacionDolar", 1270.0);
        }

        model.addAttribute("precio", precio);
        model.addAttribute("metros", metros);
        model.addAttribute("tipoTela", tipoTela);
        model.addAttribute("color", color);
        model.addAttribute("imagenUrl", imagenUrl);
        model.addAttribute("idTela", idTela);

        return "metodo-pago-tela";
    }

    @PostMapping("/confirmar-pago")
    public String confirmarPago(@RequestParam String metodoPago,
                                @RequestParam String numeroTarjeta,
                                @RequestParam String nombreTitular,
                                @RequestParam String vencimiento,
                                @RequestParam String cvv,
                                @RequestParam(required = false) Integer cuotas,
                                @RequestParam String color,
                                @RequestParam(required = false) Double precio, // puedes dejarlo, pero lo sobreescribes
                                @RequestParam(required = false) Double metros,
                                @RequestParam String tipoTela,
                                @RequestParam String imagenUrl,
                                @RequestParam Long idTela,
                                @RequestParam String jsonDireccion,
                                @RequestParam(name = "pagoEnDolares", required = false, defaultValue = "false") boolean pagoEnDolares,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        // Obtén la tela real desde el servicio/repositorio
        Tela telaReal = servicioTela.obtenerTela(idTela);
        if (telaReal == null) {
            model.addAttribute("mensajeError", "Tela no encontrada.");
            cargarDatosTela(model, color, tipoTela, 0.0, metros != null ? metros : 0.0, imagenUrl);
            return "metodo-pago-tela";
        }

        // Sobrescribe el precio con el real, ignorando lo que venga del cliente
        precio = telaReal.getPrecio();

        // Validar metros
        metros = (metros != null) ? metros : 0.0;
        if (metros <= 0) {
            model.addAttribute("mensajeError", "Cantidad de metros inválida.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Validaciones de tarjeta
        if (!numeroTarjeta.matches("\\d{16}")) {
            model.addAttribute("mensajeError", "Número de tarjeta inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        if (!cvv.matches("\\d{3}")) {
            model.addAttribute("mensajeError", "CVV inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        if (nombreTitular.trim().isEmpty()) {
            model.addAttribute("mensajeError", "El nombre del titular es obligatorio.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        try {
            YearMonth fechaVenc = YearMonth.parse(vencimiento);
            if (fechaVenc.isBefore(YearMonth.now())) {
                model.addAttribute("mensajeError", "La tarjeta está vencida.");
                cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
                return "metodo-pago-tela";
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("mensajeError", "Fecha de vencimiento inválida.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        if (!metodoPago.equalsIgnoreCase("credito") && !metodoPago.equalsIgnoreCase("debito")) {
            model.addAttribute("mensajeError", "Método de pago inválido.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        if (metodoPago.equalsIgnoreCase("debito")) {
            cuotas = 1;
        } else if (cuotas == null || !List.of(1, 2, 3, 6, 12).contains(cuotas)) {
            model.addAttribute("mensajeError", "Debe seleccionar una cantidad de cuotas válida.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        // Validar dirección y tipo de envío
        TipoEnvio envio;
        try {
            envio = servicioEnvio.determinarTipoEnvio(jsonDireccion);
        } catch (Exception e) {
            model.addAttribute("mensajeError", "No se pudo validar la dirección ingresada.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        if (envio == null || (envio != TipoEnvio.LOCAL && jsonDireccion.trim().isEmpty())) {
            model.addAttribute("mensajeError", "Debés ingresar una dirección válida para el envío.");
            cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
            return "metodo-pago-tela";
        }

        double costoEnvio = (envio == TipoEnvio.LOCAL) ? 0 : envio.getCosto();
        double total = servicioPago.calcularTotal(precio, metros, costoEnvio, cuotas);

        double cotizacionDolar = 0;
        double totalMostrar;
        double totalEquivalente;
        double valorCuota;

        if (pagoEnDolares) {
            try {
                cotizacionDolar = servicioCotizacionDolar.obtenerCotizacionDolar();
                if (cotizacionDolar <= 0) throw new RuntimeException("Cotización inválida");
            } catch (Exception e) {
                model.addAttribute("mensajeError", "Error al obtener la cotización del dólar.");
                cargarDatosTela(model, color, tipoTela, precio, metros, imagenUrl);
                return "metodo-pago-tela";
            }
            totalMostrar = servicioPago.calcularTotalEnDolares(total, cotizacionDolar);
            totalEquivalente = total;
        } else {
            totalMostrar = total;
            totalEquivalente = 0;
        }

        valorCuota = servicioPago.calcularValorCuota(totalMostrar, cuotas);

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debés iniciar sesión para realizar la compra.");
            return "redirect:/login";
        }

        try {
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

        servicioTela.registrarCompraTela(idTela, usuario, metros, metodoPago, cuotas, pagoEnDolares, cotizacionDolar, envio);

        String fechaCompra = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        redirectAttributes.addFlashAttribute("fecha", fechaCompra);
        redirectAttributes.addFlashAttribute("color", color);
        redirectAttributes.addFlashAttribute("tipoTela", tipoTela);
        redirectAttributes.addFlashAttribute("precio", precio);
        redirectAttributes.addFlashAttribute("metros", metros);
        redirectAttributes.addFlashAttribute("imagenUrl", imagenUrl);
        redirectAttributes.addFlashAttribute("metodoPago", metodoPago);
        redirectAttributes.addFlashAttribute("cuotas", cuotas);
        redirectAttributes.addFlashAttribute("valorCuota", valorCuota);
        redirectAttributes.addFlashAttribute("total", totalMostrar);
        redirectAttributes.addFlashAttribute("envioDescripcion", envio.getDescripcion());
        redirectAttributes.addFlashAttribute("envioCosto", costoEnvio);
        redirectAttributes.addFlashAttribute("envioTiempo", envio.getTiempoEntrega());
        redirectAttributes.addFlashAttribute("pagoEnDolares", pagoEnDolares);
        redirectAttributes.addFlashAttribute("cotizacionDolar", cotizacionDolar);
        redirectAttributes.addFlashAttribute("totalEquivalente", totalEquivalente);

        // Dirección de envío o retiro
        if (envio == TipoEnvio.LOCAL) {
            redirectAttributes.addFlashAttribute("direccionCompleta", "Sucursal central - Av. Siempre Viva 742, Buenos Aires");
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode direccionJson = mapper.readTree(jsonDireccion);
                String direccionCompleta = direccionJson.has("display_name")
                        ? direccionJson.get("display_name").asText()
                        : "Dirección desconocida";
                redirectAttributes.addFlashAttribute("direccionCompleta", direccionCompleta);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("direccionCompleta", "Dirección desconocida");
            }
        }

        redirectAttributes.addFlashAttribute("mensaje", "Compra realizada con éxito");

        return "redirect:/boleta-tela";
    }

    @PostMapping("/calcular-envio")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calcularEnvio(@RequestParam String jsonDireccion) {
        try {
            TipoEnvio tipoEnvio = servicioEnvio.determinarTipoEnvio(jsonDireccion);
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("tipo", tipoEnvio.name());
            resultado.put("descripcion", tipoEnvio.getDescripcion());
            resultado.put("costo", tipoEnvio.getCosto());
            resultado.put("tiempoEntrega", tipoEnvio.getTiempoEntrega());
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Dirección inválida o no encontrada"));
        }
    }

    @GetMapping("/boleta-tela")
    public String mostrarBoleta(Model model) {
        // No necesitas agregar nada si usas flash attributes
        return "boleta-tela";
    }


    @RequestMapping(value = "/telas-por-prenda/{prendaId}", method = RequestMethod.GET)
    @ResponseBody
    public List<DatosTela> obtenerTelasPorPrenda(
            @PathVariable("prendaId") Long prendaId,
            @RequestParam("metrosTalle") Double metrosTalle) {

        List<Tela> telas = servicioTela.buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(prendaId, metrosTalle);

        return telas.stream()
                .map(t -> new DatosTela(
                        t.getId(),
                        t.getTipoTela(),
                        t.getMetros(),
                        t.getColor(),
                        t.getImagenUrl()))
                .collect(Collectors.toList());
    }

}