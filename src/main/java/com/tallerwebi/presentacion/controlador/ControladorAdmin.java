package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.CompraTelaNoEncontrada;
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
    private final ServicioPedido servicioPedido;

    @Autowired
    public ControladorAdmin(ServicioTela servicioTela, ServicioPedido servicioPedido) {
        this.servicioTela = servicioTela;
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
            servicioTela.cambiarEstadoCompraTela(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado de envío actualizado con éxito.");
        } catch (CompraTelaNoEncontrada e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se encontró la compra.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo actualizar el estado.");
        }

        return new ModelAndView("redirect:/admin/envios-telas");
    }

    @GetMapping("/envios-telas")
    public ModelAndView listarTelasCompradasPorEstado() {
        ModelMap model = new ModelMap();

        // Telas activas (para cambiar estado)
        List<EstadoTela> estadosFiltrados = List.of(EstadoTela.EN_DEPOSITO, EstadoTela.EN_VIAJE);
        List<CompraTela> compras = servicioTela.obtenerComprasPorEstados(estadosFiltrados);

        // Telas entregadas (historial)
        List<CompraTela> comprasEntregadas = servicioTela.obtenerComprasPorEstados(List.of(EstadoTela.ENTREGADO));

        model.put("compras", compras); // Activas
        model.put("historialTelasEntregadas", comprasEntregadas); // Historial
        model.put("estados", EstadoTela.values());

        return new ModelAndView("estado-envio-tela-admin", model);
    }

}
