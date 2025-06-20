package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosTela;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ControladorAdmin {

    @Autowired
    private ServicioTela servicioTela;

    @GetMapping("/dashboard")
    public ModelAndView irAlDashboard(HttpServletRequest request) {
        if (request.getSession().getAttribute("ROL_USUARIO") == null) {
            return new ModelAndView("redirect:/login");
        }

        if (!request.getSession().getAttribute("ROL_USUARIO").equals("ADMIN")) {
            return new ModelAndView("home");
        }

        return new ModelAndView("home-admin");
    }

    @GetMapping("/listar-telas")
    public ModelAndView listarTelas() {
        ModelMap model = new ModelMap();
        List<Tela> telas = servicioTela.obtenerTelas();

        model.put("mensajeSinTelas", "No hay telas de fabrica registradas");
        model.addAttribute("telas", telas);

        return new ModelAndView("listar-telas", model);
    }

    @PostMapping({"/editar-tela", "/editar-tela/{id}"})
    public ModelAndView editarOCrearTela(@PathVariable(required = false) Long id,
                                         @ModelAttribute DatosTela datosTela,
                                         @RequestParam("imagen") MultipartFile archivo,
                                         RedirectAttributes redirectAttributes) {

        try {
            servicioTela.crearOActualizar(datosTela);
            String mensajeExito = id != null ? "Se edito la tela " + id : "Se creo la tela con exito";
            redirectAttributes.addFlashAttribute("mensaje", mensajeExito);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrio un error al crear o actualizar la tela");
        }

        return new ModelAndView("redirect:/admin/listar-telas");
    }

    @GetMapping({"/editar-tela", "/editar-tela/{id}"})
    public ModelAndView editarOCrearTela(@PathVariable(required = false) Long id) {
        ModelMap model = new ModelMap();
        Tela tela = new Tela();

        if (id != null) {
            tela = this.servicioTela.obtenerTela(id);
        }

        model.put("tela", tela);
        model.put("tiposTela", TipoTela.values());

        return new ModelAndView("editar-tela", model);
    }

    @PostMapping("/borrar-tela/{id}")
    public ModelAndView borrarTela(@PathVariable() Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioTela.borrarTela(id);
            redirectAttributes.addFlashAttribute("mensaje", "Se borrado la tela" + id + "con exito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrio un error al borrar la tela");
        }

        return new ModelAndView("redirect:/admin/listar-telas");
    }

}
