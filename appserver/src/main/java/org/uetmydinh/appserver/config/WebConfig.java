package org.uetmydinh.appserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CorsConfig corsConfig;

    public WebConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(corsConfig.getAllowedOrigins());
    }
}
