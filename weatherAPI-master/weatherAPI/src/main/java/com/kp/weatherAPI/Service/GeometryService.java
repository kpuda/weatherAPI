package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Repository.GeometryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeometryService {

    private final GeometryRepository geometryRepository;

    public GeometryService(GeometryRepository geometryRepository) { this.geometryRepository = geometryRepository;}

    public List<Geometry> getGeometryList() {return geometryRepository.findAll();}

    public Long getGeometryId(Double lat, Double lon) {
        List<Geometry> geometryList = getGeometryList();
        Long geoId = null;
        for (Geometry geometry : geometryList) {
            if (geometry.getCoordinates().get(0).equals(lon) && geometry.getCoordinates().get(1).equals(lat)) {
                geoId = geometry.getGeometryId();
            }
        }
        return geoId;
    }

}
