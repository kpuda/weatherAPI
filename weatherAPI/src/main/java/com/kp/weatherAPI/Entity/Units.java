
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.Embeddable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Generated("jsonschema2pojo")
public class Units {

    @SerializedName("air_pressure_at_sea_level")
    @Expose
    private String airPressureAtSeaLevel;
    @SerializedName("air_temperature")
    @Expose
    private String airTemperature;
    @SerializedName("cloud_area_fraction")
    @Expose
    private String cloudAreaFraction;
    @SerializedName("precipitation_amount")
    @Expose
    private String precipitationAmount;
    @SerializedName("relative_humidity")
    @Expose
    private String relativeHumidity;
    @SerializedName("wind_from_direction")
    @Expose
    private String windFromDirection;
    @SerializedName("wind_speed")
    @Expose
    private String windSpeed;

}
