package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.infraestructura.RepositorioPedidoImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service("servicioPedido")
@Transactional
public class ServicioPedidoImpl implements ServicioPedido {

    RepositorioPedidoImpl repositorioPedido;

    public ServicioPedidoImpl() {
        this.repositorioPedido = new RepositorioPedidoImpl();
    }

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
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return repositorioPedido.listarPedidosDelUsuario(idUsuario);
    }

}
