
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Units {

    private String airPressureAtSeaLevel;

    private String airTemperature;

    private String cloudAreaFraction;

    private String precipitationAmount;

    private String relativeHumidity;

    private String windFromDirection;

    private String windSpeed;

}
