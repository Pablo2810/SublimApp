package com.tallerwebi.dominio;

import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Result subirImagen(MultipartFile archivo, String carpeta, String nombreArchivo) {
        try {
            FileCreateRequest fileCreateRequest = new FileCreateRequest(archivo.getBytes(), nombreArchivo);
            fileCreateRequest.setFolder(carpeta);
            return imageKit.upload(fileCreateRequest);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String modificarImagen(String urlPrendaBase, Result imagen) {
        List<Map<String, String>> transformation = new ArrayList<>();
        Map<String, String> overlay = new HashMap<>();
        Map<String, Object> options = new HashMap<>();

        overlay.put("l-image,i", imagen.getFilePath() + ",w-250,l-end");
        transformation.add(overlay);

        options.put("src", urlPrendaBase);

        options.put("transformation", transformation);

        return ImageKit.getInstance().getUrl(options);
    }

}
