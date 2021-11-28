package com.kp.weatherAPI.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kp.weatherAPI.EntityDTO.WeatherDTO;
import com.kp.weatherAPI.Entity.Weather;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface WeatherService {

    WeatherDTO getWeatherByGeometry(double lat, double lon);

    Weather getWeatherFromList(double lat, double lon, List<Weather> weatherList);

    Weather getNewWeatherOrder(Double lat, Double lon) throws JsonProcessingException;

    Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) throws IOException;

    WeatherDTO newWeatherOrder(Double lat, Double lon);

    Set<String> getIncomingDates();

    WeatherDTO getWeatherForWeek(Weather weather);

    List<WeatherDTO> getWeatherListForIncomingDays();

    List<Weather> getWeatherList();

    void updateWeatherAsync() throws InterruptedException;

    Boolean validateGeometryCompatibility(Double lat, Double lon);

    void weatherSave(Weather weather);

    HttpStatus updateAll();

    void deleteWeatherByGeometry(Double lat, Double lon);
}
