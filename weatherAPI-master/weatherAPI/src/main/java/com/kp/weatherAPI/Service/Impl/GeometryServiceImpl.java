package com.kp.weatherAPI.Service.Impl;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Exceptions.WeatherAlreadyExistsException;
import com.kp.weatherAPI.Exceptions.WeatherNotFoundException;
import com.kp.weatherAPI.Repository.GeometryRepository;
import com.kp.weatherAPI.Service.GeometryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeometryServiceImpl implements GeometryService {

    private final GeometryRepository geometryRepository;

    @Override
    public List<Geometry> getGeometryList() {
        return geometryRepository.findAll();
    }

    @Override
    public Optional<Long> getGeometryIdByLatLon(Double lat, Double lon) {
        List<Geometry> geometryList = getGeometryList();

        return geometryList.stream()
                .filter(geometry -> geometry.getCoordinates().get(1).equals(lat) && geometry.getCoordinates().get(0).equals(lon))
                .map(Geometry::getGeometryId)
                .findFirst();
    }


    @Override
    public Long validateIfGeometryByIdExists(Optional<Long> id) {
        return id.orElseThrow(WeatherNotFoundException::new);
    }

    @Override
    public Boolean validateIfGeometryByIdDoesntExists(Optional<Long> id) {
        if (!id.isEmpty()) {
            throw new WeatherAlreadyExistsException();
        }
        else
            return true;

    }
}
