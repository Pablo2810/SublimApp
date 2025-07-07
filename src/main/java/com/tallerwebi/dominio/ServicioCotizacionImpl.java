package com.tallerwebi.dominio;

import com.tallerwebi.dominio.cliente.CotizacionCliente;
import com.tallerwebi.dominio.servicio.ServicioCotizacion;
import com.tallerwebi.presentacion.dto.ResultadoCotizaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioCotizacionImpl implements ServicioCotizacion {

    @Autowired
    private CotizacionCliente cotizacionCliente;

    @Override
    public ResultadoCotizaciones obtenerCotizaciones() {
        return this.cotizacionCliente.obtenerCotizaciones();
    }

}
