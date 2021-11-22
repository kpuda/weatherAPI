package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.EntityDTO.WeatherDTO;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Service.WeatherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController("/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    private final static String WEATHER_NEW = "/new";
    private final static String WEATHER_FIND = "/find";
    private final static String WEATHER_UPDATE = "/update";
    private final static String WEATHER_DELETE = "/delete";
    private final static String WEATHER_LIST = "/list";


    @ApiOperation(value = "Get weather forecast list",
            notes = "Showing weather for every location stored in database",
            response = Weather.class)
    @GetMapping(WEATHER_LIST)
    public List<WeatherDTO> getWeatherList() {
        log.info("Getting weather from every location saved in database");
        return weatherService.getWeatherListForIncomingDays();
    }

    @ApiOperation(value = "Post new weather location",
            notes = "Post new weather forecastlocation based on lat and lon geometry parameters")
    @PostMapping(WEATHER_NEW)
    Weather newWeatherOrder(@ApiParam(value = "Value of latitude for the location - required", example = "90.0") @RequestParam Double lat, @ApiParam(value = "Value of longitude for the location - required", example = "90.0") @RequestParam Double lon) {
        log.info("Passing lat and lon parameters to trigger POSTMAPPING save new weather location");
        return weatherService.newWeatherOrder(lat, lon);
    }

    @ApiOperation(value = "Get weather forecast by geometry values",
            notes = "Getting weather forecast for location stored in database by lat and lon geometry parameters")
    @GetMapping(WEATHER_FIND)
    public WeatherDTO getWeatherByGeo(@ApiParam(value = "Value of latitude - required", example = "50.0") @RequestParam Double lat, @ApiParam(value = "Value of longitude - required", example = "19.0") @RequestParam Double lon) {
        log.info("Get weather by lat lon or throw WeatherNotFoundException");
        return weatherService.getWeatherByGeometry(lat, lon);
    }

    @ApiOperation(value = "Put weather forecast",
            notes = "Updating forecast for every location")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PutMapping(WEATHER_UPDATE)
    public void updateWeather() {
        log.info("getting every location and updating weather forecast");
        weatherService.updateAll();
    }

    @ApiOperation(value = "Delete location by geometry values",
            notes = "Deleting weather from database based on lat and lon parameters")
    @DeleteMapping(WEATHER_DELETE)
    public void deleteWeatherByGeo(@ApiParam(value = "Value of longitude - required", example = "50.0") @RequestParam Double lat, @ApiParam(value = "Value of longitude - required", example = "19.0") @RequestParam Double lon) {
        log.info("deleting by lat and lon coords or else throw WeatherNotFound exception");
        weatherService.deleteWeatherByGeometry(lat, lon);
    }


}
