
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Generated("jsonschema2pojo")
public class Details {

    @SerializedName("air_pressure_at_sea_level")
    @Expose
    private Double airPressureAtSeaLevel;
    @SerializedName("air_temperature")
    @Expose
    private Double airTemperature;
    @SerializedName("cloud_area_fraction")
    @Expose
    private Double cloudAreaFraction;
    @SerializedName("relative_humidity")
    @Expose
    private Double relativeHumidity;
    @SerializedName("wind_from_direction")
    @Expose
    private Double windFromDirection;
    @SerializedName("wind_speed")
    @Expose
    private Double windSpeed;

}
