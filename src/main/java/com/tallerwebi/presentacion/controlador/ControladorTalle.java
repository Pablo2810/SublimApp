package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import com.tallerwebi.presentacion.dto.DatosTalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorTalle {

    @Autowired
    private ServicioTalle servicioTalle;

    @GetMapping("/talles-por-prenda/{prendaId}")
    @ResponseBody
    public List<DatosTalle> obtenerTallesPorPrenda(@PathVariable("prendaId") Long prendaId) {
        List<Talle> talles = servicioTalle.buscarTallesDePrendaPorId(prendaId);
        return talles.stream()
                .map(t -> new DatosTalle(t.getId(), t.getDescripcion()))
                .collect(Collectors.toList());
    }
}
