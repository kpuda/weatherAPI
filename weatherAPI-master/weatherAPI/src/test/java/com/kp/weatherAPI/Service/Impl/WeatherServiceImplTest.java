package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@RequiredArgsConstructor
class WeatherServiceImplTest {

    WeatherServiceImpl weatherService;
    WeatherRepository weatherRepository;
    HttpHeaders httpHeaders = new HttpHeaders();
    HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
    String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    String USER_AGENT = "user-agent";

    Double lat=50.0;
    Double lon=19.0;

    @Test
    void getData(){
        weatherRepository.findAll();
    }
    /*@Test
    void getNewWeatherOrder() throws JsonProcessingException {
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        httpHeaders.set(USER_AGENT, url);
        weatherService.getNewWeatherOrder(url,httpEntity);
    }*/
}