package com.kodigo.alltodo_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@PropertySource("classpath:/secrets.properties")
public class WebConfig implements WebMvcConfigurer {

    @Value("${CLIENT_PATH}")
    private String CLIENT_PATH;

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }*/

@Override
public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/**")
            .allowedOrigins(CLIENT_PATH)
            .allowedMethods("*")
            .maxAge(3600L)
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true);
}

}
