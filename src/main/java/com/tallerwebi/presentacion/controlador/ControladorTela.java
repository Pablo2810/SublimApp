package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
  public class ControladorTela {

    @Autowired
    private ServicioTela servicioTela;

    @GetMapping("/catalogo-telas")
    public String mostrarCatalogoTelas(Model model) {
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();
        model.addAttribute("telas", telas); // esto es lo que el HTML espera
        return "catalogo-telas";
    }

}