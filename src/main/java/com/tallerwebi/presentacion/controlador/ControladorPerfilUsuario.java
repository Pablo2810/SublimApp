package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Tela;
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

import javax.servlet.http.HttpServletRequest;

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
    private ServicioTela servicioTela; // servicio para obtener telas del usuario

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

        model.put("usuario", usuario);
        model.put("pedidos", pedidos);
        model.put("telas", servicioTela.obtenerTelas());

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
                                         @RequestParam(required = false) MultipartFile imagenPerfil) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);

        if (password != null && !password.isEmpty()) {
            if (passwordActual == null || !usuario.getPassword().equals(passwordActual)) {
                return new ModelAndView("redirect:/perfil-usuario?error=La contraseña actual es incorrecta.");
            }
            usuario.setPassword(password);
        }

        if (imagenPerfil != null && !imagenPerfil.isEmpty()) {
            Result resultado = servicioStorageImagen.subirImagen(imagenPerfil, "perfil_" + usuario.getId());
            usuario.setImagenPerfil(resultado.getUrl());
        }

        servicioUsuario.modificarUsuario(usuario);
        request.getSession().setAttribute("usuarioLogueado", usuario);

        return new ModelAndView("redirect:/perfil-usuario?exito=Datos actualizados correctamente.");
    }

    @PostMapping("/perfil-usuario/eliminar")
    public ModelAndView eliminarCuenta(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario != null) {
            servicioUsuario.eliminarUsuario(usuario);
            request.getSession().invalidate();
        }
        return new ModelAndView("redirect:/");
    }

}



