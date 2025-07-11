package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Transactional
public interface ServicioArchivo {
    Double transformarMB(Long bytes);
    Archivo registrarArchivo(MultipartFile file) throws ArchivoNoValido;
    //void obtenerDimensiones(MultipartFile file, Archivo archivo) throws IOException;
}
