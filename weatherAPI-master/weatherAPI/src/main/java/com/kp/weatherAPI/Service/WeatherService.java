package com.kp.weatherAPI.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kp.weatherAPI.Entity.Weather;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;


public interface WeatherService {

    Weather getWeatherByGeometry(double lat, double lon);

    Weather getWeatherFromList(double lat, double lon, List<Weather> weatherList);

    Weather getNewWeatherOrder(Double lat, Double lon) throws JsonProcessingException;

    Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) throws IOException;

    Weather newWeatherOrder(Double lat, Double lon);

    List<Weather> getWeatherList();

    Future<List<Weather>> updateAsync();

    Boolean validateGeometryCompatibility(Double lat, Double lon);

    void weatherSave(Weather weather);

    void updateAll();

    void deleteWeatherByGeometry(Double lat, Double lon);
}
