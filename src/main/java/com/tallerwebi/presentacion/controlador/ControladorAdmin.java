package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.TelaUsuarioNoEncontrada;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosModificarPedido;
import com.tallerwebi.presentacion.dto.DatosTalle;
import com.tallerwebi.presentacion.dto.DatosTela;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ControladorAdmin {

    private final ServicioTela servicioTela;
    private final ServicioTalle servicioTalle;
    private final ServicioPedido servicioPedido;

    @Autowired
    public ControladorAdmin(ServicioTela servicioTela, ServicioTalle servicioTalle, ServicioPedido servicioPedido) {
        this.servicioTela = servicioTela;
        this.servicioTalle = servicioTalle;
        this.servicioPedido = servicioPedido;
    }

    @GetMapping("/dashboard")
    public ModelAndView irAlDashboard(HttpServletRequest request) {
        return new ModelAndView("home-admin");
    }

    // ABM Telas
    @GetMapping("/listar-telas")
    public ModelAndView listarTelas() {
        ModelMap model = new ModelMap();

        try {
            List<Tela> telas = servicioTela.obtenerTelas();

            if (telas.isEmpty()) throw new Exception();

            model.addAttribute("telas", telas);
        } catch (Exception e) {
            model.put("mensajeSinTelas", "No hay telas de fabrica registradas");
        }

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

    // Pedidos (listado y modificacion de estado)
    @GetMapping("/pedidos-solicitados")
    public ModelAndView pedidosSolicitados() {
        ModelMap model = new ModelMap();
        List<Pedido> pedidos = servicioPedido.listarPedidos();

        model.put("pedidos", pedidos);
        model.put("mensajeSinPedidos", "No hay pedidos solicitados");

        return new ModelAndView("pedidos-solicitados", model);
    }

    @GetMapping("/pedido/{id}")
    public ModelAndView verDetallePedido(@PathVariable() Long id) {
        ModelMap model = new ModelMap();
        Pedido pedido = servicioPedido.obtenerPedido(id);

        model.put("estados", Estado.values());
        model.put("pedido", pedido);

        return new ModelAndView("editar-pedido", model);
    }

    @PostMapping("/pedido/{id}")
    public ModelAndView cambiarEstadoPedido(@PathVariable() Long id,
                                            DatosModificarPedido datosModificarPedido,
                                            RedirectAttributes redirectAttributes) {

        try {
            Estado nuevoEstado = Arrays.stream(Estado.values())
                    .filter(estado -> estado.name().equals(datosModificarPedido.getEstado().name()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado"));

            if (servicioPedido.cambiarEstadoPedido(id, nuevoEstado)) {
                redirectAttributes.addFlashAttribute("mensaje", "Ahora el pedido " + id + " esta en estado " + nuevoEstado.getDescripcion());
            } else {
                redirectAttributes.addFlashAttribute("mensajeAdvertencia", "El estado del pedido ya no puede modificarse");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo cambiar el estado del pedido");
        }


        return new ModelAndView("redirect:/admin/pedidos-solicitados");
    }

    @GetMapping("/tela-usuario/{id}")
    public ModelAndView verDetalleTelaUsuario(@PathVariable Long id) {
        ModelMap model = new ModelMap();
        Tela tela = servicioTela.obtenerTela(id);

        if (!(tela instanceof TelaUsuario)) {
            model.put("mensajeError", "No se encontró la tela del usuario.");
            return new ModelAndView("redirect:/admin/dashboard", model);
        }

        model.put("tela", tela);
        model.put("estados", EstadoTela.values());

        return new ModelAndView("editar-envio-tela", model);
    }

    @PostMapping("/actualizar-envio-tela/{id}")
    public ModelAndView actualizarEstadoEnvioTela(@PathVariable Long id,
                                                  @RequestParam("estado") EstadoTela nuevoEstado,
                                                  RedirectAttributes redirectAttributes) {
        try {
            servicioTela.cambiarEstadoTela(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado de envío actualizado con éxito.");
        } catch (TelaUsuarioNoEncontrada e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se encontró la tela del usuario.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo actualizar el estado de la tela.");
        }
        return new ModelAndView("redirect:/admin/estado-envio-telas");
    }


    @GetMapping("/estado-envio-telas")
    public ModelAndView listarTelasUsuarioEstado() {
        ModelMap model = new ModelMap();
        List<TelaUsuario> telasUsuario = servicioTela.obtenerTelasUsuario(null); // null para todas o filtrar si querés
        model.put("telas", telasUsuario);
        model.put("estados", EstadoTela.values());
        return new ModelAndView("estado-envio-tela-admin", model);
    }
}
