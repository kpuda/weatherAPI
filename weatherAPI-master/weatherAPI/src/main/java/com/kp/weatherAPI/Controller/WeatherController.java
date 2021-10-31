package com.kp.weatherAPI.Controller;

import com.google.gson.Gson;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Service.GeometryService;
import com.kp.weatherAPI.Service.TimeseriesService;
import com.kp.weatherAPI.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@RestController("/")
public class WeatherController {

    private WeatherService weatherService;
    private GeometryService geometryService;
    private TimeseriesService timeseriesService;

    @Autowired
    public WeatherController(WeatherService weatherService, GeometryService geometryService, TimeseriesService timeseriesService) {
        this.weatherService = weatherService;
        this.geometryService = geometryService;
        this.timeseriesService = timeseriesService;
    }


    //GET SECTION
    @GetMapping("/new") // new?lat=50&lon=19
    public com.kp.weatherAPI.Entity.Weather newOrder(@RequestParam Double lat, @RequestParam Double lon) throws IOException {
        URL url=new URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat="+lat+"&lon="+lon);
        HttpURLConnection httpConn= (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
        httpConn.getContentType(); //dlaczego bez tego mam 403?

        InputStreamReader reader=new InputStreamReader(url.openStream());
        Weather weather=new Gson().fromJson(reader, com.kp.weatherAPI.Entity.Weather.class);
        List<Timeseries> t=weather.getProperties().getTimeseries();
        t.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(t);
        return weather;
    }

    @GetMapping("/all")
    public List<Weather> showAll(){
        return weatherService.fetchAll();
    }

    @GetMapping("/byGeo")
    public List<Weather> weatherByGeo(@RequestParam Double lat, @RequestParam Double lon)  {
        if (geometryService.getGeometryId(lat,lon)!=-1){
            Long id=geometryService.getGeometryId(lat,lon);
            return weatherService.fetchByGeoId(id);
        } else
            return null;
    }

    // POST SECTION
    @PostMapping("/saveWeather")
    public String saveWeather(@RequestBody Weather weather) {
        if (geometryService.getGeometryId(
                        weather.getGeometry().getCoordinates().get(1),
                        weather.getGeometry().getCoordinates().get(0) ) == -1 ) {
            weatherService.saveWeather(weather);
            return "Saved new location";
        } else {
            return "Can't save this location. Location already in database";
        }
    }

    // PUT SECTION
    @PutMapping("/update") // dla pojedynczych miejsc
    public ResponseEntity<Weather> updateWeather(@RequestParam Double lat, @RequestParam Double lon) throws IOException {
        if (geometryService.getGeometryId(lat,lon)!=-1) {
            /*Weather temp = newOrder(lat, lon); DRY
            Weather weather = (weatherService.fetchByGeoId(geometryService.getGeometryId(lat, lon))).get(0);
            List<Timeseries> newList = timeseriesService.updateWeather(weather.getProperties().getTimeseries(), temp.getProperties().getTimeseries());
            newList.sort(Timeseries::compareTo);
            weather.getProperties().setTimeseries(newList);*/
            weatherService.saveWeather(compareTimeseriesData(lat,lon));
            return ResponseEntity.ok(compareTimeseriesData(lat,lon));
        } else {
            return null;
        }
    }

    @PutMapping("/updateAllLocations") // dla wszystkich  miejsc
    public void updateWeatherEverwhere() throws IOException {
        List<Geometry> geometryList=geometryService.getGeometryList();
        Weather temp;
        Weather weather;
        for (Geometry geometry : geometryList) {
            /* for (int i=0;i<geometryList.size();i++)
            temp=newOrder(geometryList.get(i).getCoordinates().get(1),geometryList.get(i).getCoordinates().get(0));
           weather=(weatherService.fetchByGeoId(geometryService.getGeometryId(geometryList.get(i).getCoordinates().get(1),geometryList.get(i).getCoordinates().get(0)))).get(0);
           List<Timeseries> newList= timeseriesService.updateWeather(weather.getProperties().getTimeseries(),temp.getProperties().getTimeseries());
            newList.sort(Timeseries::compareTo);
            weather.getProperties().setTimeseries(newList);*/
            weatherService.saveWeather(
                    compareTimeseriesData(
                            geometry.getCoordinates().get(1), geometry.getCoordinates().get(0)
                    ));
        }
    }

    // DELETE SECTION
    @DeleteMapping("/deleteLocationByGeo") // deleteLocationByGeo?lat=30&lon=1
    public String deleteLocationByGeo(@RequestParam Double lat, @RequestParam Double lon) {
        if (geometryService.getGeometryId(lat,lon)!=-1){
            weatherService.deleteWeatherById(geometryService.getGeometryId(lat,lon));
        return "Deleted location";
        } else
            return "Could not delete location. Either it doesn't exist in database or internal error happened";
    }

    private Weather compareTimeseriesData(Double lat, Double lon) throws IOException {
        Weather temp=newOrder(lat,lon);
        Weather weather = (weatherService.fetchByGeoId(geometryService.getGeometryId(lat, lon))).get(0);
        List<Timeseries> newList = timeseriesService.updateWeather(weather.getProperties().getTimeseries(), temp.getProperties().getTimeseries());
        newList.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(newList);
        return weather;
    }
}
