package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    Weather weather = new Weather(
            1L, "Yes", new Geometry(
            1, "Type", List.of(10.0, 12.0, 3.0)),
            new Properties(
                    1L, new Meta(
                    1L, "now", new Units(
                    "111", "11", "213", "213", "12453", "123", "123")
            ), List.of(new Timeseries(
                    1L, "12321", new Data(new Instant(new Details(20.0, 10.0, 1.0, 2.0, 3.0, 2.0)))))));
    @BeforeEach
    void createWeather(){

    }
    @Test
    void saveWeather() {

        weatherRepository.save(weather);
    }
    @Test
    void tryToSaveSameWeather(){
        weatherRepository.save(weather);
    }
}