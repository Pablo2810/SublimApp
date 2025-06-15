package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        nuevoPedido.setMontoTotal(calcularCostoTotal(nuevoPedido));

        repositorioPedido.guardarPedido(nuevoPedido);

        return nuevoPedido;
    }

    @Override
    public Pedido registrarPedidoConDescuento(String codigoPedido, Usuario usuario, HashSet<Producto> productos, Promocion promocion) {
        Pedido pedido = registrarPedido(codigoPedido, usuario, productos);
        aplicarPromocion(pedido, promocion);
        return pedido;
    }

    @Override
    public Double calcularCostoTotal(Pedido pedido) {
        Double precioTotal = 0.0;
        for(Producto producto: pedido.getProductos()) {
            precioTotal += producto.getPrecio();
        };
        return precioTotal;
    }

    @Override
    public void aplicarPromocion(Pedido pedido, Promocion promocion) {
        promocion.agregarPedido(pedido); // agrega a listado de pedidos en promoción y agrega promoción al pedido
        pedido.setMontoFinal(pedido.getMontoTotal() - promocion.getDescuento());
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return repositorioPedido.listarPedidosDelUsuario(idUsuario);
    }

}
