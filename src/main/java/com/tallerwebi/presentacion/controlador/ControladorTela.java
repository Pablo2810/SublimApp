package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorTela {

    private final ServicioTela servicioTela;
    private final List<MisTelas> telasDelUsuario = new ArrayList<>();

    @Autowired
    public ControladorTela(ServicioTela servicioTela) {
        this.servicioTela = servicioTela;
    }

    @GetMapping("/catalogo-telas")
    public String mostrarCatalogoTelas(Model model) {
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();
        model.addAttribute("telas", telas);
        return "catalogo-telas";
    }


    @GetMapping("/cargar-tela")
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
    public String registrarTelaDesdeCatalogo(@RequestParam Long id, Model model) {
        MisTelas telaComprada = servicioTela.obtenerTelasDeFabrica().stream()
                .filter(tela -> tela.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (telaComprada == null) {
            model.addAttribute("mensajeError", "Tela no encontrada en el catálogo");
        } else if (telasDelUsuario.stream().anyMatch(t -> t.getId().equals(id))) {
            model.addAttribute("mensajeAdvertencia", "Ya has comprado esta tela");
        } else {
            telasDelUsuario.add(telaComprada);
            model.addAttribute("mensaje", "Tela comprada con éxito");
        }

        // Agregás nuevamente la lista para que se vuelva a mostrar
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();
        model.addAttribute("telas", telas);

        return "catalogo-telas";
    }




    private Long generarId() {
        return (long) (telasDelUsuario.size() + 1000);
    }
}

