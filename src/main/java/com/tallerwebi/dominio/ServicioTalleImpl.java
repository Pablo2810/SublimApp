package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.presentacion.dto.DatosTalle;
import org.springframework.stereotype.Service;
import com.tallerwebi.dominio.repositorio.RepositorioTalle;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ServicioTalleImpl implements ServicioTalle {

    @Autowired
    private RepositorioTalle repositorioTalle;

    @Override
    public List<Talle> obtenerTalles() {
        return repositorioTalle.obtenerTalles();
    }

    @Override
    public void crearOActualizar(DatosTalle datosTalle) {
        Talle talle = new Talle();

        if (datosTalle.getId() != 0) {
            try {
                talle = obtenerTalle(datosTalle.getId());
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        talle.setDescripcion(datosTalle.getDescripcion());
        talle.setMetrosTotales(datosTalle.getMetrosTotales());

        repositorioTalle.crearOActualizarTalle(talle);
    }

    @Override
    public Talle obtenerTalle(Long id) {
        return repositorioTalle.obtenerTalle(id);
    }

    @Override
    public void borrarTalle(Long id) {
        try {
            Talle talle = obtenerTalle(id);
            repositorioTalle.borrarTalle(talle);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
