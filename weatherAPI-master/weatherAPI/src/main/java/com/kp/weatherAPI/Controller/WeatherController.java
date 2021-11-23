package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.EntityDTO.WeatherDTO;
import com.kp.weatherAPI.Exceptions.ConflictException;
import com.kp.weatherAPI.Exceptions.NotFoundException;
import com.kp.weatherAPI.Exceptions.ValuesOutOfBoundsException;
import com.kp.weatherAPI.Service.WeatherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    private final static String WEATHER_NEW = "/new";
    private final static String WEATHER_FIND = "/find";
    private final static String WEATHER_UPDATE = "/update";
    private final static String WEATHER_DELETE = "/delete";
    private final static String WEATHER_LIST = "/list";

    @ApiOperation(value = "Post new weather location", notes = "Post new weather forecastlocation based on lat and lon geometry parameters")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request accepted. Saved new location with forecast.", responseContainer = "List"),
            @ApiResponse(code = 406, message = "Value of lat or lon is out of bound.", response = ValuesOutOfBoundsException.class),
            @ApiResponse(code = 409, message = "Location with given parameters already exists.", response = ConflictException.class),
            @ApiResponse(code = 503, message = "Server internal error.")})
    @PostMapping(WEATHER_NEW)
    WeatherDTO newWeatherOrder(@ApiParam(value = "Value of latitude for the location - required", example = "50.0", required = true) @RequestParam Double lat,
                               @ApiParam(value = "Value of longitude for the location - required", example = "19.0", required = true) @RequestParam Double lon) {
        log.info("Passing lat and lon parameters to trigger PostMapping save new weather location");
        return weatherService.newWeatherOrder(lat, lon);
    }

    @ApiOperation(value = "Put weather forecast", notes = "Updating forecast for every location")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Request accepted. Updating weather forecast.", responseContainer = "List"),
            @ApiResponse(code = 503, message = "Server internal error.")})
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PutMapping(WEATHER_UPDATE)
    public void updateWeather() {
        log.info("getting every location and updating weather forecast");
        weatherService.updateAll();
    }

    @ApiOperation(value = "Get weather forecast list", notes = "Showing weather for every location stored in database", response = WeatherDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request accepted. Returning list of forecast for every location stored.", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = NotFoundException.class),
            @ApiResponse(code = 406, message = "Value of lat or lon is out of bound.", response = ValuesOutOfBoundsException.class),
            @ApiResponse(code = 503, message = "Server internal error.")})
    @GetMapping(WEATHER_LIST)
    public List<WeatherDTO> getWeatherList() {
        log.info("Getting weather from every location saved in database");
        return weatherService.getWeatherListForIncomingDays();
    }

    @ApiOperation(value = "Get weather forecast by geometry values", notes = "Getting weather forecast for location stored in database by lat and lon geometry parameters")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request accepted. Location found. Returning forecast for given location.", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Given location does not exist in database.", response = NotFoundException.class),
            @ApiResponse(code = 503, message = "Server internal error.")})
    @GetMapping(WEATHER_FIND)
    public WeatherDTO getWeatherByGeo(@ApiParam(value = "Value of latitude - required", example = "50.0", required = true) @RequestParam Double lat,
                                      @ApiParam(value = "Value of longitude - required", example = "19.0", required = true) @RequestParam Double lon) {
        log.info("Get weather by lat lon or throw WeatherNotFoundException");
        return weatherService.getWeatherByGeometry(lat, lon);
    }

    @ApiOperation(value = "Delete location by geometry values", notes = "Deleting weather from database based on lat and lon parameters")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request accepted. Location deleted from database.", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Given location does not exist in database.", response = NotFoundException.class),
            @ApiResponse(code = 503, message = "Server internal error.")})
    @DeleteMapping(WEATHER_DELETE)
    public void deleteWeatherByGeo(@ApiParam(value = "Value of longitude - required", example = "50.0", required = true) @RequestParam Double lat,
                                   @ApiParam(value = "Value of longitude - required", example = "19.0", required = true) @RequestParam Double lon) {
        log.info("deleting by lat and lon coords or else throw WeatherNotFound exception");
        weatherService.deleteWeatherByGeometry(lat, lon);
    }


}
