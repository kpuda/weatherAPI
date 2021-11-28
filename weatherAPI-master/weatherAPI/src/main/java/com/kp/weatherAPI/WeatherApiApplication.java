package com.kp.weatherAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class WeatherApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(WeatherApiApplication.class, args);
        for(String s : run.getBeanDefinitionNames()){
            System.out.println("Y "+s);
        }
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Docket get() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/**"))
                .apis(RequestHandlerSelectors.basePackage("com.kp.weatherAPI"))
                .build()
                .apiInfo(createApiInfo());
    }

    @Bean
    public static ObjectMapper getObjectMapper() {

        return new ObjectMapper();
    }

    private ApiInfo createApiInfo() {
        return new ApiInfo("Weather API",
                "Weather database",
                "0.5.2",
                null,
                new Contact("Krzysztof", "https://github.com/kpuda", "kpuda.contact@gmail.com"),
                null,
                null,
                Collections.emptyList()
        );
    }

}
