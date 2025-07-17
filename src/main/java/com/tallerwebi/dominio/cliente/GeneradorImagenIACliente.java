package com.tallerwebi.dominio.cliente;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tallerwebi.presentacion.dto.GenerarImagenIADTO;
import com.tallerwebi.presentacion.dto.ResultadoGenerarImagenIA;
import com.tallerwebi.presentacion.dto.ResultadoObtenerImagenIA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


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

        GenerarImagenIADTO dto = new GenerarImagenIADTO();
        dto.setPrompt(prompt);
        dto.setStructure_strength(50);
        dto.setAdherence(50);
        dto.setHdr(50);
        dto.setResolution("1k");
        dto.setAspect_ratio("square_1_1");
        dto.setModel(model);
        dto.setCreative_detailing(33);
        dto.setEngine("automatic");
        dto.setFixed_generation(false);
        dto.setFilter_nsfw(true);

        HttpEntity<GenerarImagenIADTO> entity = new HttpEntity<>(dto, headers);


        String url = "https://api.freepik.com/v1/ai/mystic";

        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, ResultadoGenerarImagenIA.class).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultadoObtenerImagenIA consultarImagenIA(String idProceso) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-freepik-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.freepik.com/v1/ai/mystic/" + idProceso;

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, ResultadoObtenerImagenIA.class).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

}
