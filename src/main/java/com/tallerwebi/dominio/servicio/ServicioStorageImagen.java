package com.tallerwebi.dominio.servicio;

import io.imagekit.sdk.models.results.Result;
import org.springframework.web.multipart.MultipartFile;

public interface ServicioStorageImagen {
    Result subirImagen(MultipartFile archivo, String nombreArchivo);
    Result subirImagen(MultipartFile archivo, String carpeta, String nombreArchivo);
    String modificarImagen(String urlPrendaBase, Result imagen);
}
