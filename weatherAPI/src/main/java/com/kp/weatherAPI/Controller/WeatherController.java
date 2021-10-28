package com.kp.weatherAPI.Controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.kp.weatherAPI.Entity.Properties;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.EntityAPI.Geometry;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController("/")
public class WeatherController {
    private WeatherRepository weatherRepository;
    private WeatherService weatherService;
    com.kp.weatherAPI.Entity.Weather weatherDb;

    @Autowired
    public WeatherController(WeatherRepository weatherRepository, WeatherService weatherService) {
        this.weatherRepository = weatherRepository;
        this.weatherService = weatherService;
    }


    @GetMapping("/new")
    public com.kp.weatherAPI.Entity.Weather newOrder(@RequestHeader(value = "User-Agent") String userAgent, @RequestParam int lat, @RequestParam int lon) throws IOException {
        URL url=new URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat="+lat+"&lon="+lon);
        HttpURLConnection httpConn= (HttpURLConnection) url.openConnection();

        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
        System.out.println(httpConn.getContentType());
        InputStreamReader reader=new InputStreamReader(url.openStream());
        weatherDb=new Gson().fromJson(reader, com.kp.weatherAPI.Entity.Weather.class);
/*   com.kp.weatherAPI.Entity.Weather weather=new Gson().fromJson(reader, Weather.class);
        List<Timeseries> timeseries=weatherDb.getProperties().getTimeseries().stream()
                .collect(Collectors.toList());
        System.out.println(timeseries);*/

        return weatherDb;
    }
    @GetMapping("/all")
    public List<Weather> showAll(){
        List<Weather>weathers=weatherRepository.findAll();
        return weathers;
    }

    @PostMapping("/saveWeather")
    public void saveWeather(@RequestBody Weather weather){
        weatherService.saveWeather(weatherDb);
    }

}
