package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;

import java.util.*;

public interface TimeseriesService {

    List<Timeseries> updateWeatherTimeseries(List<Timeseries> oldList, List<Timeseries> newData);

}
