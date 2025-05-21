package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import java.util.ArrayList;
import java.util.List;

public interface ServicioTela {

    List<MisTelas> obtenerTelasDeFabrica();

}
