package com.tallerwebi.presentacion.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPedido {

    @RequestMapping(path = "nuevo-pedido", method = RequestMethod.GET)
    public ModelAndView nuevoPedido() {
        return new ModelAndView("nuevo-pedido");
    }

}
