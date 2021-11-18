package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.NotFoundException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final TimeseriesServiceImpl timeseriesService;
    private final GeometryServiceImpl geometryService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String GEOMETRY_NOT_FOUND = "Weather doesn't exists.";
    private final static String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final static String USER_AGENT = "user-agent";


    @Transactional
    @Override
    public void saveWeather(Weather weather) {
        if (geometryService.validateIfGeometryByIdDoesntExists(
                geometryService.getGeometryIdByLatLon(
                        weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0)))) {
            weatherRepository.save(weather);
        }
    }

    @Transactional
    @Override
    @Scheduled(fixedRateString = "PT30M")
    public void updateAll() {
        weatherRepository.saveAll(updateAsync());
    }


    @Override
    public Weather getWeatherByGeometryId(double lat, double lon) {
        log.info("kurwa");
        return weatherRepository.findById(geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)))
                .orElseThrow(() -> new NotFoundException(GEOMETRY_NOT_FOUND));
    }

    @Override
    public void deleteWeather(Double lat, Double lon) {
        weatherRepository.deleteById(
                geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)
                )
        );
    }

    @SneakyThrows
    @Override
    public Weather getNewWeatherOrder(Double lat, Double lon) {
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        HttpResponse<String> weatherResponse= Unirest.post(url).header("Content-type",USER_AGENT).asString();
        Weather weather = objectMapper.readValue(weatherResponse.getBody(), Weather.class);
        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(timeseries);
        return weather;
    }

    @Override
    public Weather newWeather(Double lat, Double lon) {
        Weather weather = getNewWeatherOrder(lat, lon);
        saveWeather(weather);
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

    @Async
    @Override
    public List<Weather> updateAsync() {
        List<Weather> weatherList = getWeatherList();
        List<Weather> refreshedWeatherList = new ArrayList<>();
        weatherList.forEach(weather -> {
            Weather refreshedWeatherData = getNewWeatherOrder(weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0));
            Weather oldWeatherData = (getWeatherByGeometryId(
                    weather.getGeometry().getCoordinates().get(1),
                    weather.getGeometry().getCoordinates().get(0)));
            refreshedWeatherList.add(
                    compareTimeseriesData(refreshedWeatherData, oldWeatherData));
        });

        return refreshedWeatherList;
    }

}
