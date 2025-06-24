package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Talle;

import java.util.List;

public interface RepositorioTalle {
    List<Talle> obtenerTalles();
    void crearOActualizarTalle(Talle talle);
    Talle obtenerTalle(Long id);
    void borrarTalle(Talle talle);
    List<Talle> buscarTallesDePrendaPorId(Long id);
    // Talle buscarTallePorId(Long id);
}
