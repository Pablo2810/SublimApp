package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Prenda;

import java.util.List;

public interface RepositorioPrenda {
    List<Prenda> obtener();

    Prenda buscarPorId(Long prendaId);
}
