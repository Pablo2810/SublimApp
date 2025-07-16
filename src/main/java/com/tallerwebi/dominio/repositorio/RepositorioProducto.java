package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioProducto {

    List<Producto> listarProductosDeUnPedido(Long idPedido);

    List<Pedido> listarProductosBase();

    Boolean eliminarProducto(Long idProducto);

    void actualizarProducto(Producto producto);

    Producto guardarProducto(Producto producto);

    Producto obtenerProducto(Long id);
}
