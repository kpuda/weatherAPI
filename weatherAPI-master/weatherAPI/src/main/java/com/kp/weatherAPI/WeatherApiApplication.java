package com.kp.weatherAPI;

import com.google.gson.Gson;
import com.kp.weatherAPI.Entity.Timeseries;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

@SpringBootApplication
public class WeatherApiApplication {


    public static void main(String[] args) {


        SpringApplication.run(WeatherApiApplication.class, args);

    }
}
