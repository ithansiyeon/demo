package com.example.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${image.storage.tempDir}")
    private String imageStorageTempDir;

    @Value("${image.storage.Dir}")
    private String imageStorageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/temp/summernoteImage/**", "/summernoteImage/**")
                .addResourceLocations("file:///"+imageStorageTempDir, "file:///"+imageStorageDir);
    }
}
