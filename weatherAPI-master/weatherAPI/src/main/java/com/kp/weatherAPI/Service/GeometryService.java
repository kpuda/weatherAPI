package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Geometry;

import java.util.List;
import java.util.Optional;

public interface GeometryService {

    Long validateIfGeometryByIdExists(Optional<Long> id);

    Optional<Long> getGeometryIdByLatLon(Double lat, Double lon);

    List<Geometry> getGeometryList();

    Boolean validateIfGeometryByIdDoesntExists(Optional<Long> id);
}
