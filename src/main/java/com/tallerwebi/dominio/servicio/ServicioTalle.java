package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.presentacion.dto.DatosMedida;
import com.tallerwebi.presentacion.dto.DatosTalle;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ServicioTalle {
    List<Talle> obtenerTalles();
    void crearOActualizar(DatosTalle datosTalle);
    Talle obtenerTalle(Long id);
    void borrarTalle(Long id);
    List<Talle> buscarTallesDePrendaPorId(Long id);

    Talle recomendarTalle(DatosMedida medidas);

    List<Talle> buscarTallesPorPais(String pais);

    Boolean entraEnElRango(Talle talle, double cintura, double pecho);

    double redondear(double valor);

    DatosMedida convertirPulgadasACentimetrosYRedondear(DatosMedida medidas);
    // Talle buscarTallePorId(Long talleId);
}
