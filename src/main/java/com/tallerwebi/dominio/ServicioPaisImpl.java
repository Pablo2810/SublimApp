package com.tallerwebi.dominio;

import com.tallerwebi.dominio.servicio.ServicioPais;
import com.tallerwebi.presentacion.dto.DatosPais;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("servicioPais")
public class ServicioPaisImpl implements ServicioPais {

    private final RestTemplate restTemplate = new RestTemplate();
    private final List<String> paisesPermitidos = Arrays.asList("Argentina","Brazil","Spain","United States");

    @Override
    public List<DatosPais> obtenerPaises() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(
                "https://restcountries.com/v3.1/all?fields=name,flags",
                Object[].class
        );

        return Arrays.stream(response.getBody())
                .map(obj -> (java.util.LinkedHashMap<?, ?>) obj)
                .map(pais -> {
                    var nameMap = (java.util.LinkedHashMap<?, ?>) pais.get("name");
                    String nombre = (String) nameMap.get("common");

                    if (!paisesPermitidos.contains(nombre)) return null;

                    var flagsMap = (java.util.LinkedHashMap<?, ?>) pais.get("flags");
                    String url = (String) flagsMap.get("png");

                    return new DatosPais(nombre, url);
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }
}
