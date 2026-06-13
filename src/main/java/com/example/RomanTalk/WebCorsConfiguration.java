//package com.example.RomanTalk;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebCorsConfiguration {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:3000","http://localhost:3001","http://localhost:3002");
//            }
//        };
//    }
//
//}

// for vercel
package com.example.RomanTalk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://localhost:3001",
                                "http://localhost:3002",
                                "https://roman-talk-front-end.vercel.app"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}