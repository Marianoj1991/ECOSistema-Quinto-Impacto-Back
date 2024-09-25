package com.quintoimpacto.ecosistema.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.url}")
    private String url;


    //TODO completar cuando sea necesario(allowedHeaders, etc.)
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins(url)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","HEAD","CONNECT")
                .allowedHeaders("*")
                .exposedHeaders("authorization")
                .allowCredentials(true);
    }
}
