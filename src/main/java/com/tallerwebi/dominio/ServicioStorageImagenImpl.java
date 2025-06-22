package com.tallerwebi.dominio;

import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@PropertySource("classpath:config.properties")
public class ServicioStorageImagenImpl implements ServicioStorageImagen {

    @Autowired
    private ImageKit imageKit;

    @Override
    public Result subirImagen(MultipartFile archivo, String nombreArchivo) {
        try {
            FileCreateRequest fileCreateRequest = new FileCreateRequest(archivo.getBytes(), nombreArchivo);
            return imageKit.upload(fileCreateRequest);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
