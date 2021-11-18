package com.kp.weatherAPI.Service.Impl;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Service.TimeseriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeseriesServiceImpl implements TimeseriesService {

    @Override
    public List<Timeseries> updateWeatherTimeseries(List<Timeseries> oldList, List<Timeseries> newData) {
        log.info("Comparing old and new list of forecast. To eliminate duplicates in timestamps getTime is used as Key value in map.");
        List<Timeseries> timeseries = new ArrayList<>(
                Stream.of(oldList, newData)
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(Timeseries::getTime,
                                timeseriesData -> timeseriesData,
                                (Timeseries oldTimeseries, Timeseries newtTimeseries) -> oldTimeseries))
                        .values());


        timeseries.sort(Timeseries::compareTo);

        log.info("Overwriting forecast up to date.");
        newData.forEach(timeseriesList -> timeseries.stream()
                .filter(timeseriesOldData -> timeseriesList.getTime().equals(timeseriesOldData.getTime()))
                .forEach(timeseriesOldData -> timeseriesOldData.setData(timeseriesList.getData())));
        return timeseries;
    }

}
