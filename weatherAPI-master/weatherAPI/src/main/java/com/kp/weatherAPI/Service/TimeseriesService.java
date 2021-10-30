package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Repository.GeometryRepository;
import com.kp.weatherAPI.Repository.TimeseriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimeseriesService {

    private TimeseriesRepository timeseriesRepository;
    private GeometryService geometryService;

    @Autowired
    public TimeseriesService(TimeseriesRepository timeseriesRepository, GeometryService geometryService) {
        this.timeseriesRepository = timeseriesRepository;
        this.geometryService = geometryService;
    }

    public List<Timeseries> fetchAllTimeseries(){
        return timeseriesRepository.findAll();
    }

    public List<Timeseries> fetchTimeseriesByPropertiesId(Long id) {
        return timeseriesRepository.findAllByPropertiesId(id);
    }
    public String fetchTimeSeriesByLatLonGeo(Double lat, Double lon) {
        return geometryService.getGeometryId(lat,lon).toString();
    }
    /*public List<Timeseries> updateWeather(List<Timeseries> updatedTimeseries,Long id){
        List<Timeseries> newList=new ArrayList<>(
                Stream.of( timeseriesRepository.findAllByPropertiesId(id),updatedTimeseries)
                .flatMap(List::stream)
                .collect(Collectors.toMap(Timeseries::getTime,
                        t->t,
                        (Timeseries x,Timeseries y) ->x ==null? y:x))
                .values());

        return newList;
*//*
        List<Timeseries> updateHours= newList.stream()
                .filter(t->t.getTimeseriesId()==null)
                .collect(Collectors.toList());
*//*
    }
*/
    public List<Timeseries> updateWeather(List<Timeseries> oldList,List<Timeseries> newData){
        List<Timeseries> newList=new ArrayList<>(
                Stream.of( oldList,newData)
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(Timeseries::getTime,
                                t->t,
                                (Timeseries x,Timeseries y) ->x ==null? y:x))
                        .values());
        List<Timeseries> updateHours= newList.stream()
                .filter(t->t.getTimeseriesId()==null)
                .collect(Collectors.toList());
        //return updateHours;
        return newList;
    }
}
