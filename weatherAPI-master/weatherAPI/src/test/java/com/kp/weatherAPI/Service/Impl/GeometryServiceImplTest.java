package com.kp.weatherAPI.Service.Impl;

import com.kp.weatherAPI.Entity.Geometry;
import com.kp.weatherAPI.Repository.GeometryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RequiredArgsConstructor
@Slf4j
class GeometryServiceImplTest {


      GeometryServiceImpl geometryService;
     GeometryRepository geometryRepository;


    @Test
    void showGeometryList(){
      log.info("{} ok for now");
        //List<Geometry> geometryList = geometryService.getGeometryList();
        log.info("{} did it pass?");
    }
    @Test
    void showGeoId(){
        geometryService.getGeometryIdByLatLon(50.0,19.0);
    }
}