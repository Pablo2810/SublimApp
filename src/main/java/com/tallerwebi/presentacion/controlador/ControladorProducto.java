package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Prenda;
import com.tallerwebi.dominio.entidad.Talle;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class ControladorProducto {
    private final ServicioPrenda servicioPrenda;

    @Autowired
    public ControladorProducto(
                               ServicioPrenda servicioPrenda) {
        this.servicioPrenda = servicioPrenda;
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
    public ModelAndView registrarProductoAlPedido(@ModelAttribute DatosProducto datosProducto){
        ModelMap model = new ModelMap();
        model.put("producto", datosProducto);
        return new ModelAndView("detalle-pedido", model);
    }
    /*
    @RequestMapping(path = "/personalizar", method = RequestMethod.POST)
    public ModelAndView generarProducto(@ModelAttribute("datosProducto") DatosProducto datosProducto,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        ModelMap model = new ModelMap();

        if (file == null || file.isEmpty()) {
            model.put("error", "Debe subir un archivo");
            return new ModelAndView("personalizar", model);
        }

        if (!Objects.equals(file.getContentType(), "image/jpeg")) {
            model.put("error", "Ingrese un archivo válido (.JPG o .JPEG)");
            return new ModelAndView("personalizar", model);
        }

        if (datosProducto.getCantidad() == null || datosProducto.getCantidad() < 1) {
            model.put("error", "Ingrese la cantidad de copias");
            return new ModelAndView("personalizar", model);
        }

        Archivo archivo = servicioArchivo.registrarArchivo(datosProducto.generarNombre(), file);
        model.put("archivo", archivo);

        return new ModelAndView("personalizar", model);
    }*/
}
