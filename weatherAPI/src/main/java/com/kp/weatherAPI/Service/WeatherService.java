package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public void saveWeather(Weather weather){
        weatherRepository.save(weather);
    }
}
