package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.presentacion.dto.DatosPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ControladorPedido {
    private ServicioArchivo servicioArchivo;
    private ServicioPedido servicioPedido;

    @Autowired
    public ControladorPedido(ServicioArchivo servicioArchivo, ServicioPedido servicioPedido){
        this.servicioArchivo = servicioArchivo;
        this.servicioPedido = servicioPedido;
    }

    @RequestMapping(path = "/nuevo-pedido", method = RequestMethod.GET)
    public ModelAndView nuevoPedido(){
        ModelMap model = new ModelMap();
        model.put("datosPedido", new DatosPedido());
        return new ModelAndView("nuevo-pedido", model);
    }

    @RequestMapping(path = "/detalle-pedido", method = RequestMethod.POST)
    public ModelAndView procesarPedido(@ModelAttribute("datosPedido") DatosPedido datosPedido, @RequestParam("file") MultipartFile file){
        ModelMap model = new ModelMap();
        Archivo archivo = servicioArchivo.registrarArchivo(datosPedido.getNombre(), file);
        Pedido pedido = servicioPedido.registrarPedido(datosPedido.getCantidadCopias(), archivo);
        model.put("pedidoNuevo", pedido);
        return new ModelAndView("detalle-pedido", model);
    }

    @RequestMapping("/historial-pedidos")
    public ModelAndView historialPedidos() {
        ModelMap model = new ModelMap();
        List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuario(1L);

        model.put("pedidos", pedidos);

        return new ModelAndView("historial-pedidos", model);
    }

}
