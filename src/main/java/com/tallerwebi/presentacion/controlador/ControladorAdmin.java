package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller()
@RequestMapping("/admin")
public class ControladorAdmin {

    @Autowired
    private ServicioTela servicioTela;

    @GetMapping("/dashboard")
    public ModelAndView irAlDashboard(HttpServletRequest request) {
        if (request.getSession().getAttribute("ROL_USUARIO") == null) {
            return new ModelAndView("redirect:/login");
        }

        if(!request.getSession().getAttribute("ROL_USUARIO").equals("ADMIN")) {
            return new ModelAndView("home");
        }

        return new ModelAndView("home-admin");
    }

    @GetMapping("/listar-telas")
    public ModelAndView listarTelas() {
        ModelMap model = new ModelMap();
        List<MisTelas> telas = servicioTela.obtenerTelasDeFabrica();

        model.put("mensajeSinTelas", "No hay telas de fabrica registradas");
        model.addAttribute("telas", telas);

        return new ModelAndView("listar-telas", model);
    }

//    @GetMapping("/editar-tela/{id}")
//    public ModelAndView editarTela(@PathVariable("id") Long id) {
//        ModelMap model = new ModelMap();
//        Tela tela = this.servicioTela.getTelaById(id);
//
//        if (tela == null) {
//            return new ModelAndView("redirect:/admin/listar-telas");
//        }
//
//        model.put("tela", tela);
//        model.put("tiposTela", TipoTela.values());
//
//        return new ModelAndView("editar-tela", model);
//    }
//
//    @PutMapping("/editar-tela/{id}")
//    public ModelAndView editarTela(@PathVariable("id") Long id,
//                                   @ModelAttribute DatosTela datosTela,
//                                   @RequestParam("imagen") MultipartFile archivo) {
//        Tela tela = this.servicioTela.getTelaById(id);
//
//        if (tela == null) {
//            return new ModelAndView("redirect:/admin/listar-telas");
//        }
//
//        tela.setTipoTela(datosTela.getTipoTela());
//        tela.setMetros(datosTela.getMetros());
//        tela.setColor(datosTela.getColor());
//        tela.setPrecio(datosTela.getPrecio());
//        tela.setImagenUrl(datosTela.getImagenUrl());
//
//        //this.servicioTela.updateTela(tela);
//
//        return new ModelAndView("editar-tela");
//    }

    @PostMapping("/crear-tela")
    public ModelAndView crearTela(@ModelAttribute("tela") Tela tela) {
        return new ModelAndView("crear-tela");
    }

}
