package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.CompraTela;
import com.tallerwebi.dominio.entidad.EstadoTela;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioCompraTela {

    void guardarTela(CompraTela compra);

    CompraTela obtenerCompraPorId(Long id);

    void eliminarCompra(CompraTela compra);


    List<CompraTela> buscarComprasPorUsuarioYEstado(Long usuarioId, EstadoTela estado);
}
