package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.presentacion.dto.DatosUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPerfil(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/perfil", method = RequestMethod.GET)
    public ModelAndView navegarPerfil(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = servicioUsuario.consultarUsuario(session.getAttribute("userEmail").toString());
        DatosUsuario datosUsuario = new DatosUsuario();
        datosUsuario.setEmail(usuario.getEmail());
        if (usuario.getNombre() != null) {
            datosUsuario.setNombre(usuario.getNombre());
        }
        model.put("datosUsuario", datosUsuario);
        return new ModelAndView("perfil", model);
    }

    @RequestMapping(path = "/guardar-perfil", method = RequestMethod.POST)
    public ModelAndView guardarPerfil(@ModelAttribute("datosUsuario") DatosUsuario datosUsuario,
                                    HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = servicioUsuario.consultarUsuario(session.getAttribute("userEmail").toString());
        servicioUsuario.modificarUsuario(usuario);
        model.put("datosUsuario", datosUsuario);
        return new ModelAndView("perfil", model);
    }

}
