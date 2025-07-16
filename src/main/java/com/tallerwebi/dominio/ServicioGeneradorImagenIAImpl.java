package com.tallerwebi.dominio;

import com.tallerwebi.dominio.cliente.GeneradorImagenIACliente;
import com.tallerwebi.dominio.servicio.ServicioGeneradorImagenIA;
import com.tallerwebi.presentacion.dto.ResultadoGenerarImagenIA;
import com.tallerwebi.presentacion.dto.ResultadoObtenerImagenIA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioGeneradorImagenIAImpl implements ServicioGeneradorImagenIA {

    @Autowired
    private GeneradorImagenIACliente cliente;

    @Override
    public ResultadoGenerarImagenIA generarImagenIA(String prompt, String preferenciaModelo) {
        return cliente.generarImagen(prompt, preferenciaModelo);
    }

    @Override
    public ResultadoObtenerImagenIA consultarImagenIA(String idProceso) {
        return cliente.consultarImagenIA(idProceso);
    }
}
