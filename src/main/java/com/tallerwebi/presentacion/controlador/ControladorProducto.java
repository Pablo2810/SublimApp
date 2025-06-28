package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.dto.DatosPedido;
import com.tallerwebi.presentacion.dto.DatosProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    public ModelAndView mostrarFormularioDeProducto(){
        ModelMap model = new ModelMap();
        List<Prenda> prendas = servicioPrenda.obtenerTodas();
        model.put("producto", new DatosProducto());
        model.put("prendas", prendas);
        return new ModelAndView("nuevo-pedido", model);
    }

    @RequestMapping(path = "/registrar-producto", method = RequestMethod.POST)
    public ModelAndView registrarProductoAlPedido(@ModelAttribute DatosProducto datosProducto, HttpServletRequest request){
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
       // if(usuario == null){ return new ModelAndView("redirect:/login"); } //Usar esto en cada controlador

        ModelMap model = new ModelMap();
        Prenda prenda = servicioPrenda.buscarPrendaPorId(datosProducto.getPrendaId());
        Talle talle = servicioTalle.obtenerTalle(datosProducto.getTalleId()); // cambio buscarTallePorId por obtenerTalle
        Tela tela = null;
        try {
            tela = servicioTela.buscarTelaDelUsuario(datosProducto.getTelaId(), usuario);
        } catch (TelaNoEncontrada e) {
            List<Prenda> prendas = servicioPrenda.obtenerTodas();
            model.put("producto", new DatosProducto());
            model.put("prendas", prendas);
            model.put("error", "No tienes esta tela");
            return new ModelAndView("nuevo-pedido", model);
        }
        Archivo archivo = null;
        try {
            archivo = servicioArchivo.registrarArchivo(datosProducto.getArchivo());
        } catch (ArchivoNoValido e) {
            List<Prenda> prendas = servicioPrenda.obtenerTodas();
            model.put("producto", new DatosProducto());
            model.put("prendas", prendas);
            model.put("error", "Ingrese un archivo valido");
            return new ModelAndView("nuevo-pedido", model);
        }

        //Pedido PENDIENTE asociar al PRODUCTO NUEVO
        Producto producto = servicioProducto.registrarProducto(datosProducto.getCantidad(), archivo, prenda, talle, tela);
        Pedido pedido = servicioPedido.buscarPedidoEstadoPendiente(usuario);
        pedido.getProductos().add(producto);
        servicioPedido.asociarProductoPedido(pedido);

        model.put("pedido", pedido);
        return new ModelAndView("detalle-pedido", model);
    }
}
