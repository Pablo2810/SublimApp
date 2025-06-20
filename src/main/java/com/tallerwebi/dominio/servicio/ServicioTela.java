package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
public interface ServicioTela {
    void agregarTelasDeFabrica();
    List<MisTelas> obtenerTelasDeFabrica();
    void dejarSinStockTelaDeFabrica();

    List<Tela> buscarPrendaPorId(Long prendaId);
}
