package com.tallerwebi.presentacion.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHome {

    @GetMapping("/inicio")
    public String mostrarHome() {
        return "thymeleaf/home";
    }

    /*@GetMapping("/guia-talles")
    public ModelAndView verGuiaDeTalles(){
        return new ModelAndView("guia-talles");
    }*/

    @GetMapping("/disenios")
    public ModelAndView verDiseniosPersonalizados(){
        return new ModelAndView("disenios");
    }
}


