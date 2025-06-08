package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.tallerwebi.dominio.entidad.Tela;

@Controller
public class ControladorTela {

    private final ServicioTela servicioTela;
    private final List<MisTelas> telasDelUsuario = new ArrayList<>();

    @Autowired
    public ControladorTela(ServicioTela servicioTela) {
        this.servicioTela = servicioTela;
    }

    // Mostrar el catálogo de telas
    @GetMapping("/catalogo-telas")
    public String mostrarCatalogoTelas(Model model) {
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();
        model.addAttribute("telas", telas);
        return "catalogo-telas";
    }

    // Mostrar el formulario para cargar telas del usuario
    @GetMapping("/cargar-tela")
    public String mostrarFormularioCarga(Model model) {
        model.addAttribute("telasUsuario", telasDelUsuario); // Lista en memoria
        model.addAttribute("tiposTela", TipoTela.values());
        return "cargar-tela";
    }

    // Cargar manualmente una tela
    @PostMapping("/mis-telas/cargar-tela")
    public String cargarTela(@RequestParam String tipoTela,
                             @RequestParam String color,
                             @RequestParam String imagenUrl,
                             RedirectAttributes redirectAttributes) {
        try {
            TipoTela tipo = TipoTela.valueOf(tipoTela.toUpperCase());
            MisTelas nueva = new MisTelas(generarId(), tipo, color, 0.0, imagenUrl);
            telasDelUsuario.add(nueva);
            redirectAttributes.addFlashAttribute("mensaje", "Tela guardada con éxito");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Tipo de tela inválido");
        }

        return "redirect:/cargar-tela";
    }

    // Comprar una tela desde el catálogo
    @GetMapping("/registrar-tela")
    public String registrarTelaDesdeCatalogo(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        MisTelas telaComprada = servicioTela.obtenerTelasDeFabrica().stream()
                .filter(tela -> tela.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (telaComprada == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Tela no encontrada en el catálogo");
        } else if (telasDelUsuario.stream().anyMatch(t -> t.getId().equals(id))) {
            redirectAttributes.addFlashAttribute("mensajeAdvertencia", "Ya has comprado esta tela");
        } else {
            telasDelUsuario.add(telaComprada);
            redirectAttributes.addFlashAttribute("mensaje", "Tela comprada con éxito");
        }

        return "redirect:/catalogo-telas";
    }

    // Mostrar el detalle para comprar una tela específica
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

    // Procesar compra y generar boleta
    @PostMapping("/procesar-compra")
    public String procesarCompra(@RequestParam String color,
                                 @RequestParam Double precio,
                                 @RequestParam Double metros,
                                 @RequestParam(required = false) String tipoTela,
                                 @RequestParam(required = false) String imagenUrl,
                                 RedirectAttributes redirectAttributes) {

        TipoTela tipo = null;
        if (tipoTela != null && !tipoTela.isEmpty()) {
            try {
                tipo = TipoTela.valueOf(tipoTela.toUpperCase());
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", "Tipo de tela inválido");
                return "redirect:/cargar-tela";
            }
        }

        MisTelas telaComprada = new MisTelas(generarId(), tipo, color, precio, imagenUrl);
        telaComprada.setMetros(metros);
        telasDelUsuario.add(telaComprada);

        double total = precio * metros;
        String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        redirectAttributes.addFlashAttribute("fecha", fechaFormateada);
        redirectAttributes.addFlashAttribute("color", color);
        redirectAttributes.addFlashAttribute("precio", precio);
        redirectAttributes.addFlashAttribute("metros", metros);
        redirectAttributes.addFlashAttribute("total", total);
        redirectAttributes.addFlashAttribute("mensaje", "Compra realizada con éxito");

        return "redirect:/cargar-tela";
    }


    // Método auxiliar para generar ID
    private Long generarId() {
        return (long) (telasDelUsuario.size() + 1000);
    }
}




