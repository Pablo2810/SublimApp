package com.tallerwebi.dominio.cliente;

import com.tallerwebi.presentacion.dto.GenerarImagenIADTO;
import com.tallerwebi.presentacion.dto.ResultadoGenerarImagenIA;
import com.tallerwebi.presentacion.dto.ResultadoObtenerImagenIA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
public class GeneradorImagenIACliente {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${freepink.api}")
    private String apiKey;

    public ResultadoGenerarImagenIA generarImagen(String prompt, String model) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-freepik-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        GenerarImagenIADTO dto = new GenerarImagenIADTO(prompt, model);

        try {
            String url = "https://api.freepik.com/v1/ai/mystic";
            return restTemplate.postForEntity(url, dto, ResultadoGenerarImagenIA.class, headers).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultadoObtenerImagenIA consultarImagenIA(String idProceso) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-freepik-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String url = "https://api.freepik.com/v1/ai/mystic";
            return restTemplate.getForEntity(url, ResultadoObtenerImagenIA.class, idProceso, entity).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

}
