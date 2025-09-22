package com.bsm.ease2bill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ✅ Configure static resource locations (CSS, JS, Images)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static files from /static folder
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Optional: If you want to serve files from external directory
        // registry.addResourceHandler("/uploads/**")
        //         .addResourceLocations("file:/path/to/uploads/");
    }
}