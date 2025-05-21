package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;

@Service("servicioPedido")
@Transactional
public class ServicioPedidoImpl implements ServicioPedido {

    @Override
    public Pedido registrarPedido(Integer cantidadCopias, Archivo archivo) {
        Pedido nuevoPedido = new Pedido();
        if (archivo != null && cantidadCopias >= 1){
            nuevoPedido.setId(1L);
            nuevoPedido.setFechaCreacion(LocalDate.now());
            nuevoPedido.setEstado(Estado.EN_ESPERA);
            nuevoPedido.setCantCopias(cantidadCopias);
            nuevoPedido.setArchivo(archivo);
            return nuevoPedido;
        }
        return null;
    }

    @Override
    public String calcularMetros(Pedido pedido) throws IOException {
        BufferedImage imagen = ImageIO.read(pedido.getArchivo().getDatos());
        Integer anchoPix = imagen.getWidth();
        Integer altoPix = imagen.getHeight();
        Integer dpi = 300; //Resolucion profesional

        Double ancho = (anchoPix / dpi) * 0.0254;
        Double alto = (altoPix / dpi) * 0.0254;
        pedido.setAncho(Math.round(ancho * 10.0) / 10.0);
        pedido.setAlto((Math.round(alto * 10.0) / 10.0) * pedido.getCantCopias());
        return pedido.getAncho().toString() +" x "+ pedido.getAlto().toString();
    }

    @Override
    public Double calcularCostoTotal(Double alto) {
        Double costoServicio = 1500.0;//Costo del servicio por defecto
        return alto * costoServicio;
    }

    @Override
    public Double aplicarDescuento() {
        return null;
    }
}
