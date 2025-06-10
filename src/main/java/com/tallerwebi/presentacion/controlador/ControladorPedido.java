package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.presentacion.dto.DatosPedido;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
                                       @RequestParam("file") MultipartFile file) throws IOException {
        ModelMap model = new ModelMap();

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

        Usuario usuarioConectado = servicioUsuario.consultarUsuario("admin@unlam.edu.ar"); // guardar email en session
        // obtener hashset de productos

        Archivo archivo = servicioArchivo.registrarArchivo(datosPedido.getNombre(), file);
        Pedido pedido = servicioPedido.registrarPedido(UUID.randomUUID().toString(), usuarioConectado, new HashSet<>());

        model.put("pedidoNuevo", pedido);

        return new ModelAndView("detalle-pedido", model);
    }

    @RequestMapping("/historial-pedidos")
    public ModelAndView historialPedidos() {
        ModelMap model = new ModelMap();
        List<Pedido> pedidos = servicioPedido.listarPedidosDelUsuario(1L);

        model.put("mensajeSinPedidos", "Todavia no tienes pedidos");
        model.put("pedidos", pedidos);

        return new ModelAndView("historial-pedidos", model);
    }

}
