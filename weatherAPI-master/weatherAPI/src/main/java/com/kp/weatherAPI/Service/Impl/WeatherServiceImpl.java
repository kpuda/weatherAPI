package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.WeatherNotFoundException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final TimeseriesServiceImpl timeseriesService;
    private final GeometryServiceImpl geometryService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void saveWeather(Weather weather) {
        if (geometryService.validateIfGeometryByIdDoesntExists(
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
    public void updateAll(List<Weather> refreshedWeatherList) {
        weatherRepository.saveAll(refreshedWeatherList);
    }

    @Override
    public List<Weather> getWeatherForEveryLocation() {

        return weatherRepository.findAll();
    }

    @Override
    public Weather getWeatherByGeometryId(Long id) {
        log.info("Yes");
        return weatherRepository.findById(id).orElseThrow(WeatherNotFoundException::new);
    }

    @Override
    public void deleteWeatherByLatLon(Double lat, Double lon) {
        weatherRepository.deleteById(
                geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)
                )
        );
    }

    @SneakyThrows
    @Override
    public Weather getNewWeatherOrder(String url, HttpEntity httpEntity)  {
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

    @Override
    public List<Weather> getWeatherList() {
        return weatherRepository.findAll();
    }
}
