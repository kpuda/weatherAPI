package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.ConflictException;
import com.kp.weatherAPI.Exceptions.NotFoundException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final TimeseriesServiceImpl timeseriesService;
    private final GeometryServiceImpl geometryService;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final static String GEOMETRY_NOT_FOUND = "Weather doesn't exists.";
    private final static String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final static String USER_AGENT = "user-agent";
    private final static String LAT_LON_OUT_BOUNDS = "The Lat Lon configuration is out of bounds. Value of latitude  range from -90 to 90° and -180° to 180° for longitude";


    @Transactional
    @Override
    public void weatherSave(Weather weather) {
        if (geometryService.validateIfGeometryByIdDoesntExists(
                geometryService.getGeometryIdByLatLon(
                        weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0)))) {
            weatherRepository.save(weather);
        }
    }


    @SneakyThrows
    @Transactional
    @Override
    @Scheduled(fixedRateString = "PT30M")
    public void updateAll() {
        log.info("Started scheduled updateAll()");
        Future<List<Weather>> weatherList = updateAsync();
        if (!weatherList.isDone()) {
            log.info("Async failed.");
        } else {
            log.info("Saving the updated list. Scheduled ended.");
            weatherRepository.saveAll(weatherList.get());
        }
    }

    @Override
    @Async("autoWeatherUpdate")
    public Future<List<Weather>> updateAsync() {
        List<Weather> weatherList = getWeatherList();
        log.info("Getting weather list.");
        List<Weather> refreshedWeatherList = new ArrayList<>();
        weatherList.forEach(weather -> {
            Weather refreshedWeatherData = getNewWeatherOrder(weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0));
            Weather oldWeatherData = (
                    getWeatherFromList(
                    weather.getGeometry().getCoordinates().get(1),
                    weather.getGeometry().getCoordinates().get(0),
                    weatherList));

            refreshedWeatherList.add(
                    compareTimeseriesData(refreshedWeatherData, oldWeatherData)
            );
        });

        return AsyncResult.forValue(refreshedWeatherList);
    }
    @Override
    public Weather getWeatherFromList(double lat, double lon, List<Weather> weatherList) {
        return weatherList.stream()
                .filter(weather -> weather.getGeometry().getCoordinates().get(1).equals(lat) && weather.getGeometry().getCoordinates().get(0).equals(lon))
                .findAny().get();
    }

    @SneakyThrows
    @Override
    public Weather getNewWeatherOrder(Double lat, Double lon) {
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        HttpResponse<String> weatherResponse = Unirest.post(url).header("Content-type", USER_AGENT).asString();
        Weather weather = objectMapper.readValue(weatherResponse.getBody(), Weather.class);
        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(timeseries);
        return weather;
    }

    @Override
    public Weather newWeatherOrder(Double lat, Double lon) {
        log.info("Waiting for weather to be fetched and passing it to weatherSave().");
        if (validateGeometryCompatibility(lat, lon)) {
            Weather weather = getNewWeatherOrder(lat, lon);
            weatherSave(weather);
            return weather;
        } else
            return null;
    }

    @Override
    public Boolean validateGeometryCompatibility(Double lat, Double lon) {
        if (lat <= 90 && lat >= -90 && lon <= 180 && lon >= -180) {
            return true;
        } else
            throw new ConflictException(LAT_LON_OUT_BOUNDS);
    }

    @Override
    public Weather getWeatherByGeometry(double lat, double lon) {
        log.info("Returning weather entity if exists");
        /*Weather weather = weatherRepository.findById(geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)))
                .orElseThrow(() -> new NotFoundException(GEOMETRY_NOT_FOUND));
        List<Timeseries> timeseries = weather.getProperties().getTimeseries();

        OptionalDouble average = timeseries.stream()
                .filter(time -> time.getTime().substring(0, 10).equals(String.valueOf(LocalDate.now())))
                .mapToDouble(timeseries1 -> timeseries1.getData().getInstant().getDetails().getAirTemperature()).average();
*/// TODO UNDER TESTING
        return weatherRepository.findById(geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)))
                .orElseThrow(() -> new NotFoundException(GEOMETRY_NOT_FOUND));
    }

    @Override
    public List<Weather> getWeatherList() {
        log.info("Returning weather list from database");
        List<Weather> weatherList = weatherRepository.findAll();
        return weatherRepository.findAll();
    }

    @Override
    public Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) {
        log.info("Updating forecast");
        List<Timeseries> newTimeseriesList = timeseriesService.updateWeatherTimeseries(
                oldWeatherData.getProperties().getTimeseries(), refreshedWeatherData.getProperties().getTimeseries());
        newTimeseriesList.sort(Timeseries::compareTo);
        oldWeatherData.getProperties().setTimeseries(newTimeseriesList);
        return oldWeatherData;
    }

    @Override
    public void deleteWeatherByGeometry(Double lat, Double lon) {
        log.info("Deleting weather for given location");
        weatherRepository.deleteById(
                geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)
                )
        );
    }

}
