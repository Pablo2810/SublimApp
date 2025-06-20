package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Talle;

import java.util.List;

public interface ServicioTalle {
    List<Talle> buscarPrendaPorId(Long id);
}
