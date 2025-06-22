package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.repositorio.RepositorioTalle;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioTalle")
@Transactional
public class ServicioTalleImpl implements ServicioTalle {
    private RepositorioTalle repositorioTalle;

    @Autowired
    public ServicioTalleImpl(RepositorioTalle repositorioTalle) {
        this.repositorioTalle = repositorioTalle;
    }

    @Override
    public List<Talle> buscarPrendaPorId(Long id) {
        return repositorioTalle.buscarPorID(id);
    }

    @Override
    public Talle buscarTallePorId(Long talleId) {
        return repositorioTalle.buscarTallePorId(talleId);
    }
}
