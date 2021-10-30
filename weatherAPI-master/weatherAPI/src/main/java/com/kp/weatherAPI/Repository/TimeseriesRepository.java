package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Timeseries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeseriesRepository extends JpaRepository<Timeseries,Long> {

    @Query(
            value = "SELECT * FROM weather.timeseries where properties_id=?;",
            nativeQuery = true
    )
    List<Timeseries> findAllByPropertiesId(Long id);

}
