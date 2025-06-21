package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import com.tallerwebi.dominio.entidad.Usuario;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
public class ControladorPedido {


    private final ServicioPedido servicioPedido;
    //private final ServicioPromocion servicioPromocion;
    private final ServicioProducto servicioProducto;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPedido(ServicioPedido servicioPedido,
                             ServicioUsuario servicioUsuario,
                             ServicioProducto servicioProducto) {
        this.servicioPedido = servicioPedido;
        this.servicioUsuario = servicioUsuario;
        this.servicioProducto = servicioProducto;
    }

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

}
