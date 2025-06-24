package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorPerfilUsuario {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioPedido servicioPedido;

    @Autowired
    private ServicioTela servicioTela; // servicio para obtener telas del usuario

    @GetMapping("/perfil-usuario")
    public ModelAndView verPerfilUsuario(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();
        List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuario(usuario.getId());
        //List<Tela> telas = servicioTela.listarTelasDelUsuario(usuario.getId());

        model.put("usuario", usuario);
        model.put("pedidos", pedidos);
        //model.put("telas", telas);

        return new ModelAndView("perfil-usuario", model);
    }

    @PostMapping("/perfil-usuario/actualizar")
    public ModelAndView actualizarPerfil(
            HttpServletRequest request,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String password
    ) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        // Actualizar campos
        usuario.setNombre(nombre);
        usuario.setEmail(email);

        if (password != null && !password.isEmpty()) {
            // Aquí debes aplicar lógica para encriptar la contraseña si usás alguna
            usuario.setPassword(password);
        }

        servicioUsuario.modificarUsuario(usuario);

        // Actualizar sesión con datos nuevos
        request.getSession().setAttribute("usuarioLogueado", usuario);

        // Redirigir al perfil para evitar resubmits y mostrar datos actualizados
        return new ModelAndView("redirect:/perfil-usuario");
    }
}


