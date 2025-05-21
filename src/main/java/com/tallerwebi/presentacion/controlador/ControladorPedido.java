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

import java.io.IOException;

@Controller
public class ControladorPedido {

    private final ServicioArchivo servicioArchivo;
    private final ServicioPedido servicioPedido;

    @Autowired
    public ControladorPedido(ServicioArchivo servicioArchivo, ServicioPedido servicioPedido) {
        this.servicioArchivo = servicioArchivo;
        this.servicioPedido = servicioPedido;
    }

    @RequestMapping(path = "/nuevo-pedido", method = RequestMethod.GET)
    public ModelAndView nuevoPedido() {
        ModelMap model = new ModelMap();
        model.put("datosPedido", new DatosPedido());
        return new ModelAndView("nuevo-pedido", model);
    }

    @RequestMapping(path = "/detalle-pedido", method = RequestMethod.POST)
    public ModelAndView procesarPedido(@ModelAttribute("datosPedido") DatosPedido datosPedido,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        ModelMap model = new ModelMap();

        // Registrar archivo subido
        Archivo archivo = servicioArchivo.registrarArchivo(datosPedido.getNombre(), file);

        // Registrar pedido con la cantidad de copias y el archivo
        Pedido pedido = servicioPedido.registrarPedido(datosPedido.getCantidadCopias(), archivo);

        // Calcular metros totales y costo del servicio
        pedido.setMetrosTotales(servicioPedido.calcularMetros(pedido));
        pedido.setCostoServicio(servicioPedido.calcularCostoTotal(pedido.getAlto()));

        model.put("pedidoNuevo", pedido);
        return new ModelAndView("detalle-pedido", model);
    }
}

