package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.presentacion.dto.DatosTalle;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ServicioTalle {
    List<Talle> obtenerTalles();
    void crearOActualizar(DatosTalle datosTalle);
    Talle obtenerTalle(Long id);
    void borrarTalle(Long id);
    /*
    List<Talle> buscarPrendaPorId(Long id);
    Talle buscarTallePorId(Long talleId);
    */
}
