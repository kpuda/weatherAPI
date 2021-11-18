package com.kp.weatherAPI.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kp.weatherAPI.Entity.Weather;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;


public interface WeatherService {

    void saveWeather(Weather weather);

    Weather getWeatherByGeometryId(double lat, double lon);

    void deleteWeather(Double lat, Double lon);

    Weather getNewWeatherOrder(Double lat, Double lon) throws JsonProcessingException;

    Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) throws IOException;

    List<Weather> getWeatherList();

    void updateAll();

    List<Weather> updateAsync();

    Weather newWeather(Double lat, Double lon);
}
