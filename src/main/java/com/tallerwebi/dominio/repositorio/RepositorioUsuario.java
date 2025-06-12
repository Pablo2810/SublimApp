package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
}

