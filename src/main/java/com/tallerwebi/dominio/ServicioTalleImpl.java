package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.presentacion.dto.DatosMedida;
import com.tallerwebi.presentacion.dto.DatosTalle;
import org.springframework.stereotype.Service;
import com.tallerwebi.dominio.repositorio.RepositorioTalle;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service("servicioTalle")
@Transactional
public class ServicioTalleImpl implements ServicioTalle {

    @Autowired
    private RepositorioTalle repositorioTalle;

    @Autowired
    public ServicioTalleImpl(RepositorioTalle repositorioTalle) {
        this.repositorioTalle = repositorioTalle;
    }

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

    @Override
    public List<Talle> buscarTallesDePrendaPorId(Long id) {
        return repositorioTalle.buscarTallesDePrendaPorId(id);
    }

    @Override
    public Talle recomendarTalle(DatosMedida medidas) {
        List<Talle> talles = repositorioTalle.buscarTallesPorPais(medidas.getPais());
        talles.sort(Comparator.comparing(Talle::getMetrosTotales)); // O por descripción si preferís

        double cintura = redondear(medidas.getCintura());
        double pecho = redondear(medidas.getPecho());

        for (int i = 0; i < talles.size(); i++) {
            Talle talle = talles.get(i);
            if (this.entraEnElRango(talle, cintura, pecho)) {
                if (medidas.getPreferencia().equalsIgnoreCase("ajustado")) {
                    return talle;
                } else if (medidas.getPreferencia().equalsIgnoreCase("holgado")) {
                    if (i + 1 < talles.size()) {
                        return talles.get(i + 1);
                    } else {
                        return talle;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Talle> buscarTallesPorPais(String pais) {
        return repositorioTalle.buscarTallesPorPais(pais);
    }

    @Override
    public Boolean entraEnElRango(Talle talle, double cintura, double pecho) {
        double margen = 0.5;
        return (cintura >= (talle.getCinturaMIN() - margen) && cintura <= (talle.getCinturaMAX() + margen)) &&
                (pecho >= (talle.getPechoMIN() - margen) && pecho <= (talle.getPechoMAX() + margen));
    }

    @Override
    public double redondear(double valor) {
        return Math.round(valor * 10.0) / 10.0;
    }

    @Override
    public DatosMedida convertirPulgadasACentimetrosYRedondear(DatosMedida medidas) {
        medidas.setCintura(redondear(medidas.getCintura() * 2.54));
        medidas.setPecho(redondear(medidas.getPecho() * 2.54));
        return medidas;
    }

}
