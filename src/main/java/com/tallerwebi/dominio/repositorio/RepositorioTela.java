package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Tela;

import java.util.List;

public interface RepositorioTela {
    List<Tela> listarTelas();
    Tela obtenerTela(Long id);
    void crearOActualizarTela(Tela tela);
    void borrarTela(Long id);
}
