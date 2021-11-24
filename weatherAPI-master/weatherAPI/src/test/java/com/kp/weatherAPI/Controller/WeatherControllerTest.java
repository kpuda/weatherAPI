package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.EntityDTO.*;
import com.kp.weatherAPI.Service.Impl.GeometryServiceImpl;
import com.kp.weatherAPI.Service.Impl.TimeseriesServiceImpl;
import com.kp.weatherAPI.Service.Impl.WeatherServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @MockBean
    private WeatherServiceImpl weatherService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherController weatherController;


   /* WeatherDTO weatherDTO = new WeatherDTO(
            new GeometryDTO(19.0, 50.0, 200.0),
            new PropertiesDTO(
                    List.of(
                            new TimeseriesDTO("23-11-2021", List.of(
                                    new DataDTO("12:00:00", new DetailsDTO(10.0, 5.0, 10.0, 10.0, 12.0, 1.0)))))
            ));
    WeatherDTO weatherDTO2 = new WeatherDTO(
            new GeometryDTO(17.0, 53.0, 200.0),
            new PropertiesDTO(
                    List.of(
                            new TimeseriesDTO("24-11-2021", List.of(
                                    new DataDTO("12:00:00", new DetailsDTO(10.0, 5.0, 10.0, 10.0, 12.0, 1.0)))))
            ))*/;


    @BeforeEach
    void setUp() throws Exception {
        List<WeatherDTO> weatherDTOList = (List<WeatherDTO>) weatherService.getWeatherByGeometry(50.0, 19.0);
        System.out.println("Setup "+weatherDTOList);
    }

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void getHelloWorld() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/weather/hello")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello world"));
    }

    @Test
    void newWeatherOrder() {
    }

    @Test
    void updateWeather() {
    }

    @Test
    void shouldReturnWeatherList() throws Exception {
        when(weatherService.getWeatherList()).thenReturn(null);
        System.out.println(weatherService.getWeatherList());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/weather/list")
                        .header("X-foo", "Duke"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    void getWeatherByGeo() throws Exception {
/*        System.out.println(weatherDTO+"yes");
        when(weatherService.getWeatherByGeometry(50.0,19.0)).thenReturn(weatherDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/weather/find?lat=50&lon=19")
                .header("Content-type","user-agent"))
                .andExpect(MockMvcResultMatchers.status().isOk()); //TODO*/
    }

    @Test
    void deleteWeatherByGeo() throws Exception {
       /// verify()
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/weather/delete")
                .header("Content-type","user-agent"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}