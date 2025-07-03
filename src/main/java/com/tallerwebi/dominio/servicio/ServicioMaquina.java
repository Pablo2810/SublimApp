package com.tallerwebi.dominio.servicio;

import javax.transaction.Transactional;

@Transactional
public interface ServicioMaquina {
    Long calcularTiempoEspera();
}
