package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Service.GeometryService;
import com.kp.weatherAPI.Service.Impl.TimeseriesServiceImpl;
import com.kp.weatherAPI.Service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;


@RestController()
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    private final GeometryService geometryService;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
    private final static String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final static String USER_AGENT = "user-agent";

    @GetMapping("/new")
    Weather newWeatherOrder(@RequestParam Double lat, @RequestParam Double lon) throws IOException {
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        httpHeaders.set(USER_AGENT, url);
        return weatherService.getNewWeatherOrder(url,httpEntity);
    }

    @GetMapping("/all")
    public List<Weather> showAll() {
        log.info("Getting weather from every location saved");
        return weatherService.getWeatherForEveryLocation();
    }

    @GetMapping("/bygeo")
    public Weather weatherByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        return weatherService.getWeatherByGeometryId(
                geometryService.validateIfGeometryExists(
                        geometryService.getGeometryIdByLatLon(lat,lon)));
    }

    @Transactional
    @PostMapping("/saveweather")
    public Weather saveWeather(@RequestBody Weather weather) {
            weatherService.saveWeather(weather);
        return weather;
    }

    @Scheduled(fixedRateString = "PT30M")
    @Transactional
    @PutMapping("/update")
    public void updateWeatherEverwhere() throws IOException {
        List<Geometry> geometryList = geometryService.getGeometryList();
            for (Geometry geometry : geometryList) {
                Weather refreshedWeatherData = newWeatherOrder(geometry.getCoordinates().get(1), geometry.getCoordinates().get(0));
                Weather oldWeatherData = (weatherService.getWeatherByGeometryId(geometryService.validateIfGeometryExists(
                        geometryService.getGeometryIdByLatLon(
                                geometry.getCoordinates().get(1),
                                geometry.getCoordinates().get(0)))
                ));
                weatherService.updateWeather(
                        weatherService.compareTimeseriesData(refreshedWeatherData, oldWeatherData));
            }
    }
    @DeleteMapping("/deletelocationbylatlon")
    public void deleteLocationByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        weatherService.deleteWeatherByLatLon(lat,lon);
    }


}
