package com.kp.weatherAPI.Service.Impl;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Exceptions.ConflictException;
import com.kp.weatherAPI.Exceptions.NotFoundException;
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
    private final String GEOMETRY_NOT_FOUND = "Weather doesn't exists.";
    private final String GEOMETRY_ALREADY_EXISTS = "Weather for this place already exists.";

    @Override
    public List<Geometry> getGeometryList() {
        log.info("Getting list of stored geometries.");
        return geometryRepository.findAll();
    }

    @Override
    public Optional<Long> getGeometryIdByLatLon(Double lat, Double lon) {
        log.info("Getting list of available geometries.");
        List<Geometry> geometryList = getGeometryList();
        log.info("Streaming list to filter if any of ID meets the requirements.");
        return geometryList.stream()
                .filter(geometry -> geometry.getCoordinates().get(1).equals(lat) && geometry.getCoordinates().get(0).equals(lon))
                .map(Geometry::getGeometryId)
                .findFirst();
    }

    @Override
    public Long validateIfGeometryByIdExists(Optional<Long> id) {
        return id.orElseThrow(() -> new NotFoundException(GEOMETRY_NOT_FOUND));
    }

    @Override
    public Boolean validateIfGeometryByIdDoesntExists(Optional<Long> id) {
        if (!id.isEmpty()) {
            throw new ConflictException(GEOMETRY_ALREADY_EXISTS);
        } else {
            return true;
        }
    }
}
