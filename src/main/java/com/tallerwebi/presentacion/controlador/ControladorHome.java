package com.tallerwebi.presentacion.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorHome {

    @GetMapping("/home")
    public String mostrarHome() {
        return "home";
    }
}

