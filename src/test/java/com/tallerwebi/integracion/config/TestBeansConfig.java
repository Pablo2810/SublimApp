package com.tallerwebi.integracion.config;

import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import io.imagekit.sdk.ImageKit;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeansConfig {

    @Bean
    public ImageKit imageKit() {
        return Mockito.mock(ImageKit.class);
    }

    @Bean
    public ServicioUsuario servicioUsuario() {
        return Mockito.mock(ServicioUsuario.class);
    }

    @Bean
    public ServicioPedido servicioPedido() {
        return Mockito.mock(ServicioPedido.class);
    }

    @Bean
    public ServicioTela servicioTela() {
        return Mockito.mock(ServicioTela.class);
    }

    @Bean
    public ServicioStorageImagen servicioStorageImagen() {
        return Mockito.mock(ServicioStorageImagen.class);
    }
}

