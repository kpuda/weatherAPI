package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.WeatherNotFoundException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final TimeseriesServiceImpl timeseriesService;
    private final GeometryServiceImpl geometryService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void saveWeather(Weather weather) {
        if (geometryService.validateIfGeometryDoesntExists(
                geometryService.getGeometryIdByLatLon(
                        weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0))
        ))
            weatherRepository.save(weather);
    }

    @Override
    public void updateWeather(Weather weather) {

        weatherRepository.save(weather);
    }

    @Override
    public List<Weather> getWeatherForEveryLocation() {

        return weatherRepository.findAll();
    }

    @Override
    public Weather getWeatherByGeometryId(Long id) {
        return weatherRepository.findById(id).orElseThrow(WeatherNotFoundException::new);
    }

    @Override
    public void deleteWeatherByLatLon(Double lat, Double lon) {
        weatherRepository.deleteById(
                geometryService.validateIfGeometryExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)
                )
        );
    }

    @Override
    public Weather getNewWeatherOrder(String url, HttpEntity httpEntity) throws JsonProcessingException {
        ResponseEntity<String> weatherResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Weather weather = objectMapper.readValue(weatherResponse.getBody(), Weather.class);
        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(timeseries);
        return weather;
    }

    @Override
    public Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) {
        List<Timeseries> newTimeseriesList = timeseriesService.updateWeatherTimeseries(
                oldWeatherData.getProperties().getTimeseries(), refreshedWeatherData.getProperties().getTimeseries());
        newTimeseriesList.sort(Timeseries::compareTo);
        oldWeatherData.getProperties().setTimeseries(newTimeseriesList);
        return oldWeatherData;
    }
}
