package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsDTO {

    private Double airPressureAtSeaLevel;

    private Double airTemperature;

    private Double cloudAreaFraction;

    private Double relativeHumidity;

    private Double windFromDirection;

    private Double windSpeed;
}
