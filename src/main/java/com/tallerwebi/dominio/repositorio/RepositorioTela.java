package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface RepositorioTela {
    void guardarTelaFabrica();

    List<Tela> buscarTelasPorPrenda(Long id);

    Tela buscarTelaPorId(Long id, Usuario usuario);
}
