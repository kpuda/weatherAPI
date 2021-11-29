package com.kp.weatherAPI.Controller;

import com.kp.weatherAPI.EntityDTO.WeatherDTO;
import com.kp.weatherAPI.Service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherController weatherController;
    @MockBean
    private WeatherService weatherService;


    @BeforeEach
    void setUp() throws Exception {
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
        WeatherDTO weather= weatherController.newWeatherOrder(50.0,19.0);
        System.out.println("Yes" +weather);
    }

    @Test
    void updateWeather() {
    }

    @Test
    void shouldReturnWeatherList() throws Exception {

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