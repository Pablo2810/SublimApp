package com.tallerwebi.presentacion.controlador;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.servicio.ServicioPais;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import com.tallerwebi.presentacion.dto.DatosMedida;
import com.tallerwebi.presentacion.dto.DatosTalle;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorTalle {
    private ServicioTalle servicioTalle;
    private ServicioPais servicioPais;

    @Autowired
    public ControladorTalle(ServicioTalle servicioTalle, ServicioPais servicioPais) {
        this.servicioTalle = servicioTalle;
        this.servicioPais = servicioPais;
    }

    @GetMapping("/talles-por-prenda/{prendaId}")
    @ResponseBody
    public List<DatosTalle> obtenerTallesPorPrenda(@PathVariable("prendaId") Long prendaId) {
        List<Talle> talles = servicioTalle.buscarTallesDePrendaPorId(prendaId);
        return talles.stream()
                .map(t -> new DatosTalle(t.getId(), t.getDescripcion()))
                .collect(Collectors.toList());
    }

    @GetMapping("/talle-por-id/{talleId}")
    @ResponseBody
    public DatosTalle obtenerTallePorId(@PathVariable("talleId") Long talleId) {
        Talle talle = servicioTalle.obtenerTalle(talleId);
        return new DatosTalle(talle.getId(), talle.getDescripcion(), talle.getMetrosTotales());
    }

    @GetMapping("/guia-talles")
    public ModelAndView mostrarGuia() {
        ModelMap model = new ModelMap();
        String pais = "Argentina";
        model.put("paises", servicioPais.obtenerPaises());
        model.put("talles", servicioTalle.buscarTallesPorPais(pais));
        model.put("medidas", new DatosMedida());
        return new ModelAndView("guia-talles", model);
    }

    @PostMapping("/guia-talles/recomendado")
    public ModelAndView recomendarTalle(@ModelAttribute DatosMedida medidas){
        ModelMap model = new ModelMap();

        if(medidas.getUnidad().equals("in")){
            this.transformarPulgadasEnCentimetros(medidas);
        }

        Talle talleRecomendado = servicioTalle.recomendarTalle(medidas);

        if (talleRecomendado == null){
            model.put("error", "Las medidas que usaste no son validas");
        }
        model.put("paises", servicioPais.obtenerPaises());
        model.put("talles", servicioTalle.buscarTallesPorPais(medidas.getPais()));
        model.put("talleRecomendado", talleRecomendado);
        model.put("medidas", medidas);

        return new ModelAndView("guia-talles", model);
    }

    @GetMapping("/talles-por-pais")
    @ResponseBody
    public List<DatosTalle> obtenerTallesPorPais(@RequestParam String pais) {
        List<Talle> talles = servicioTalle.buscarTallesPorPais(pais);
        return talles.stream()
                .map(t -> new DatosTalle(
                        t.getId(),
                        t.getDescripcion(),
                        t.getCinturaMIN(),
                        t.getCinturaMAX(),
                        t.getPechoMIN(),
                        t.getPechoMAX(),
                        t.getMetrosTotales()
                ))
                .collect(Collectors.toList());
    }

    private void transformarPulgadasEnCentimetros(DatosMedida medidas) {
        medidas.setCintura(medidas.getCintura() * 2.54);
        medidas.setPecho(medidas.getPecho() * 2.54);
    }
}
