package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Prenda;
import com.tallerwebi.dominio.servicio.ServicioPrenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.GeneratedValue;
import java.util.List;

@Controller
public class ControladorPrenda {
    private ServicioPrenda servicioPrenda;

    @Autowired
    public ControladorPrenda(ServicioPrenda servicioPrenda) {
        this.servicioPrenda = servicioPrenda;
    }

    @GetMapping
    public List<Prenda> obtenerPrendas(){
        return servicioPrenda.obtenerTodas();
    }

}
