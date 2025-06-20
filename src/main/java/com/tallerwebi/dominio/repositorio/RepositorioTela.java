package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Tela;

import java.util.List;

public interface RepositorioTela {
    void guardarTelaFabrica();

    List<Tela> buscarTelasPorPrenda(Long id);
}
