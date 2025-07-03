package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;

import javax.transaction.Transactional;

@Transactional
public interface ServicioUsuario {

    Usuario consultarUsuario(String email);

    void modificarUsuario(Usuario usuario);

    boolean emailDisponible(String email);

}
