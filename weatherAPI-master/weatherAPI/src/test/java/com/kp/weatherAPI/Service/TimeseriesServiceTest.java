package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TimeseriesServiceTest {

    @Autowired
    TimeseriesService timeseriesService;

    @Test
    public void fetchData(){
        List<Timeseries> timeseriesList=timeseriesService.fetchAllTimeseries();
        System.out.println(timeseriesList.toString());
    }
    @Test
    public void fetchDataByPropertiesId(){
        List<Timeseries> timeseriesList=timeseriesService.fetchTimeseriesByPropertiesId(1L);
        System.out.println(timeseriesList.toString());
    }
    @Test
    public void showId(){
        timeseriesService.fetchTimeSeriesByLatLonGeo(50.0,19.0);
    }
}