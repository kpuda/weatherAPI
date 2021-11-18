package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Geometry;

import java.util.List;
import java.util.Optional;

public interface GeometryService {

    List<Geometry> getGeometryList();

    Optional<Long> getGeometryIdByLatLon(Double lat, Double lon);

    Long validateIfGeometryByIdExists(Optional<Long> id);

    Boolean validateIfGeometryByIdDoesntExists(Optional<Long> id);
}
