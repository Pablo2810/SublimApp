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
    private final ServicioTela servicioTela;

    @Autowired
    public ControladorPedido(ServicioPedido servicioPedido,
                             ServicioUsuario servicioUsuario,
                             ServicioProducto servicioProducto,
                             ServicioMaquina servicioMaquina,
                             ServicioTela servicioTela) {
        this.servicioPedido = servicioPedido;
        this.servicioUsuario = servicioUsuario;
        this.servicioProducto = servicioProducto;
        this.servicioMaquina = servicioMaquina;
        this.servicioTela = servicioTela;
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

    @RequestMapping(value = "/pagar-pedido", method = RequestMethod.POST)
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
    }

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
}

