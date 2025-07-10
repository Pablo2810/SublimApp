package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.TipoEnvio;

public interface ServicioEnvio {

    TipoEnvio determinarTipoEnvio(String direccion) throws Exception;

}

