package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.util.LogHelper;
import com.tallerwebi.presentacion.dto.DatosPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class ControladorPedido {

    private final ServicioArchivo servicioArchivo;
    private final ServicioPedido servicioPedido;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPedido(ServicioArchivo servicioArchivo, ServicioPedido servicioPedido, ServicioUsuario servicioUsuario) {
        this.servicioArchivo = servicioArchivo;
        this.servicioPedido = servicioPedido;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/nuevo-pedido", method = RequestMethod.GET)
    public ModelAndView nuevoPedido() {
        ModelMap model = new ModelMap();
        model.put("datosPedido", new DatosPedido());
        return new ModelAndView("nuevo-pedido", model);
    }

    @RequestMapping(path = "/detalle-pedido", method = RequestMethod.POST)
    public ModelAndView procesarPedido(@ModelAttribute("datosPedido") DatosPedido datosPedido,
                                       HttpSession session,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        ModelMap model = new ModelMap();
        Usuario usuario = servicioUsuario.consultarUsuario(session.getAttribute("userEmail").toString());

        if (file == null || file.isEmpty()) {
            model.put("error", "Debe subir un archivo");
            return new ModelAndView("nuevo-pedido", model);
        }

        if (datosPedido.getCantidadCopias() == null || datosPedido.getCantidadCopias() < 1) {
            model.put("error", "Ingrese la cantidad de copias");
            return new ModelAndView("nuevo-pedido", model);
        }

        if (!Objects.equals(file.getContentType(), "image/jpeg")) {
            model.put("error", "Ingrese un archivo válido (.JPG o .JPEG)");
            return new ModelAndView("nuevo-pedido", model);
        }

        Archivo archivo = servicioArchivo.registrarArchivo(datosPedido.getNombre(), file);
        Pedido pedido = servicioPedido.registrarPedido(datosPedido.getCantidadCopias(), archivo, usuario);

        model.put("pedidoNuevo", pedido);

        return new ModelAndView("detalle-pedido", model);
    }

    @RequestMapping(path = "/historial-pedidos", method = RequestMethod.GET)
    public ModelAndView mostrarHistorialPedidos(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = servicioUsuario.consultarUsuario(session.getAttribute("userEmail").toString());
        model.put("pedidos", servicioPedido.listarPedidosDelUsuario(usuario));
        return new ModelAndView("historial-pedidos", model);
    }
}
