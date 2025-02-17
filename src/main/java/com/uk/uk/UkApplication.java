package com.uk.uk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class UkApplication
//        extends SpringBootServletInitializer
        implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(UkApplication.class, args);
        
        System.out.println("Sasi");
    }

    @Override
    public void run(String... args) throws Exception {

    }

//    @Bean
//    public WebMvcConfigurer configure() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry reg) {
//                reg.addMapping("/*").allowedOrigins("*");
//            }
//        };
//    }
}
