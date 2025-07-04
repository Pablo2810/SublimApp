package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioMaquina;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioProducto;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.presentacion.dto.DatosPedido;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    @RequestMapping(value = "/detalle-pedido", method = RequestMethod.GET)
    public ModelAndView mostrarCarrito(ModelMap model, HttpServletRequest request) {
        if (!model.containsAttribute("pedido")) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            model.addAttribute("pedido", pedido);
        }
        return new ModelAndView("detalle-pedido", model);
    }

    @RequestMapping(value = "/pagar-pedido", method = RequestMethod.POST)
    public ModelAndView pagarPedidoPendiente(@RequestParam Long pedidoId, HttpServletRequest request){
        try {
            servicioPedido.cambiarEstadoPedido(pedidoId, Estado.EN_ESPERA);
            Long diasEspera = servicioMaquina.calcularTiempoEspera();
            servicioPedido.generarPedidoCompleto(pedidoId, UUID.randomUUID().toString(), LocalDate.now(), diasEspera);

            return new ModelAndView("historial-pedidos");
        } catch (Exception e) {
            ModelMap model = new ModelMap();
            // Recuperar el pedido con estado pendiente del usuario
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            model.put("pedido", pedido);

            return new ModelAndView("detalle-pedido", model);
        }
    }


    @RequestMapping(value = "/historial-pedidos", method = RequestMethod.GET)
    public ModelAndView historialPedidos(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuarioNoPendiente(usuario.getId());

        model.put("mensajeSinPedidos", "Todavia no tienes pedidos");
        model.put("pedidos", pedidos);

        return new ModelAndView("historial-pedidos", model);
    }
}
