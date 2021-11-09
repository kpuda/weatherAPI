package com.kp.weatherAPI.Controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.WeatherAlreadyExistsException;
import com.kp.weatherAPI.Exceptions.WeatherFetchException;
import com.kp.weatherAPI.Exceptions.WeatherNotFoundException;
import com.kp.weatherAPI.Service.GeometryService;
import com.kp.weatherAPI.Service.TimeseriesService;
import com.kp.weatherAPI.Service.WeatherService;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController()
public class WeatherController {

    RestTemplate restTemplate = new RestTemplate();

    private final WeatherService weatherService;
    private final GeometryService geometryService;
    private final TimeseriesService timeseriesService;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
    private final String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final String USER_AGENT = "user-agent";
    private final String USER_AGENT_URL = "Mozilla/5.0 Firefox/26.0";

    public WeatherController(WeatherService weatherService, GeometryService geometryService, TimeseriesService timeseriesService) {
        this.weatherService = weatherService;
        this.geometryService = geometryService;
        this.timeseriesService = timeseriesService;
    }

    @GetMapping("/new")
    Weather newOrder(@RequestParam Double lat, @RequestParam Double lon) throws IOException {
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        httpHeaders.set(USER_AGENT, url);
        ResponseEntity<String> weatherResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        ObjectMapper objectMapper=new ObjectMapper();
        Weather weather=objectMapper.readValue(weatherResponse.getBody(),Weather.class);
        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(timeseries);
        return weather;
    }
    @GetMapping("/all")
    public List<Weather> showAll() {
        return Optional.ofNullable(
                weatherService.fetchAll())
                .orElseThrow(()->new WeatherFetchException());
    }

    @GetMapping("/byGeo")
    public Weather weatherByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        return Optional.ofNullable(
                        weatherService.fetchWeatherByGeometryId(geometryService.getGeometryId(lat, lon)))
                .orElseThrow(() -> new WeatherNotFoundException(lat, lon));
    }

    @Transactional
    @PostMapping("/saveWeather")
    public Weather saveWeather(@RequestBody Weather weather) {
        if (geometryService.getGeometryId(
                weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0))!=-1){
            throw new WeatherAlreadyExistsException();
        }
        weatherService.saveWeather(weather);
        return weather;
    }

    @Scheduled(fixedRateString = "PT30M")
    @Transactional
    @PutMapping("/update")
    public void updateWeatherEverwhere() throws IOException {
        List<Geometry> geometryList = geometryService.getGeometryList();
        if (geometryList.size()==0){
            throw new WeatherFetchException();
        }
        for (Geometry geometry : geometryList) {
            weatherService.saveWeather(
                    compareTimeseriesData(
                            geometry.getCoordinates().get(1), geometry.getCoordinates().get(0)
                    ));
        }
        System.out.println(new Date());
    }
    @DeleteMapping("/deleteLocationByGeo")
    public void deleteLocationByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        if (geometryService.getGeometryId(lat,lon)==-1){
            throw new WeatherNotFoundException(lat,lon);
        }
        weatherService.deleteWeatherById(geometryService.getGeometryId(lat, lon));
    }

    private Weather compareTimeseriesData(Double lat, Double lon) throws IOException {
        Weather refreshedWeatherData = newOrder(lat, lon);
        Weather oldWeatherData = (weatherService.fetchWeatherByGeometryId(geometryService.getGeometryId(lat, lon)));
        List<Timeseries> newTimeseriesList = timeseriesService.updateWeather(oldWeatherData.getProperties().getTimeseries(), refreshedWeatherData.getProperties().getTimeseries());
        newTimeseriesList.sort(Timeseries::compareTo);
        oldWeatherData.getProperties().setTimeseries(newTimeseriesList);
        return oldWeatherData;
    }

}
