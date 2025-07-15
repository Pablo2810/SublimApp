package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import com.tallerwebi.dominio.servicio.ServicioEmail;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service("servicioPedido")
@Transactional
public class ServicioPedidoImpl implements ServicioPedido {

    RepositorioPedido repositorioPedido;
    ServicioEmail servicioEmail;
    RepositorioProducto repositorioProducto;
    RepositorioTela repositorioTela;

    @Autowired
    public ServicioPedidoImpl(RepositorioPedido repositorioPedido, ServicioEmail servicioEmail,
                              RepositorioProducto repositorioProducto, RepositorioTela repositorioTela) {
        this.repositorioPedido = repositorioPedido;
        this.servicioEmail = servicioEmail;
        this.repositorioProducto = repositorioProducto;
        this.repositorioTela = repositorioTela;
    }

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
            pedido.setFechaCreacion(LocalDate.now());
            pedido.setFechaEntrega(LocalDate.now().plusDays(3)); // Valor por defecto
            pedido.setCodigoPedido(UUID.randomUUID().toString());
            pedido.setMontoTotal(0.0);
            pedido.setMontoFinal(0.0);
            repositorioPedido.guardar(pedido);
        }
        return pedido;
    }


    @Override
    public Pedido obtenerPedido(Long id) {
        return repositorioPedido.obtenerPedido(id);
    }

    @Override
    public void asociarProductoPedido(Pedido pedido) {
        if (pedido == null || pedido.getProductos() == null) {
            throw new IllegalArgumentException("Pedido no válido o sin productos.");
        }

        double total = 0.0;

        for (Producto producto : pedido.getProductos()) {
            if (producto.getPrecio() == null) {
                throw new IllegalArgumentException("El producto tiene precio nulo.");
            }

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
                servicioEmail.enviarCorreoEstadoPedido(pedido.getUsuarioPedido().getEmail(), pedido,
                        ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/historial-pedidos")
                                .build()
                                .toUriString());
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

    @Override
    public void cancelarPedido(Long id) {
        Pedido pedido = obtenerPedido(id);

        if (pedido.getEstado().equals(Estado.PENDIENTE) || pedido.getEstado().equals(Estado.EN_ESPERA)) {
            pedido.setEstado(Estado.CANCELADO);
            repositorioPedido.actualizar(pedido);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void eliminarProductoDelPedido(Pedido pedido, Long productoId) {
        if (pedido == null || pedido.getProductos() == null) {
            throw new IllegalArgumentException("Pedido no válido o sin productos.");
        }

        Producto productoAEliminar = pedido.getProductos()
                .stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (productoAEliminar != null) {
            try {
                // Reponer stock
                Double metrosTela = productoAEliminar.getTela().getMetros();
                Double metrosCantidad = productoAEliminar.getTalle().getMetrosTotales() * productoAEliminar.getCantidad();
                productoAEliminar.getTela().setMetros(metrosTela + metrosCantidad);
                repositorioTela.actualizar(productoAEliminar.getTela());

                // Eliminar producto primero
                repositorioProducto.eliminarProducto(productoId);

                // Quitar producto de la colección y actualizar pedido
                pedido.getProductos().remove(productoAEliminar);
                repositorioPedido.actualizar(pedido);

            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar el producto del pedido", e);
            }
        } else {
            throw new IllegalArgumentException("Producto no encontrado en el pedido");
        }
    }

}
