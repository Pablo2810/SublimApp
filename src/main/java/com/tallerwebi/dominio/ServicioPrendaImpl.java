package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Prenda;
import com.tallerwebi.dominio.repositorio.RepositorioPrenda;
import com.tallerwebi.dominio.servicio.ServicioPrenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioPrenda")
@Transactional
public class ServicioPrendaImpl  implements ServicioPrenda {
    private RepositorioPrenda repositorioPrenda;

    @Autowired
    public ServicioPrendaImpl(RepositorioPrenda repositorioPrenda) {
        this.repositorioPrenda = repositorioPrenda;
    }

    @Override
    public List<Prenda> obtenerTodas() {
        return repositorioPrenda.obtener();
    }
}
