
package com.kp.weatherAPI.Entity;


import javax.persistence.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Details {

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
