package com.kp.weatherAPI;

import com.google.gson.Gson;
import com.kp.weatherAPI.Entity.Timeseries;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@SpringBootApplication
public class WeatherApiApplication {

	public static void main(String[] args)   {
		SpringApplication.run(WeatherApiApplication.class, args);

	}

}
