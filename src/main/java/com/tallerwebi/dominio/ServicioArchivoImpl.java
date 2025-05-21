package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;

@Service("servicioArchivo")
@Transactional
public class ServicioArchivoImpl  implements ServicioArchivo {
    @Override
    public Archivo registrarArchivo(String nombreArchivo, MultipartFile file) throws IOException {
        Archivo nuevoArchivo = new Archivo();
        Double pesoMB = this.transformarMB(file.getSize());
        nuevoArchivo.setNombre(nombreArchivo);
        nuevoArchivo.setTipoFormato(file.getContentType());
        nuevoArchivo.setPesoMB(pesoMB);
        nuevoArchivo.setDatos(file.getInputStream());
        return nuevoArchivo;
    }

    @Override
    public Double transformarMB(Long bytes) {
        double tamanioKB = bytes / 1024.0;
        double tamanioMB = tamanioKB / 1024.0;
        return Math.round(tamanioMB * 10.0) / 10.0;
    }

    @Override
    public Boolean validarFormato(String formato) {
        return formato.equals("image/jpg");
    }
}
