package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Service.WeatherService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController()
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    private final static String WEATHER_NEW = "/weather/new";
    private final static String WEATHER_BY_LOCATION = "weather/geo";
    private final static String WEATHER_UPDATE = "/weather/update";
    private final static String WEATHER_DELETE_GEO = "/weather/delete/geo";
    private final static String WEATHER_LIST = "/weather/list";

    @ApiOperation(value = "Get new weather by lat and lon geometry parameters")
    @PostMapping(WEATHER_NEW)
    Weather newWeatherOrder(@RequestParam Double lat, @RequestParam Double lon) {
        log.info("passing lat and lon parameters to save new weather location");
        return weatherService.newWeather(lat, lon);
    }

    @ApiOperation(value = "Showing weather for every location stored in database")
    @GetMapping(WEATHER_LIST)
    public List<Weather> getWeatherList() {
        log.info("Getting weather from every location saved in database");
        return weatherService.getWeatherList();
    }

    @ApiOperation(value = "Showing weather for location stored in database by lat and lon geometry parameters")
    @GetMapping(WEATHER_BY_LOCATION)
    public Weather getWeatherByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        log.info("Get weather by lat lon or throw WeatherNotFoundException");
        return weatherService.getWeatherByGeometryId(lat, lon);
    }

    @ApiOperation(value = "Updating forecast for every location")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PutMapping(WEATHER_UPDATE)
    public void updateWeatherEverwhere() {
        log.info("getting every location and updating weather forecast");
        weatherService.updateAll();
    }

    @ApiOperation(value = "Deleting weather from database by lat and lon")
    @DeleteMapping(WEATHER_DELETE_GEO)
    public void deleteLocationByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        log.info("deleting by lat and lon coords or else throw WeatherNotFound exception");
        weatherService.deleteWeather(lat, lon);
    }


}
