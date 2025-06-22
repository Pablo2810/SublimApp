package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.repositorio.RepositorioArchivo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioArchivo")
public class RepositorioArchivoImpl implements RepositorioArchivo {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioArchivoImpl(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    @Override
    public Archivo guardar(Archivo archivo) {
        Long idGenerado = (Long) sessionFactory.getCurrentSession().save(archivo);
        return sessionFactory.getCurrentSession().get(Archivo.class, idGenerado);
    }
}
