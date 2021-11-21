package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeometryRepository extends JpaRepository<Geometry, Long> {
}
