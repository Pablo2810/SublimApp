package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
}
