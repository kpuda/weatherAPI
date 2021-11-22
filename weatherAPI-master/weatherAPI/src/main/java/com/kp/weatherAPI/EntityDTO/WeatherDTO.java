package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherDTO {

    private GeometryDTO geometry;

    private PropertiesDTO properties;
}
