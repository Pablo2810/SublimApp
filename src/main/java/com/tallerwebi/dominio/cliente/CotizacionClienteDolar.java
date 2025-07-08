package com.tallerwebi.dominio.cliente;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class CotizacionClienteDolar {

    private static final String API_URL = "https://dolarapi.com/v1/dolares/blue";

    public double obtenerCotizacionVenta() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error al consultar la API del dólar: HTTP " + conn.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json.toString());
            return jsonNode.get("venta").asDouble();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la cotización del dólar: " + e.getMessage(), e);
        }
    }
}

