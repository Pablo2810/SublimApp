package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.infraestructura.RepositorioPedidoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service("servicioPedido")
@Transactional
public class ServicioPedidoImpl implements ServicioPedido {

    RepositorioPedido repositorioPedido;

    @Autowired
    public ServicioPedidoImpl(RepositorioPedido repositorioPedido) {
        this.repositorioPedido = repositorioPedido;
    }

    @Override
    public Pedido registrarPedido(String codigoPedido, Usuario usuario, HashSet<Producto> productos) {
        LocalDate fecha = LocalDate.now();
        long demora = 3L; // calcular por simulación

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setFechaCreacion(fecha);
        nuevoPedido.setFechaEntrega(fecha.plusDays(demora));
        nuevoPedido.setEstado(Estado.EN_ESPERA);
        nuevoPedido.setProductos(productos);
        nuevoPedido.setUsuarioPedido(usuario);

        Double precioTotal = 0.0;
        for(Producto producto: productos) {
            precioTotal += producto.getPrecio();
        }
        nuevoPedido.setMontoTotal(precioTotal);
        nuevoPedido.setMontoFinal(precioTotal); // setea por defecto el precio total, se modifica en el otro méto-do

        return nuevoPedido;
    }

    public Pedido registrarPedido(String codigoPedido, Usuario usuario, HashSet<Producto> productos, Promocion promocion) {
        Pedido pedido = registrarPedido(codigoPedido, usuario, productos);
        promocion.agregarPedido(pedido); // agrega a listado de pedidos en promoción y agrega promoción al pedido
        pedido.setMontoFinal(pedido.getMontoTotal() - promocion.getDescuento());
        return pedido;
    }

    @Override
    public Double calcularCostoTotal(Double alto, Integer cantidadCopias) {
        Double costoServicio = 1500.0;//Costo del servicio por defecto
        return (alto * cantidadCopias) * costoServicio;
    }

    @Override
    public Double aplicarDescuento() {
        return null;
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return repositorioPedido.listarPedidosDelUsuario(idUsuario);
    }

}
