package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorTela {

    @Autowired
    private ServicioTela servicioTela;

    private final List<MisTelas> telasDelUsuario = new ArrayList<>();

    @GetMapping("/catalogo-telas")
    public String mostrarCatalogoTelas(Model model) {
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();
        model.addAttribute("telas", telas);
        return "catalogo-telas";
    }


    @GetMapping("/mis-telas/cargar-tela")
    public String mostrarFormularioCarga(Model model) {
        model.addAttribute("telasUsuario", telasDelUsuario);
        model.addAttribute("tiposTela", TipoTela.values());
        return "cargar-tela";
    }


    @PostMapping("/mis-telas/cargar-tela")
    public String cargarTela(@RequestParam String tipoTela,
                             @RequestParam String color,
                             @RequestParam String imagenUrl) {
        try {
            TipoTela tipo = TipoTela.valueOf(tipoTela.toUpperCase());
            MisTelas nueva = new MisTelas(generarId(), tipo, color, 0.0, imagenUrl);
            telasDelUsuario.add(nueva);
        } catch (IllegalArgumentException e) {
            return "redirect:/mis-telas/cargar?error=tipoInvalido";
        }
        return "redirect:/mis-telas/cargar-tela";
    }

    @GetMapping("/registrar-tela")
    public String registrarTelaDesdeCatalogo(@RequestParam Long id) {
        MisTelas telaComprada = servicioTela.obtenerTelasDeFabrica().stream()
                .filter(tela -> tela.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (telaComprada != null && telasDelUsuario.stream().noneMatch(t -> t.getId().equals(id))) {
            telasDelUsuario.add(telaComprada);
        }

        return "redirect:/mis-telas/cargar-tela";
    }

    private Long generarId() {
        return (long) (telasDelUsuario.size() + 1000);
    }
}

