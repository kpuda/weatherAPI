package com.kp.weatherAPI.EntityDTO;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DetailsDTO {

    @NotNull
    private Double airPressureAtSeaLevel;

    @NotNull
    private Double airTemperature;

    @NotNull
    private Double cloudAreaFraction;

    @NotNull
    private Double relativeHumidity;

    @NotNull
    private Double windFromDirection;

    @NotNull
    private Double windSpeed;
}
