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


public interface TimeseriesService {

    List<Timeseries> updateWeatherTimeseries(List<Timeseries> oldList, List<Timeseries> newData);

}
