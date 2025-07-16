package com.tallerwebi.dominio.servicio;

import com.tallerwebi.presentacion.dto.ResultadoGenerarImagenIA;
import com.tallerwebi.presentacion.dto.ResultadoObtenerImagenIA;

public interface ServicioGeneradorImagenIA {
    ResultadoGenerarImagenIA generarImagenIA(String prompt, String preferenciaModelo);
    ResultadoObtenerImagenIA consultarImagenIA(String idProceso);
}
