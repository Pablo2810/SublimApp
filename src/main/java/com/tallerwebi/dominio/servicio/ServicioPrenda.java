package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Prenda;

import java.util.List;

public interface ServicioPrenda {
    List<Prenda> obtenerTodas();

    Prenda buscarPrendaPorId(Long prendaId);
}
