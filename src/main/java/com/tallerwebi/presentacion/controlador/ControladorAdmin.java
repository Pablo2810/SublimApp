package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.ServicioStorageImagenImpl;
import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosTalle;
import com.tallerwebi.presentacion.dto.DatosTela;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ControladorAdmin {

    @Autowired
    private ServicioTela servicioTela;
    @Autowired
    private ServicioTalle servicioTalle;
    @Autowired
    private ServicioStorageImagenImpl servicioStorageImagen;

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

    // ABM Telas
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
            servicioTela.crearOActualizar(datosTela, archivo);

            String mensajeExito = id != null ? "Se edito la tela " + id : "Se creo la tela con exito";
            redirectAttributes.addFlashAttribute("mensaje", mensajeExito);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeAdvertencia", "No se pudo subir la imagen a la nube");
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

    @GetMapping("/borrar-tela/{id}")
    public ModelAndView borrarTela(@PathVariable() Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioTela.borrarTela(id);
            redirectAttributes.addFlashAttribute("mensaje", "Se borrado la tela" + id + "con exito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrio un error al borrar la tela");
        }

        return new ModelAndView("redirect:/admin/listar-telas");
    }

    // ABM Talles
    @GetMapping("/listar-talles")
    public ModelAndView listarTalles() {
        ModelMap model = new ModelMap();
        List<Talle> talles = servicioTalle.obtenerTalles();

        model.put("mensajeSinTalles", "No hay talles registrados");
        model.addAttribute("talles", talles);

        return new ModelAndView("listar-talles", model);
    }

    @PostMapping({"/editar-talle", "/editar-talle/{id}"})
    public ModelAndView editarOCrearTalle(@PathVariable(required = false) Long id,
                                          @ModelAttribute DatosTalle datosTalle,
                                          RedirectAttributes redirectAttributes) {

        try {
            servicioTalle.crearOActualizar(datosTalle);
            String mensajeExito = id != null ? "Se edito el talle " + id : "Se creo el talle con exito";
            redirectAttributes.addFlashAttribute("mensaje", mensajeExito);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrio un error al crear o actualizar el talle");
        }

        return new ModelAndView("redirect:/admin/listar-talles");
    }

    @GetMapping({"/editar-talle", "/editar-talle/{id}"})
    public ModelAndView editarOCrearTalle(@PathVariable(required = false) Long id) {
        ModelMap model = new ModelMap();
        Talle talle = new Talle();

        if (id != null) {
            talle = this.servicioTalle.obtenerTalle(id);
        }

        model.put("talle", talle);

        return new ModelAndView("editar-talle", model);
    }

    @GetMapping("/borrar-talle/{id}")
    public ModelAndView borrarTalle(@PathVariable() Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioTalle.borrarTalle(id);
            redirectAttributes.addFlashAttribute("mensaje", "Se borrado el talle" + id + "con exito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrio un error al borrar el talle");
        }

        return new ModelAndView("redirect:/admin/listar-talles");
    }

}
