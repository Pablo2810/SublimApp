package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service("servicioPedido")
@Transactional
public class ServicioPedidoImpl implements ServicioPedido {

    private final RepositorioPedido repositorioPedido;
    private final ServicioEmail servicioEmail;
    private final RepositorioProducto repositorioProducto;
    private final RepositorioTela repositorioTela;
    private final ServicioCotizacionDolar servicioCotizacionDolar;
    private final ServicioMaquina servicioMaquina;
    private final ServicioEnvio servicioEnvio;

    @Autowired
    public ServicioPedidoImpl(RepositorioPedido repositorioPedido, ServicioEmail servicioEmail,
                              RepositorioProducto repositorioProducto, RepositorioTela repositorioTela,
                              ServicioCotizacionDolar servicioCotizacionDolar,
                              ServicioMaquina servicioMaquina, ServicioEnvio servicioEnvio) {
        this.repositorioPedido = repositorioPedido;
        this.servicioEmail = servicioEmail;
        this.repositorioProducto = repositorioProducto;
        this.repositorioTela = repositorioTela;
        this.servicioCotizacionDolar = servicioCotizacionDolar;
        this.servicioMaquina = servicioMaquina;
        this.servicioEnvio = servicioEnvio;
    }

    @Override
    public Double calcularCostoTotal(Pedido pedido, double cotizacion) {
        Double precioTotal = 0.0;

        for (Producto producto : pedido.getProductos()) {
            precioTotal += producto.getPrecio() * producto.getCantidad();
        };

        return precioTotal;
    }

    @Override
    public void aplicarPromocion(Pedido pedido, Promocion promocion) {
        promocion.agregarPedido(pedido); // agrega a listado de pedidos en promoción y agrega promoción al pedido
        pedido.setMontoFinal(pedido.getMontoTotal() - promocion.getDescuento());
    }

    @Override
    public void generarPedidoCompleto(Long id, String tipoMonedaPago, double cotizacion, String codigoPedido, LocalDate fechaCreacion, int diasEspera) {
        Pedido pedido = obtenerPedido(id);
        pedido.setCodigoPedido(codigoPedido);
        pedido.setFechaCreacion(fechaCreacion);
        pedido.setFechaEntrega(LocalDate.now().plusDays(diasEspera));

        // Como ya no usas enum Moneda, seteamos null o podés agregar un atributo String para guardar tipoMonedaPago
        // pedido.setMonedaDePago(null);

        // Si quieres mantener la info de moneda, te recomiendo crear en Pedido:
        // private String monedaDePagoStr;
        // con sus getters/setters, y acá hacer:
        // pedido.setMonedaDePagoStr(tipoMonedaPago);

        // Calculamos el monto total considerando la moneda
        double montoTotal = calcularCostoTotal(pedido, cotizacion);
        pedido.setMontoTotal(montoTotal);

        // Si se paga en dólares, montoFinal es montoTotal dividido cotización, sino igual
        if ("DOLAR".equalsIgnoreCase(tipoMonedaPago)) {
            pedido.setMontoFinal(montoTotal / cotizacion);
        } else {
            pedido.setMontoFinal(montoTotal);
        }

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
            throw new RuntimeException(e);
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

    @Override
    public Pedido buscarPendiente(Usuario usuario) {
        return repositorioPedido.buscarPedidoPendientePorUsuario(usuario);
    }

    @Override
    public void eliminarPedido(Pedido pedidoEncontrado) {
        repositorioPedido.eliminar(pedidoEncontrado);
    }

    @Transactional
    @Override
    public Pedido procesarPagoPedidoProductos(Long pedidoId,
                                              boolean pagoEnDolares,
                                              String metodoPago,
                                              String opcionEnvioStr,
                                              String direccionEnvio,
                                              String numeroTarjeta,
                                              String cvv,
                                              String nombreTitular,
                                              String vencimiento,
                                              Integer cuotas) {

        Pedido pedido = obtenerPedido(pedidoId);
        if (pedido == null) throw new RuntimeException("Pedido no encontrado");

        // Obtener tipo de envío según lo que seleccionó el usuario
        TipoEnvio envio;
        try {
            envio = TipoEnvio.valueOf(opcionEnvioStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de envío inválido");
        }

        // Validar cuotas según método de pago
        if ("debito".equalsIgnoreCase(metodoPago)) {
            cuotas = 1;
        } else {
            if (cuotas == null || !List.of(1, 2, 3, 6, 12).contains(cuotas)) {
                throw new RuntimeException("Cantidad de cuotas inválida");
            }
        }

        // Obtener cotización del dólar
        double cotizacion = 1.0;
        if (pagoEnDolares) {
            try {
                cotizacion = servicioCotizacionDolar.obtenerCotizacionDolar();
            } catch (Exception e) {
                cotizacion = 1270.0; // fallback por si falla
            }
        }

        // Calcular monto productos (en pesos)
        double montoProductos = calcularCostoTotal(pedido, cotizacion);

        // Calcular costo de envío (en pesos)
        double costoEnvio = envio.getCosto();

        // Sumar productos + envío (en pesos)
        double montoFinalPesos = montoProductos + costoEnvio;

        // Si el pago es en dólares, convertir el monto final a dólares
        double montoFinal = pagoEnDolares ? (montoFinalPesos / cotizacion) : montoFinalPesos;

        // Validar tarjeta (simple)
        if (!numeroTarjeta.matches("\\d{16}")) throw new RuntimeException("Número de tarjeta inválido");
        if (!cvv.matches("\\d{3}")) throw new RuntimeException("CVV inválido");
        if (nombreTitular.trim().isEmpty()) throw new RuntimeException("Titular vacío");
        try {
            YearMonth fechaVenc = YearMonth.parse(vencimiento);
            if (fechaVenc.isBefore(YearMonth.now())) throw new RuntimeException("Tarjeta vencida");
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Fecha de vencimiento inválida");
        }

        // Setear datos al pedido
        pedido.setTipoEnvio(envio);
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setMetodoPago(metodoPago);
        pedido.setCuotas(cuotas);
        pedido.setPagoEnDolares(pagoEnDolares);
        pedido.setCotizacionDolar(pagoEnDolares ? cotizacion : 0.0);
        pedido.setCostoEnvio(costoEnvio);
        pedido.setDescripcionEnvio(envio.getDescripcion());
        pedido.setTiempoEntrega(envio.getTiempoEntrega());
        pedido.setMontoTotal(montoProductos); // sin envío
        pedido.setMontoFinal(montoFinal);     // total con envío

        // Seteo de estado y fechas
        pedido.setEstado(Estado.EN_ESPERA);
        pedido.setCodigoPedido(UUID.randomUUID().toString());
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setFechaEntrega(LocalDate.now().plusDays(servicioMaquina.calcularTiempoEspera()));

        repositorioPedido.actualizar(pedido);

        return pedido;
    }
}
