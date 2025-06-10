/*package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ControladorCompraTelas {

    private final ServicioTela servicioTela;

    @Autowired
    public ControladorCompraTelas(ServicioTela servicioTela) {
        this.servicioTela = servicioTela;
    }

    @PostMapping("/generar-boleta")
    public String procesarCompra(@RequestParam String color,
                                 @RequestParam Double precio,
                                 @RequestParam Double metros,
                                 Model model) {

        double total = precio * metros;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = LocalDateTime.now().format(formatter);
        model.addAttribute("fecha", fechaFormateada);


        model.addAttribute("color", color);
        model.addAttribute("precio", precio);
        model.addAttribute("metros", metros);
        model.addAttribute("total", total);

        return "boleta-tela";
    }

    @GetMapping("/detalle-tela-id/{id}")
    public String verDetalleTelaPorId(@PathVariable Long id, Model model) {
        MisTelas tela = servicioTela.obtenerTelasDeFabrica().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (tela == null) {
            model.addAttribute("mensajeError", "Tela no encontrada.");
            return "catalogo-telas";
        }

        model.addAttribute("tela", tela);
        return "detalle-tela";
    }

} */

