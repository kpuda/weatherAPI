package com.kp.weatherAPI.Service.Impl;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Repository.TimeseriesRepository;
import com.kp.weatherAPI.Service.TimeseriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TimeseriesServiceImpl implements TimeseriesService {

    @Override
    public List<Timeseries> updateWeatherTimeseries(List<Timeseries> oldList, List<Timeseries> newData) {
        List<Timeseries> timeseries = new ArrayList<>(
                Stream.of(oldList, newData)
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(Timeseries::getTime,
                                timeseriesData -> timeseriesData,
                                (Timeseries oldTimeseries, Timeseries newtTimeseries) -> oldTimeseries == null ? newtTimeseries : oldTimeseries))
                        .values());
        timeseries.sort(Timeseries::compareTo);

        newData.forEach(timeseriesList -> {
            timeseries.stream()
                    .filter(timeseriesOldData -> timeseriesList.getTime().equals(timeseriesOldData.getTime()))
                    .forEach(timeseriesOldData -> timeseriesOldData.setData(timeseriesList.getData()));
        });
        return timeseries;
    }
}
