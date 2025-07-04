package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.EstadoTela;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.TelaUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
public class ControladorPerfilUsuario {

    @Autowired
    private ServicioStorageImagen servicioStorageImagen;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioPedido servicioPedido;

    @Autowired
    private ServicioTela servicioTela;

    @GetMapping("/perfil-usuario")
    public ModelAndView verPerfilUsuario(HttpServletRequest request,
                                         @RequestParam(required = false) String exito,
                                         @RequestParam(required = false) String error) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();

        List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuario(usuario.getId());

        List<TelaUsuario> telasEntregadas = servicioTela.obtenerTelasUsuarioPorEstado(usuario.getId(), EstadoTela.ENTREGADO);

        model.put("usuario", usuario);
        model.put("pedidos", pedidos);
        model.put("telas", telasEntregadas);

        if (exito != null) model.put("exito", exito);
        if (error != null) model.put("error", error);

        return new ModelAndView("perfil-usuario", model);
    }

    @PostMapping("/perfil-usuario/actualizar")
    public ModelAndView actualizarPerfil(HttpServletRequest request,
                                         @RequestParam String nombre,
                                         @RequestParam String email,
                                         @RequestParam(required = false) String telefono,
                                         @RequestParam(required = false) String password,
                                         @RequestParam(required = false) String passwordActual,
                                         @RequestParam(required = false) MultipartFile imagenPerfil,
                                         RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        if (!email.equals(usuario.getEmail()) && !servicioUsuario.emailDisponible(email)) {
            redirectAttrs.addFlashAttribute("error", "El correo ya está en uso.");
            return new ModelAndView("redirect:/configuracion-perfil");
        }

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);

        if (password != null && !password.isEmpty()) {
            if (passwordActual == null || !usuario.getPassword().equals(passwordActual)) {
                redirectAttrs.addFlashAttribute("error", "La contraseña actual es incorrecta.");
                return new ModelAndView("redirect:/configuracion-perfil");
            }
            usuario.setPassword(password);
        }

        if (imagenPerfil != null && !imagenPerfil.isEmpty()) {
            Result resultado = servicioStorageImagen.subirImagen(imagenPerfil, "perfil_" + usuario.getId());
            usuario.setImagenPerfil(resultado.getUrl());
        }

        servicioUsuario.modificarUsuario(usuario);
        request.getSession().setAttribute("usuarioLogueado", usuario);

        redirectAttrs.addFlashAttribute("exito", "Datos actualizados correctamente.");
        return new ModelAndView("redirect:/configuracion-perfil");
    }

    @GetMapping("/configuracion-perfil")
    public ModelAndView configuracionPerfil(HttpServletRequest request,
                                            @ModelAttribute("exito") String exito,
                                            @ModelAttribute("error") String error) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();
        model.put("usuario", usuario);

        if (exito != null) model.put("exito", exito);
        if (error != null) model.put("error", error);

        return new ModelAndView("configuracion_perfil", model);
    }

    @GetMapping("/estado-envio-tela")
    public ModelAndView verEstadoEnvioTelas(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();

        List<TelaUsuario> telas = servicioTela.obtenerTelasUsuario(usuario.getId());

        model.put("usuario", usuario);
        model.put("telas", telas);

        return new ModelAndView("estado-envio-tela", model);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}




