package com.tallerwebi.dominio.cliente;

import com.tallerwebi.presentacion.dto.ResultadoCotizaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CotizacionCliente {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${exchangeRateApiKey}")
    private String apiKey;

    public ResultadoCotizaciones obtenerCotizaciones() {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/ARS";

        try {
            ResponseEntity<ResultadoCotizaciones> resultado = restTemplate.getForEntity(url, ResultadoCotizaciones.class);
            return resultado.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
