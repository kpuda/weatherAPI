package com.kp.weatherAPI;

import com.google.gson.Gson;
import com.kp.weatherAPI.Entity.Timeseries;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@SpringBootApplication
@EnableSwagger2
public class WeatherApiApplication {

    public static void main(String[] args) {


        SpringApplication.run(WeatherApiApplication.class, args);

    }
    @Bean
    public Docket get(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(createApiInfo());
    }

    private ApiInfo createApiInfo() {
        return new ApiInfo("Weather API",
                "Weather database",
                "1.00",
                "http://kp.pl",
                new Contact("Krzysztof","http://kp.pl","kpuda.contact@gmail.com"),
                "my own license",
                "http://kp.pl",
                Collections.emptyList()
                );
    }
}
