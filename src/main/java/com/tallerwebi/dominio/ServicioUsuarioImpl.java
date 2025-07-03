package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario(String email) {
        return repositorioUsuario.buscar(email);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        repositorioUsuario.modificar(usuario);
    }

    @Override
    public boolean emailDisponible(String email) {return !repositorioUsuario.existeEmail(email); }

}