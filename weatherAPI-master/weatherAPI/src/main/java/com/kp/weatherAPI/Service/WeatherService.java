package com.kp.weatherAPI.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kp.weatherAPI.Entity.Weather;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;


public interface WeatherService {

    void saveWeather(Weather weather);


    List<Weather> getWeatherForEveryLocation();

    Weather getWeatherByGeometryId(Long id);

    void deleteWeatherByLatLon(Double lat, Double lon);

    Weather getNewWeatherOrder(String url, HttpEntity httpEntity) throws JsonProcessingException;

    Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) throws IOException;

    void updateWeather(Weather weather);


    List<Weather> getWeatherList();

    void updateAll(List<Weather> refreshedWeatherList);
}
