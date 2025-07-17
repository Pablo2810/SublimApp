package com.tallerwebi.presentacion.controlador;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ControladorPedido {

    private final ServicioPedido servicioPedido;
    private final ServicioProducto servicioProducto;
    private final ServicioUsuario servicioUsuario;
    private final ServicioMaquina servicioMaquina;
    private final ServicioTela servicioTela;
    private final ServicioCotizacionDolar servicioCotizacionDolar;
    private final ServicioEnvio servicioEnvio;

    @Autowired
    public ControladorPedido(ServicioPedido servicioPedido,
                             ServicioUsuario servicioUsuario,
                             ServicioProducto servicioProducto,
                             ServicioMaquina servicioMaquina,
                             ServicioTela servicioTela,
                             ServicioCotizacionDolar servicioCotizacionDolar,
                             ServicioEnvio servicioEnvio) {
        this.servicioPedido = servicioPedido;
        this.servicioUsuario = servicioUsuario;
        this.servicioProducto = servicioProducto;
        this.servicioMaquina = servicioMaquina;
        this.servicioTela = servicioTela;
        this.servicioCotizacionDolar = servicioCotizacionDolar;
        this.servicioEnvio = servicioEnvio;
    }

    @RequestMapping(value = "/detalle-pedido", method = RequestMethod.GET)
    public ModelAndView mostrarCarrito(ModelMap model, HttpServletRequest request) {
        if (!model.containsAttribute("pedido")) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            servicioPedido.asociarProductoPedido(pedido);
            model.addAttribute("pedido", pedido);
        }
        return new ModelAndView("detalle-pedido", model);
    }

    @RequestMapping(value = "/historial-pedidos", method = RequestMethod.GET)
    public ModelAndView historialPedidos(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        if (usuario != null) {
            try {
                List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuarioNoPendiente(usuario.getId());
                model.put("mensajeSinPedidos", "Todavía no tienes pedidos");
                model.put("pedidos", pedidos);
            } catch (Exception e) {
                return new ModelAndView("redirect:/home");
            }
        } else {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("historial-pedidos", model);
    }

    @RequestMapping(value = "/cancelar-pedido/{id}", method = RequestMethod.POST)
    public ModelAndView cancelarPedido(@PathVariable("id") Long id,
                                       RedirectAttributes redirectAttributes) {
        try {
            servicioPedido.cancelarPedido(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cancelaste tu pedido");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ya no es posible cancelar este pedido");
        }

        return new ModelAndView("redirect:/historial-pedidos");
    }

    /*@RequestMapping(value = "/pagar-pedido", method = RequestMethod.POST)
    public ModelAndView pagarPedidoPendiente(@RequestParam Long pedidoId,
                                             @RequestParam("moneda") Moneda moneda,
                                             @RequestParam("cotizacion") double cotizacion,
                                             HttpServletRequest request) {
        try {
            boolean cambioExitoso = servicioPedido.cambiarEstadoPedido(pedidoId, Estado.EN_ESPERA);
            if (!cambioExitoso) {
                throw new RuntimeException("No se pudo cambiar el estado del pedido");
            }
            int diasEspera = servicioMaquina.calcularTiempoEspera();
            servicioPedido.generarPedidoCompleto(pedidoId, moneda, cotizacion, UUID.randomUUID().toString(), LocalDate.now(), diasEspera);
            return new ModelAndView("redirect:/historial-pedidos");
        } catch (Exception e) {
            ModelMap model = new ModelMap();
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            model.put("pedido", pedido);
            model.put("error", "Error al procesar el pago, por favor intente nuevamente.");
            return new ModelAndView("detalle-pedido", model);
        }
    }*/

    @PostMapping("/eliminar-producto/{productoId}")
    public ModelAndView eliminarProducto(@PathVariable Long productoId,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        try {
            reponerStockTela(productoId);
            servicioProducto.eliminarProducto(productoId);

            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            if (pedido != null && pedido.getProductos().isEmpty()) {
                servicioPedido.eliminarPedido(pedido);
                ModelMap model = new ModelMap();
                model.put("mensaje", "No tienes pedidos pendientes");
                model.put("pedido", null);
                return new ModelAndView("detalle-pedido", model);
            }

            redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar el producto: " + e.getMessage());
        }

        return new ModelAndView("redirect:/detalle-pedido");
    }

    private void reponerStockTela(Long idProducto) {
        Producto productoEncontrado = servicioProducto.buscarPorId(idProducto);
        Double metrosTela = productoEncontrado.getTela().getMetros();
        Double metrosCantidadTalle = productoEncontrado.getTalle().getMetrosTotales() * productoEncontrado.getCantidad();
        productoEncontrado.getTela().setMetros(metrosTela + metrosCantidadTalle);
        servicioTela.actualizar(productoEncontrado.getTela());
    }

    @GetMapping("/metodo-pago-pedido")
    public String mostrarFormularioPagoProducto(
            @RequestParam("idPedido") Long idPedido,
            Model model,
            @ModelAttribute("mensajeError") String mensajeError,
            @ModelAttribute("opcionEnvio") String opcionEnvioFlash,
            @ModelAttribute("direccionEnvioSeleccionada") String direccionEnvioFlash,
            @ModelAttribute("metodoPago") String metodoPagoFlash,
            @ModelAttribute("moneda") String monedaFlash
    ) {
        double cotizacionDolar;
        try {
            cotizacionDolar = servicioCotizacionDolar.obtenerCotizacionDolar();
        } catch (Exception e) {
            cotizacionDolar = 1270.0;
        }

        Pedido pedido = servicioPedido.obtenerPedido(idPedido);
        if (pedido == null) {
            return "redirect:/historial-pedidos";
        }

        int cantidadTotal = pedido.getProductos().stream()
                .mapToInt(Producto::getCantidad)
                .sum();

        double precioUnitario = cantidadTotal > 0 ? pedido.getMontoTotal() / cantidadTotal : 0;

        model.addAttribute("pedido", pedido);
        model.addAttribute("cantidadTotal", cantidadTotal);
        model.addAttribute("precioUnitario", precioUnitario);
        model.addAttribute("idPedido", idPedido);
        model.addAttribute("tiposEnvio", TipoEnvio.values());
        model.addAttribute("cotizacionDolar", cotizacionDolar);

        // Usar valores flash o valores por defecto
        model.addAttribute("mensajeError", mensajeError != null ? mensajeError : null);
        model.addAttribute("opcionEnvio", opcionEnvioFlash != null ? opcionEnvioFlash : TipoEnvio.LOCAL.name());
        model.addAttribute("direccionEnvioSeleccionada", direccionEnvioFlash != null ? direccionEnvioFlash : "");
        model.addAttribute("metodoPago", metodoPagoFlash != null ? metodoPagoFlash : "debito");
        model.addAttribute("moneda", monedaFlash != null ? monedaFlash : "PESOS");

        return "metodo-pago-pedido";
    }

    @PostMapping("/confirmar-pago-productos")
    public String confirmarPagoProductos(
            @RequestParam("idPedido") Long idPedido,
            @RequestParam("pagoEnDolares") Boolean pagoEnDolares,
            @RequestParam("metodoPago") String metodoPago,
            @RequestParam(value = "cuotas", required = false) Integer cuotas,
            @RequestParam(value = "direccionEnvio", required = false) String direccionEnvio,
            @RequestParam("numeroTarjeta") String numeroTarjeta,
            @RequestParam("cvv") String cvv,
            @RequestParam("nombreTitular") String nombreTitular,
            @RequestParam("vencimiento") String vencimiento,
            @RequestParam("tipoEnvio") String tipoEnvioStr,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            if (pagoEnDolares == null) {
                throw new RuntimeException("Debe seleccionar la moneda de pago.");
            }

            // Validaciones de tarjeta
            if (!numeroTarjeta.matches("\\d{16}"))
                throw new RuntimeException("Número de tarjeta inválido.");
            if (!cvv.matches("\\d{3}"))
                throw new RuntimeException("CVV inválido.");
            if (nombreTitular.trim().isEmpty())
                throw new RuntimeException("Nombre titular vacío.");
            try {
                YearMonth fechaVenc = YearMonth.parse(vencimiento);
                if (fechaVenc.isBefore(YearMonth.now()))
                    throw new RuntimeException("Tarjeta vencida.");
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Fecha de vencimiento inválida.");
            }

            if (!metodoPago.equalsIgnoreCase("debito") && !metodoPago.equalsIgnoreCase("credito")) {
                throw new RuntimeException("Método de pago inválido.");
            }

            // Forzar cuotas a 1 si es débito
            if ("debito".equalsIgnoreCase(metodoPago)) {
                cuotas = 1;
            } else if (cuotas == null || !List.of(1, 2, 3, 6, 12).contains(cuotas)) {
                throw new RuntimeException("Cantidad de cuotas inválida.");
            }

            // Calcular tipo de envío basado en dirección
            TipoEnvio tipoEnvioDetalles;
            try {
                tipoEnvioDetalles = TipoEnvio.valueOf(tipoEnvioStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Tipo de envío inválido.");
            }

            // Validar dirección si el tipo de envío no es LOCAL
            if (tipoEnvioDetalles != TipoEnvio.LOCAL &&
                    (direccionEnvio == null || direccionEnvio.trim().isEmpty())) {
                throw new RuntimeException("Debe ingresar una dirección válida para el envío.");
            }

            // Obtener cotización dólar si paga en dólares
            double cotizacion = 1.0;
            if (pagoEnDolares) {
                try {
                    cotizacion = servicioCotizacionDolar.obtenerCotizacionDolar();
                } catch (Exception e) {
                    cotizacion = 1270.0; // fallback
                }
            }

            Pedido pedido = servicioPedido.obtenerPedido(idPedido);
            if (pedido == null) {
                throw new RuntimeException("Pedido no encontrado.");
            }

            // Guardar dirección completa para boleta
            if (tipoEnvioDetalles == TipoEnvio.LOCAL) {
                redirectAttributes.addFlashAttribute("direccionCompleta", "Sucursal central - Av. Siempre Viva 742, Buenos Aires");
            } else {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode direccionJson = mapper.readTree(direccionEnvio);
                    String direccionCompleta = direccionJson.has("display_name")
                            ? direccionJson.get("display_name").asText()
                            : direccionEnvio;
                    redirectAttributes.addFlashAttribute("direccionCompleta", direccionCompleta);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("direccionCompleta", direccionEnvio);
                }
            }

            // Procesar pago y actualizar pedido con todos los datos
            pedido = servicioPedido.procesarPagoPedidoProductos(
                    idPedido,
                    pagoEnDolares,
                    metodoPago,
                    tipoEnvioDetalles.name(),
                    direccionEnvio,
                    numeroTarjeta,
                    cvv,
                    nombreTitular,
                    vencimiento,
                    cuotas
            );

            // Formatear fechas para la vista
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaCreacionFormateada = pedido.getFechaCreacion() != null ? pedido.getFechaCreacion().format(formatter) : "";
            String fechaEntregaFormateada = pedido.getFechaEntrega() != null ? pedido.getFechaEntrega().format(formatter) : "";

            redirectAttributes.addFlashAttribute("fechaCreacionFormateada", fechaCreacionFormateada);
            redirectAttributes.addFlashAttribute("fechaEntregaFormateada", fechaEntregaFormateada);
            redirectAttributes.addFlashAttribute("pedido", pedido);
            redirectAttributes.addFlashAttribute("cotizacionDolar", cotizacion);

            return "redirect:/boleta-pedido?idPedido=" + pedido.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            redirectAttributes.addFlashAttribute("direccionEnvioSeleccionada", direccionEnvio);
            redirectAttributes.addFlashAttribute("metodoPago", metodoPago);
            redirectAttributes.addFlashAttribute("moneda", pagoEnDolares != null && pagoEnDolares ? "DOLARES" : "PESOS");
            return "redirect:/metodo-pago-pedido?idPedido=" + idPedido;
        }
    }

    @GetMapping("/boleta-pedido")
    public String mostrarBoletaPedido(@RequestParam("idPedido") Long idPedido, Model model) {
        Pedido pedido = servicioPedido.obtenerPedido(idPedido);
        if (pedido == null) {
            return "redirect:/error";
        }

        boolean pagoEnDolares = pedido.getPagoEnDolares();
        double cotizacionDolar = 1.0;

        if (pagoEnDolares) {
            cotizacionDolar = pedido.getCotizacionDolar() != null ? pedido.getCotizacionDolar() : 1.0;
        }

        // Convertir precios si se pagó en dólares para cada producto
        for (Producto producto : pedido.getProductos()) {
            double precioConvertido = pagoEnDolares
                    ? producto.getPrecio() / cotizacionDolar
                    : producto.getPrecio();
            producto.setPrecioConvertido(precioConvertido);
        }

        model.addAttribute("pedido", pedido);
        model.addAttribute("pagoEnDolares", pagoEnDolares);
        model.addAttribute("cotizacionDolar", cotizacionDolar);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaCreacionFormateada = pedido.getFechaCreacion() != null ? pedido.getFechaCreacion().format(formatter) : "";
        String fechaEntregaFormateada = pedido.getFechaEntrega() != null ? pedido.getFechaEntrega().format(formatter) : "";
        model.addAttribute("fechaCreacionFormateada", fechaCreacionFormateada);
        model.addAttribute("fechaEntregaFormateada", fechaEntregaFormateada);

        return "boleta-pedido";
    }

    @PostMapping("/calcular-envio-pedido")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calcularEnvioPedido(@RequestParam String jsonDireccion) {
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
}

