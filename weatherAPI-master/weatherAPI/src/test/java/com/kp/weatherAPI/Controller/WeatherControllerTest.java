package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Service.Impl.WeatherServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestClientTest
class WeatherControllerTest {



    @Autowired
    private WeatherServiceImpl weatherService;

    @SneakyThrows
    @Test
    public void dunno(){
        //Weather weather = weatherService.newWeatherOrder(50.0, 19.0);

       // List<Weather> weatherList=List.of(weather);

       // given(weatherService.getWeatherList()).willReturn(weatherList);

    }

}