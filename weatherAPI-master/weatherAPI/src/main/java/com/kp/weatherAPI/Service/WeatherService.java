package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Weather> fetchAll(){return weatherRepository.findAll();}

    public List<Weather> fetchByGeoId(Long id) {
        List<Weather> temp=weatherRepository.findWeatherByGeoId(id);
        List<Timeseries> timeseries = temp.get(0).getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        temp.get(0).getProperties().setTimeseries(timeseries);
        return temp;}

    public void deleteWeatherById(Long id) { weatherRepository.deleteById(id);}
}
