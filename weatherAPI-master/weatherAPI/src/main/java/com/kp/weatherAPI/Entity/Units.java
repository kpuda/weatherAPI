
package com.kp.weatherAPI.Entity;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Units {

    @NotNull
    private String airPressureAtSeaLevel;

    @NotNull
    private String airTemperature;

    @NotNull
    private String cloudAreaFraction;

    @NotNull
    private String precipitationAmount;

    @NotNull
    private String relativeHumidity;

    @NotNull
    private String windFromDirection;

    @NotNull
    private String windSpeed;

}
