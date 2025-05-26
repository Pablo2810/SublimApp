package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;

public interface ServicioUsuario {

    Usuario consultarUsuario(String email);
    void modificarUsuario(Usuario usuario);
}
