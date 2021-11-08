package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather,Long> {

    @Query(
            value = "select * from weather where geometry_id=?;",
            nativeQuery = true
    )
    Weather findWeatherByGeoId(Long id);
}
