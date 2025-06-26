package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import com.tallerwebi.dominio.repositorio.RepositorioArchivo;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.util.Objects;

@Service("servicioArchivo")
@Transactional
public class ServicioArchivoImpl  implements ServicioArchivo {

    private final RepositorioArchivo repositorioArchivo;

    public ServicioArchivoImpl(RepositorioArchivo repositorioArchivo) { this.repositorioArchivo = repositorioArchivo; }

    @Override
    public Archivo registrarArchivo(MultipartFile file) throws ArchivoNoValido {
        if (!Objects.equals(file.getContentType(), "image/jpeg") || file.isEmpty()){
            throw new ArchivoNoValido();
        }
        Archivo archivo = new Archivo();
        archivo.setNombre(file.getOriginalFilename());
        archivo.setTipoFormato(file.getContentType());
        archivo.setPesoMB(this.transformarMB(file.getSize()));
        return repositorioArchivo.guardar(archivo);
    }

    @Override
    public Double transformarMB(Long bytes) {
        double tamanioKB = bytes / 1024.0;
        double tamanioMB = tamanioKB / 1024.0;
        return Math.round(tamanioMB * 10.0) / 10.0;
    }


}
