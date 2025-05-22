package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

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
        this.obtenerDimensiones(file, nuevoArchivo);

        return nuevoArchivo;
    }

    @Override
    public void obtenerDimensiones(MultipartFile file, Archivo archivo) throws IOException {
        BufferedImage imagen = ImageIO.read(file.getInputStream());
        Integer anchoPix = imagen.getWidth();
        Integer altoPix = imagen.getHeight();
        Integer dpi = 300; //Resolucion profesional

        Double ancho = (anchoPix / dpi) * 0.0254;
        Double largo = (altoPix / dpi) * 0.0254;

        //Transformado a MTS
        archivo.setAncho(Math.round(ancho * 10.0) / 10.0);
        archivo.setAlto((Math.round(largo * 10.0) / 10.0));
    }

    @Override
    public Double transformarMB(Long bytes) {
        double tamanioKB = bytes / 1024.0;
        double tamanioMB = tamanioKB / 1024.0;
        return Math.round(tamanioMB * 10.0) / 10.0;
    }
}
