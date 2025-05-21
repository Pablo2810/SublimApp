package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Archivo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioArchivo {
    Double transformarMB(Long bytes);
    Archivo registrarArchivo(String nombreArchivo, MultipartFile file) throws IOException;
    void obtenerDimensiones(MultipartFile file, Archivo archivo) throws IOException;
}
