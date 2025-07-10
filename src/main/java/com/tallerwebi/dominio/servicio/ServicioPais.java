package com.tallerwebi.dominio.servicio;

import com.tallerwebi.presentacion.dto.DatosPais;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ServicioPais {
    List<DatosPais> obtenerPaises();
}
