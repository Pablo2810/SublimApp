package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioTelaImpl implements ServicioTela {

    @Override
    public List<MisTelas> obtenerTelasDeFabrica() {
        List<MisTelas> telas = new ArrayList<>();
        String[] colores = {"blanco", "negro", "rojo", "azul", "amarillo"};

        double precioW15 = 10000.0;
        double precioSET = 15000.0;
        long id = 1;

        for (String color : colores) {
            telas.add(new MisTelas(id++, TipoTela.W15, color, precioW15, "/img/w15_" + color + ".jpg"));
            telas.add(new MisTelas(id++, TipoTela.SET, color, precioSET, "/img/set_" + color + ".jpg"));
        }

        return telas;
    }
}