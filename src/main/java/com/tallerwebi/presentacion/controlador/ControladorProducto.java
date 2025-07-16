package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.dto.*;
import io.imagekit.sdk.models.results.Result;
import com.tallerwebi.presentacion.dto.DatosPrenda;
import com.tallerwebi.presentacion.dto.DatosProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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
    private final ServicioCotizacionDolar servicioCotizacionDolar;
    private final ServicioGeneradorImagenIA servicioGeneradorImagenIA;
    private final ServicioStorageImagen servicioStorageImagen;

    @Autowired
    public ControladorProducto(ServicioProducto servicioProducto,
                               ServicioTalle servicioTalle,
                               ServicioPrenda servicioPrenda,
                               ServicioTela servicioTela,
                               ServicioArchivo servicioArchivo,
                               ServicioPedido servicioPedido,
                               ServicioCotizacionDolar servicioCotizacionDolar,
                               ServicioGeneradorImagenIA servicioGeneradorImagenIA,
                               ServicioStorageImagen servicioStorageImagen) {
        this.servicioPrenda = servicioPrenda;
        this.servicioTalle = servicioTalle;
        this.servicioTela = servicioTela;
        this.servicioArchivo = servicioArchivo;
        this.servicioProducto = servicioProducto;
        this.servicioPedido = servicioPedido;
        this.servicioCotizacionDolar = servicioCotizacionDolar;
        this.servicioGeneradorImagenIA = servicioGeneradorImagenIA;
        this.servicioStorageImagen = servicioStorageImagen;
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

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        try {
            Prenda prenda = servicioPrenda.buscarPrendaPorId(datosProducto.getPrendaId());
            Talle talle = servicioTalle.obtenerTalle(datosProducto.getTalleId());
            Tela tela = servicioTela.obtenerTela(datosProducto.getTelaId());

            if (Stream.of(prenda, talle, tela).anyMatch(Objects::isNull)) {
                redirectAttributes.addFlashAttribute("mensajeError", "Materiales inválidos");
                return new ModelAndView("redirect:/nuevo-pedido");
            }

            Archivo archivo = servicioArchivo.registrarArchivo(datosProducto.getArchivo());

            Producto producto = servicioProducto.registrarProducto(datosProducto.getCantidad(), archivo, prenda, talle, tela);
            if (producto == null) {
                redirectAttributes.addFlashAttribute("mensajeError", "No se pudo registrar el producto");
                return new ModelAndView("redirect:/nuevo-pedido");
            }

            Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
            pedido.getProductos().add(producto);
            servicioPedido.asociarProductoPedido(pedido);

            String nombreImagen = usuario.getId().toString() + producto.getId().toString();
            Result imagenSubida = servicioStorageImagen.subirImagen(datosProducto.getArchivo(), "disenios_subidos", nombreImagen);
            producto.setImagenUrl(imagenSubida.getUrl());

            if (prenda.getImagenUrl() != null) {
                String urlPrendaConDisenio = servicioStorageImagen.modificarImagen(prenda.getImagenUrl(), imagenSubida);
                producto.setImagenPrendaConDisenioUrl(urlPrendaConDisenio);
            }
            servicioProducto.actualizarImagenProducto(producto.getId(), producto);

            try {
                if (talle == null || producto == null || talle.getMetrosTotales() == null) {
                    redirectAttributes.addFlashAttribute("mensajeError", "Datos inválidos");
                    return new ModelAndView("redirect:/nuevo-pedido");
                }
                Double metrosNecesarios = talle.getMetrosTotales() * producto.getCantidad();
                servicioTela.consumirTelaParaProducto(tela, metrosNecesarios, usuario);
            } catch (StockInsuficiente e) {
                redirectAttributes.addFlashAttribute("mensajeError", "No hay stock suficiente de la tela seleccionada");
                return new ModelAndView("redirect:/nuevo-pedido");
            } catch (TelaNoEncontrada e) {
                redirectAttributes.addFlashAttribute("mensajeError", "Tela no encontrada");
                return new ModelAndView("redirect:/nuevo-pedido");
            }

            try {
                double cotizacionDolar = servicioCotizacionDolar.obtenerCotizacionDolar();
                model.put("cotizacionDolar", cotizacionDolar);
            } catch (Exception e) {
                model.addAttribute("mensajeError", "Error al consultar cotización del dólar.");
                return new ModelAndView("redirect:/nuevo-pedido");
            }

            model.put("pedido", pedido);
            return new ModelAndView("detalle-pedido", model);

        } catch (ArchivoNoValido e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ingrese un archivo válido");
        } catch (StockInsuficiente e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No hay stock suficiente de la tela seleccionada");
        } catch (TelaNoEncontrada e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Tela no encontrada");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al registrar el pedido");
        }

        return new ModelAndView("redirect:/nuevo-pedido");
    }

    private void reponerStockTela(Long idProducto) {
        Producto productoEncontrado = servicioProducto.buscarPorId(idProducto);
        Double metrosTela = productoEncontrado.getTela().getMetros();
        Double metrosCantidadTalle = productoEncontrado.getTalle().getMetrosTotales() * productoEncontrado.getCantidad();
        productoEncontrado.getTela().setMetros(metrosTela + metrosCantidadTalle);
        servicioTela.actualizar(productoEncontrado.getTela());
    }

    @ResponseBody
    @GetMapping(path = "/generar-imagen")
    public ResultadoGenerarImagenIA generarImagen(@RequestParam("prompt") String prompt,
                                                  @RequestParam("preferenciaModelo") String modelo) {
        return servicioGeneradorImagenIA.generarImagenIA(prompt, modelo);
    }

    @ResponseBody
    @GetMapping(path = "/obtener-imagen-generada/{idProceso}")
    public ResultadoObtenerImagenIA obtenerImagenGenerada(@PathVariable("idProceso") String idProceso) {
        return servicioGeneradorImagenIA.consultarImagenIA(idProceso);
    }

}


