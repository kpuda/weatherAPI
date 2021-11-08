package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Timeseries;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TimeseriesServiceTest {

    @Autowired
    TimeseriesService timeseriesService;


}