package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather,Long> {


}
