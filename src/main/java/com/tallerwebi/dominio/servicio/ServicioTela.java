package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import javassist.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ServicioTela {

    List<MisTelas> obtenerTelasDeFabrica();
    List<Tela> obtenerTelas();
    Tela obtenerTela(Long id);
    void crearOActualizar(DatosTela datosTela);
    void borrarTela(Long id);
}
