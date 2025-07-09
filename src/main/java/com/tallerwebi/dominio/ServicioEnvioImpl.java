package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.TipoEnvio;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.servicio.ServicioEnvio;
import org.springframework.stereotype.Service;


@Service
public class ServicioEnvioImpl implements ServicioEnvio {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TipoEnvio determinarTipoEnvio(String jsonDireccion) {
        if (jsonDireccion == null || jsonDireccion.isBlank()) {
            // Sin dirección: retiro en local
            return TipoEnvio.LOCAL;
        }

        try {
            JsonNode root = objectMapper.readTree(jsonDireccion);
            JsonNode address = root.path("address");

            String city = address.path("city").asText("").toLowerCase();
            if (city.isEmpty()) {
                city = address.path("town").asText("").toLowerCase();
            }
            if (city.isEmpty()) {
                city = address.path("village").asText("").toLowerCase();
            }
            String state = address.path("state").asText("").toLowerCase();
            String country = address.path("country").asText("").toLowerCase();

            if (!"argentina".equals(country)) {
                return TipoEnvio.EXTERIOR;
            }

            String[] cabaStrings = {"capital federal", "caba", "ciudad autónoma de buenos aires"};
            for (String s : cabaStrings) {
                if (state.contains(s) || city.contains(s)) {
                    return TipoEnvio.CABA;
                }
            }

            return TipoEnvio.INTERIOR;

        } catch (Exception e) {
            // Si falla el parseo, asumimos retiro local para no bloquear el flujo
            return TipoEnvio.LOCAL;
        }
    }
}