package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Repository.GeometryRepository;
import com.kp.weatherAPI.Repository.TimeseriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimeseriesService {

    private final TimeseriesRepository timeseriesRepository;
    private final GeometryService geometryService;

    public TimeseriesService(TimeseriesRepository timeseriesRepository, GeometryService geometryService) {
        this.timeseriesRepository = timeseriesRepository;
        this.geometryService = geometryService;
    }
    public List<Timeseries> updateWeather(List<Timeseries> oldList, List<Timeseries> newData) throws IOException {
        List<Timeseries> timeseries = new ArrayList<>(
                Stream.of(oldList, newData)
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(Timeseries::getTime,
                                timeseries1 -> timeseries1,
                                (Timeseries x, Timeseries y) -> x == null ? y : x))
                        .values());

        timeseries.forEach(timeseriesList -> {
            newData.stream()
                    .filter(timeseriesNewData -> timeseriesNewData.getTime().equals(timeseriesList.getTime()))
                    .forEach(timeseriesNewData -> timeseriesList.setData(timeseriesNewData.getData()));
        });
        return timeseries;
    }
}
