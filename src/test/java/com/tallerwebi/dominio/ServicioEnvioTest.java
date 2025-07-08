package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.*;
import com.tallerwebi.dominio.entidad.TipoEnvio;
import org.junit.jupiter.api.*;


public class ServicioEnvioTest {

    private ServicioEnvioImpl servicioEnvio;

    @BeforeEach
    public void setup() {
        servicioEnvio = new ServicioEnvioImpl();
    }

    @Test
    void direccionNull_DeberiaRetornarLOCAL() {
        TipoEnvio tipo = servicioEnvio.determinarTipoEnvio(null);
        assertEquals(TipoEnvio.LOCAL, tipo);
    }

    @Test
    void direccionVacia_DeberiaRetornarLOCAL() {
        TipoEnvio tipo = servicioEnvio.determinarTipoEnvio("   ");
        assertEquals(TipoEnvio.LOCAL, tipo);
    }

    @Test
    void direccionEnCABA_DeberiaRetornarCABA() {
        String json = "{"
                + "\"display_name\": \"Av. Corrientes 1234, CABA, Argentina\","
                + "\"address\": {"
                + "\"city\": \"Buenos Aires\","
                + "\"state\": \"Ciudad Autónoma de Buenos Aires\","
                + "\"country\": \"Argentina\""
                + "}"
                + "}";

        TipoEnvio tipo = servicioEnvio.determinarTipoEnvio(json);
        assertEquals(TipoEnvio.CABA, tipo);
    }

    @Test
    void direccionInterior_DeberiaRetornarINTERIOR() {
        String json = "{"
                + "\"display_name\": \"Bv. San Juan 500, Córdoba, Argentina\","
                + "\"address\": {"
                + "\"city\": \"Córdoba\","
                + "\"state\": \"Córdoba\","
                + "\"country\": \"Argentina\""
                + "}"
                + "}";

        TipoEnvio tipo = servicioEnvio.determinarTipoEnvio(json);
        assertEquals(TipoEnvio.INTERIOR, tipo);
    }

    @Test
    void direccionExterior_DeberiaRetornarEXTERIOR() {
        String json = "{"
                + "\"display_name\": \"1600 Amphitheatre Parkway, California, USA\","
                + "\"address\": {"
                + "\"city\": \"Mountain View\","
                + "\"state\": \"California\","
                + "\"country\": \"United States\""
                + "}"
                + "}";

        TipoEnvio tipo = servicioEnvio.determinarTipoEnvio(json);
        assertEquals(TipoEnvio.EXTERIOR, tipo);
    }


    @Test
    void jsonInvalido_DeberiaRetornarLOCAL() {
        String jsonMal = "{ este json no cierra ni es válido }";

        TipoEnvio tipo = servicioEnvio.determinarTipoEnvio(jsonMal);
        assertEquals(TipoEnvio.LOCAL, tipo);
    }
}