package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServicioArchivoTest {

    @Test
    public void queSePuedaRegistrarUnArchivo() throws IOException {
        /*ServicioArchivoImpl servicioArchivo = new ServicioArchivoImpl();
        InputStream imgPrueba = getClass().getResourceAsStream("/img/fondo_home.jpg");
        //assertNotNull(imgPrueba, "No se encontró la imagen de prueba");
        Archivo archivo = new Archivo();
        MockMultipartFile file = new MockMultipartFile(
                "d",
                "camiseta.jpg",
                "image/jpeg",
                imgPrueba);

        archivo = servicioArchivo.registrarArchivo("Camiseta", file);

        assertThat(archivo.getNombre(), equalToIgnoringCase("Camiseta"));
        assertThat(archivo.getTipoFormato(), equalToIgnoringCase("image/jpeg"));*/
    }
}
