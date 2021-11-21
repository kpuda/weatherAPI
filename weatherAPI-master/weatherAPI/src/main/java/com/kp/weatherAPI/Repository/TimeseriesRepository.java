package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Timeseries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeseriesRepository extends JpaRepository<Timeseries, Long> {
}
