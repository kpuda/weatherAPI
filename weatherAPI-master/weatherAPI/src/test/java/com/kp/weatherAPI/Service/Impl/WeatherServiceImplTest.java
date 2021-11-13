package com.kp.weatherAPI.Service.Impl;

import com.kp.weatherAPI.Entity.*;
import com.kp.weatherAPI.Repository.GeometryRepository;
import com.kp.weatherAPI.Repository.TimeseriesRepository;
import com.kp.weatherAPI.Repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RequiredArgsConstructor
class WeatherServiceImplTest {

    @MockBean
    private WeatherRepository weatherRepository;
    @MockBean
    private TimeseriesRepository timeseriesRepository;
    @MockBean
    private GeometryRepository geometryRepository;
    @InjectMocks
    private WeatherServiceImpl weatherService;
    @InjectMocks
    private GeometryServiceImpl geometryService;
    @InjectMocks
    private TimeseriesServiceImpl timeseriesService;

    Weather weather = new Weather(
            1L, "Yes", new Geometry(
            1, "Type", List.of(10.0, 12.0, 3.0)),
            new Properties(
                    1L, new Meta(
                    1L, "now", new Units(
                    "111", "11", "213", "213", "12453", "123", "123")
            ), List.of(new Timeseries(
                    1L, "12321", new Data(new Instant(new Details(20.0, 10.0, 1.0, 2.0, 3.0, 2.0)))))));

    @Test
    void saveWeather(){
        weatherService.saveWeather(weather);
    }
    @Test
    void tryToSaveSameWeather(){
        weatherService.saveWeather(weather);
    }

}