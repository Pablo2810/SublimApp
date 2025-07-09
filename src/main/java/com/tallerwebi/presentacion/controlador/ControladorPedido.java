package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class ControladorPedido {


    private final ServicioPedido servicioPedido;
    private final ServicioProducto servicioProducto;
    private final ServicioUsuario servicioUsuario;
    private final ServicioMaquina servicioMaquina;

    @Autowired
    public ControladorPedido(ServicioPedido servicioPedido,
                             ServicioUsuario servicioUsuario,
                             ServicioProducto servicioProducto,
                             ServicioMaquina servicioMaquina) {
        this.servicioPedido = servicioPedido;
        this.servicioUsuario = servicioUsuario;
        this.servicioProducto = servicioProducto;
        this.servicioMaquina = servicioMaquina;
    }

    /*
        @RequestMapping(path = "/nuevo-pedido", method = RequestMethod.GET)
        public ModelAndView nuevoPedido() {
            ModelMap model = new ModelMap();
            model.put("datosPedido", new DatosPedido());
            return new ModelAndView("nuevo-pedido", model);
        }

        @RequestMapping(path = "/detalle-pedido", method = RequestMethod.POST)
        public ModelAndView procesarPedido(@ModelAttribute("datosPedido") DatosPedido datosPedido, HttpServletRequest request) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
            ModelMap model = new ModelMap();
            HashSet<Producto> productos = new HashSet<>();

            Pedido pedido = servicioPedido.registrarPedido(UUID.randomUUID().toString(), usuario, productos);

            model.put("pedidoNuevo", pedido);

            return new ModelAndView("detalle-pedido", model);
        }

        @RequestMapping("/historial-pedidos")
        public ModelAndView historialPedidos() {
            ModelMap model = new ModelMap();
            List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuario(1L);

            model.put("mensajeSinPedidos", "Todavia no tienes pedidos");
            model.put("pedidos", pedidos);

            return new ModelAndView("historial-pedidos", model);
        }
    */

    // Muestra el pedido pendiente (carrito)
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

    // Muestra historial de pedidos ya pagados o no pendientes
    @RequestMapping(value = "/historial-pedidos", method = RequestMethod.GET)
    public ModelAndView historialPedidos(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        if (usuario != null) {
            try {
                List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuarioNoPendiente(usuario.getId());

                model.put("mensajeSinPedidos", "Todavia no tienes pedidos");
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

    // Procesa el pago del pedido pendiente
    @RequestMapping(value = "/pagar-pedido", method = RequestMethod.POST)
    public ModelAndView pagarPedidoPendiente(@RequestParam Long pedidoId,
                                             @RequestParam("moneda") Moneda moneda,
                                             @RequestParam("cotizacion") double cotizacion,
                                             HttpServletRequest request) {
        try {
            // Cambiar estado a EN_ESPERA
            boolean cambioExitoso = servicioPedido.cambiarEstadoPedido(pedidoId, Estado.EN_ESPERA);
            if (!cambioExitoso) {
                throw new RuntimeException("No se pudo cambiar el estado del pedido");
            }
            // Calcular demora y completar pedido
            int diasEspera = servicioMaquina.calcularTiempoEspera();
            servicioPedido.generarPedidoCompleto(pedidoId, moneda, cotizacion, UUID.randomUUID().toString(), LocalDate.now(), diasEspera);
            // Redirigir a historial
            return new ModelAndView("redirect:/historial-pedidos");
        } catch (Exception e) {
            // En caso de error mostrar detalle pedido con mensaje
            ModelMap model = new ModelMap();
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            model.put("pedido", pedido);
            model.put("error", "Error al procesar el pago, por favor intente nuevamente.");
            return new ModelAndView("detalle-pedido", model);
        }
    }

    @PostMapping("/eliminar-producto/{productoId}")
    public ModelAndView eliminarProducto(@PathVariable Long productoId, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);

        if (pedido != null && pedido.getEstado() == Estado.PENDIENTE) {
            // Lógica para eliminar producto de pedido (depende de tu implementación)
            servicioPedido.eliminarProductoDelPedido(pedido, productoId);

            // Recalcular total
            servicioPedido.asociarProductoPedido(pedido);
        } else {
            // No permitido borrar producto si pedido no está en PENDIENTE
            // Podés mostrar mensaje o loguear
        }

        return new ModelAndView("redirect:/detalle-pedido");
    }

}
