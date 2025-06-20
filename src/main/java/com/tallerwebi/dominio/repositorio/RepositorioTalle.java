package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Talle;

import java.util.List;

public interface RepositorioTalle {

    List<Talle> buscarPorID(Long id);
}
