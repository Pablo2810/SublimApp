package com.tallerwebi.config;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@org.springframework.context.annotation.Configuration
@PropertySource("classpath:config.properties")
public class ImageKitConfig {

    @Value("${imagekit.publicKey}")
    private String publicKey;
    @Value("${imagekit.privateKey}")
    private String privateKey;
    @Value("${imagekit.urlEndpoint}")
    private String urlEndpoint;

    @Bean
    public ImageKit init() {
        ImageKit imageKit = ImageKit.getInstance();
        Configuration config = new Configuration(publicKey, privateKey, urlEndpoint);
        imageKit.setConfig(config);
        return imageKit;
    }

}
