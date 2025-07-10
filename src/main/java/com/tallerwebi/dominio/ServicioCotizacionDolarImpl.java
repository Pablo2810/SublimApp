package com.tallerwebi.dominio;

import com.tallerwebi.dominio.cliente.CotizacionClienteDolar;
import com.tallerwebi.dominio.servicio.ServicioCotizacionDolar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioCotizacionDolarImpl implements ServicioCotizacionDolar {

    @Autowired
    private CotizacionClienteDolar cotizacionClienteDolar;

    @Override
    public double obtenerCotizacionDolar() {
        return cotizacionClienteDolar.obtenerCotizacionVenta();
    }

}
