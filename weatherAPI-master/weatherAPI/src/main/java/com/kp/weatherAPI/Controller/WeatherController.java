package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Service.GeometryService;
import com.kp.weatherAPI.Service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController()
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    private final GeometryService geometryService;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
    private final static String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final static String USER_AGENT = "user-agent";

    @GetMapping("/new")
    Weather newWeatherOrder(@RequestParam Double lat, @RequestParam Double lon) throws IOException {
       log.info("passing lat and lon parameters to complete url string");
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        httpHeaders.set(USER_AGENT, url);
        return weatherService.getNewWeatherOrder(url,httpEntity);
    }

    @GetMapping("/all")
    public List<Weather> showAll() {
        log.info("Getting weather from every location saved in database");
        return weatherService.getWeatherForEveryLocation();
    }

    @GetMapping("/bygeo")
    public Weather getWeatherByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        log.info("Get weather by lat lon or throw WeatherNotFoundException");
        return weatherService.getWeatherByGeometryId(
                geometryService.validateIfGeometryExists(
                        geometryService.getGeometryIdByLatLon(lat,lon)));
    }

    @Transactional
    @PostMapping("/saveweather")
    public Weather saveWeather(@RequestBody Weather weather) {
        log.info("save weather or throw WeatherAlreadyExists exception");
            weatherService.saveWeather(weather);
        return weather;
    }

    @Scheduled(fixedRateString = "PT30M")
    @Transactional
    @PutMapping("/update")
    public void updateWeatherEverwhere() throws IOException {
        log.info("getting every location and updating weather forecast");
        List<Weather> weatherList = weatherService.getWeatherList();
        List<Weather> refreshedWeatherList= new ArrayList<>();

        weatherList.forEach(weather -> {
                    try {
                        Weather refreshedWeatherData = newWeatherOrder(weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0));
                        Weather oldWeatherData = (weatherService.getWeatherByGeometryId(geometryService.validateIfGeometryExists(
                                geometryService.getGeometryIdByLatLon(
                                        weather.getGeometry().getCoordinates().get(1),
                                        weather.getGeometry().getCoordinates().get(0))) ));
                        refreshedWeatherList.add(
                                weatherService.compareTimeseriesData(refreshedWeatherData,oldWeatherData));
                    } catch (IOException e) {e.printStackTrace();}
                });

        weatherService.updateAll(refreshedWeatherList);
    }
    @DeleteMapping("/deletelocationbylatlon")
    public void deleteLocationByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        log.info("deleting by lat and lon coords or else throw WeatherNotFound exception");
        weatherService.deleteWeatherByLatLon(lat,lon);
    }


}
