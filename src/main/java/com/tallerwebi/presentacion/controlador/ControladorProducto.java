package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.dto.DatosPrenda;
import com.tallerwebi.presentacion.dto.DatosProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Controller
public class ControladorProducto {
    private final ServicioPrenda servicioPrenda;
    private final ServicioTalle servicioTalle;
    private final ServicioTela servicioTela;
    private final ServicioArchivo servicioArchivo;
    private final ServicioProducto servicioProducto;
    private final ServicioPedido servicioPedido;

    @Autowired
    public ControladorProducto(ServicioProducto servicioProducto,
                               ServicioTalle servicioTalle,
                               ServicioPrenda servicioPrenda,
                               ServicioTela servicioTela,
                               ServicioArchivo servicioArchivo,
                               ServicioPedido servicioPedido) {
        this.servicioPrenda = servicioPrenda;
        this.servicioTalle = servicioTalle;
        this.servicioTela = servicioTela;
        this.servicioArchivo = servicioArchivo;
        this.servicioProducto = servicioProducto;
        this.servicioPedido = servicioPedido;
    }

    @RequestMapping(path = "/nuevo-pedido", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioDeProducto(RedirectAttributes redirectAttributes) {
        ModelMap model = new ModelMap();

        try {
            List<Prenda> listPrendas = servicioPrenda.obtenerTodas();

            if (listPrendas.isEmpty()) throw new TelaNoEncontrada();

            List<DatosPrenda> prendas = listPrendas.stream()
                    .map(prenda -> new DatosPrenda(prenda.getId(), prenda.getDescripcion(), prenda.getPrecioBase()))
                    .collect(Collectors.toList());

            model.put("producto", new DatosProducto());
            model.put("prendas", prendas);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Por el momento no hay prendas en stock");
            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("nuevo-pedido", model);
    }

    @RequestMapping(path = "/registrar-producto", method = RequestMethod.POST)
    public ModelAndView registrarProductoAlPedido(@ModelAttribute DatosProducto datosProducto,
                                                  HttpServletRequest request,
                                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        ModelMap model = new ModelMap();

        if (usuario == null) return new ModelAndView("redirect:/login");

        Prenda prenda = null;
        Talle talle = null;
        Tela tela = null;
        Archivo archivo = null;

        try {
            prenda = servicioPrenda.buscarPrendaPorId(datosProducto.getPrendaId());
            talle = servicioTalle.obtenerTalle(datosProducto.getTalleId());
            tela = servicioTela.obtenerTela(datosProducto.getTelaId());

            if (Stream.of(prenda, talle, tela).anyMatch(Objects::isNull)) {
                redirectAttributes.addFlashAttribute("mensajeError", "Materiales inválidos");
                return new ModelAndView("redirect:/nuevo-pedido");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al traer los materiales del pedido");
            return new ModelAndView("redirect:/nuevo-pedido");
        }

        try {
            archivo = servicioArchivo.registrarArchivo(datosProducto.getArchivo());
        } catch (ArchivoNoValido e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ingrese un archivo válido");
            return new ModelAndView("redirect:/nuevo-pedido");
        }

        try {
            Producto producto = servicioProducto.registrarProducto(datosProducto.getCantidad(), archivo, prenda, talle, tela);
            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            pedido.getProductos().add(producto);
            servicioPedido.asociarProductoPedido(pedido);

                try {
                    Double metrosNecesarios = talle.getMetrosTotales() * producto.getCantidad();
                    servicioTela.consumirTelaParaProducto(tela, metrosNecesarios, usuario);
                } catch (StockInsuficiente e) {
                    redirectAttributes.addFlashAttribute("mensajeError", "No hay stock suficiente de la tela seleccionada");
                    return new ModelAndView("redirect:/nuevo-pedido");
                } catch (TelaNoEncontrada e) {
                    redirectAttributes.addFlashAttribute("mensajeError", "Tela no encontrada");
                    return new ModelAndView("redirect:/nuevo-pedido");
                }


            model.put("pedido", pedido);
            return new ModelAndView("detalle-pedido", model);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al registrar el pedido");
            System.out.println(e.getMessage());
            return new ModelAndView("redirect:/nuevo-pedido");
        }
    }

}
