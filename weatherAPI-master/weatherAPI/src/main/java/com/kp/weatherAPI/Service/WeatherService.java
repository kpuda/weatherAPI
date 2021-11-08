package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.WeatherAlreadyExistsException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final GeometryService geometryService;

    public WeatherService(WeatherRepository weatherRepository, GeometryService geometryService) {
        this.weatherRepository = weatherRepository;
        this.geometryService = geometryService;
    }

    public void saveWeather(Weather weather) {
        weatherRepository.save(weather);
    }

    public List<Weather> fetchAll() {
        return weatherRepository.findAll();
    }

    public Weather fetchWeatherByGeometryId(Long id) {
        return weatherRepository.findWeatherByGeoId(id);
    }

    public void deleteWeatherById(Long id) {
        weatherRepository.deleteById(id);
    }
}
