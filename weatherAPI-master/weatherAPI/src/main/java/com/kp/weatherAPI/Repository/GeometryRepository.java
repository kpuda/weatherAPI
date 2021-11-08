package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeometryRepository extends JpaRepository<Geometry,Long> {

}
