package com.kp.weatherAPI.Service;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Repository.GeometryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeometryService {

    private GeometryRepository geometryRepository;

    @Autowired
    public GeometryService(GeometryRepository geometryRepository) {
        this.geometryRepository = geometryRepository;
    }

    public List<Geometry> getGeometryList(){
        return geometryRepository.findAll();
    }
    public Long getGeometryId(Double lat,Double lon){
        List<Geometry>geometryList=getGeometryList();
        Long geoId=-1L;
        for (int i=0;i<geometryList.size();i++){
            if (geometryList.get(i).getCoordinates().get(0).equals(lon) && geometryList.get(i).getCoordinates().get(1).equals(lat)){
                geoId= geometryList.get(i).getGeometryId();
            }
        }
        return geoId;
    }
}
