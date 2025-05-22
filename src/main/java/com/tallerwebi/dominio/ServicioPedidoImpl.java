package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.util.LogHelper;
import com.tallerwebi.infraestructura.RepositorioPedidoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service("servicioPedido")
@Transactional
public class ServicioPedidoImpl implements ServicioPedido {

    private static final Logger log = LoggerFactory.getLogger(ServicioPedidoImpl.class);
    RepositorioPedidoImpl repositorioPedido;

    @Autowired
    public ServicioPedidoImpl(RepositorioPedidoImpl repositorioPedido) {
        this.repositorioPedido = repositorioPedido;
    }

    @Override
    public Pedido registrarPedido(Integer cantidadCopias, Archivo archivo, Usuario usuario) {
        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setId(1L);
        nuevoPedido.setFechaCreacion(LocalDate.now());
        nuevoPedido.setEstado(Estado.EN_ESPERA);
        nuevoPedido.setCantCopias(cantidadCopias);
        nuevoPedido.setCostoServicio(this.calcularCostoTotal(archivo.getAlto(), cantidadCopias));
        archivo.setAlto(archivo.getAlto() * cantidadCopias);
        nuevoPedido.setMetrosTotales(archivo.getAncho().toString() +"x"+ archivo.getAlto().toString());
        nuevoPedido.setArchivo(archivo);
        nuevoPedido.setUsuario(usuario);

        //repositorioPedido.agregarPedido(nuevoPedido);

        return nuevoPedido;
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
    public List<Pedido> listarPedidosDelUsuario(Usuario usuario) {
        return repositorioPedido.listarPedidosDelUsuario(usuario);
    }

}
