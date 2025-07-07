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

    // comento estos dos metodos que no se usan
//    @Override
//    public Pedido registrarPedido(String codigoPedido, Usuario usuario, HashSet<Producto> productos) {
//        LocalDate fecha = LocalDate.now();
//        long demora = 3L; // calcular por simulación
//
//        Pedido nuevoPedido = new Pedido();
//        nuevoPedido.setFechaCreacion(fecha);
//        nuevoPedido.setFechaEntrega(fecha.plusDays(demora));
//        nuevoPedido.setEstado(Estado.EN_ESPERA);
//        nuevoPedido.setProductos(productos);
//        nuevoPedido.setUsuarioPedido(usuario);
//        nuevoPedido.setMontoTotal(calcularCostoTotal(nuevoPedido));
//
//        repositorioPedido.guardarPedido(nuevoPedido);
//
//        return nuevoPedido;
//    }
//
//    @Override
//    public Pedido registrarPedidoConDescuento(String codigoPedido, Usuario usuario, HashSet<Producto> productos, Promocion promocion) {
//        Pedido pedido = registrarPedido(codigoPedido, usuario, productos);
//        aplicarPromocion(pedido, promocion);
//        return pedido;
//    }

    @Override
    public Double calcularCostoTotal(Pedido pedido, double cotizacion) {
        Double precioTotal = 0.0;

        for (Producto producto : pedido.getProductos()) {
            precioTotal += producto.getPrecio();
        };

        return precioTotal / cotizacion;
    }

    @Override
    public void aplicarPromocion(Pedido pedido, Promocion promocion) {
        promocion.agregarPedido(pedido); // agrega a listado de pedidos en promoción y agrega promoción al pedido
        pedido.setMontoFinal(pedido.getMontoTotal() - promocion.getDescuento());
    }

    @Override
    public void generarPedidoCompleto(Long id, Moneda moneda, double cotizacion, String codigoPedido, LocalDate fechaCreacion, int diasEspera) {
        Pedido pedido = obtenerPedido(id);
        pedido.setCodigoPedido(codigoPedido);
        pedido.setFechaCreacion(fechaCreacion);
        pedido.setFechaEntrega(LocalDate.now().plusDays(diasEspera));
        pedido.setMonedaDePago(moneda);
        pedido.setMontoTotal(calcularCostoTotal(pedido, cotizacion));
        pedido.setMontoFinal(pedido.getMontoTotal());
        repositorioPedido.actualizar(pedido);
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return repositorioPedido.listarPedidosDelUsuario(idUsuario);
    }

    @Override
    public List<Pedido> listarPedidosDelUsuarioNoPendiente(Long idUsuario) {
        return repositorioPedido.listarPedidosDelUsuarioNoPendiente(idUsuario);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return repositorioPedido.listarPedidos();
    }

    @Override
    public Pedido buscarPedidoEstadoPendiente(Usuario usuario){
        Pedido pedido = repositorioPedido.buscarPedidoPendientePorUsuario(usuario);
        if (pedido == null){
            pedido = new Pedido();
            pedido.setEstado(Estado.PENDIENTE);
            pedido.setUsuarioPedido(usuario);
            pedido.setProductos(new HashSet<>());
            repositorioPedido.guardar(pedido);
        }
        return pedido;
    }

    @Override
    public Pedido obtenerPedido(Long id) {
        return repositorioPedido.obtenerPedido(id);
    }

    @Override
    public void asociarProductoPedido(Pedido pedido){
        double total = 0.0;
        for (Producto producto: pedido.getProductos()) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        pedido.setMontoTotal(total);

        repositorioPedido.actualizar(pedido);
    }

    @Override
    public boolean cambiarEstadoPedido(Long id, Estado nuevoEstado) {
        try {
            /* Desarrollo anterior, evaluar
            Pedido pedido = repositorioPedido.buscarPorId(id);
            pedido.setEstado(Estado.EN_ESPERA);
            pedido.setFechaCreacion(LocalDate.now());
            repositorioPedido.actualizar(pedido);
            */

            Pedido pedido = obtenerPedido(id);

            if (puedeCambiarElEstado(pedido.getEstado(), nuevoEstado)) {
                repositorioPedido.cambiarEstadoPedido(pedido, nuevoEstado);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return false;
    }

    /*
    Evalua que se pueda cambiar el estado del pedido
    - una vez que el estado este en SUBLIMADO | EN_ESPERA | A_RETIRAR no puede volver a estar PENDIENTE
    */
    private boolean puedeCambiarElEstado(Estado estadoAnterior, Estado nuevoEstado) {
        return estadoAnterior.equals(Estado.PENDIENTE) && !nuevoEstado.equals(Estado.PENDIENTE) ||
                estadoAnterior.equals(Estado.EN_ESPERA) && nuevoEstado.equals(Estado.SUBLIMANDO) ||
                estadoAnterior.equals(Estado.SUBLIMANDO) && nuevoEstado.equals(Estado.A_RETIRAR);
    }

}
