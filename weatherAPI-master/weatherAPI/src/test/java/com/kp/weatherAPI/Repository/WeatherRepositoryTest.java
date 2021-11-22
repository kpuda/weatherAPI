package com.kp.weatherAPI.Repository;

import com.kp.weatherAPI.Entity.Weather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    void shouldReturnList() {
        List<Weather> all = weatherRepository.findAll();
        System.out.println(all);
    }
}