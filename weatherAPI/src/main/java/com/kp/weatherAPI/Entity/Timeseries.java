
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
@Entity
@Embeddable
@lombok.Data
@AllArgsConstructor
@Generated("jsonschema2pojo")
public class Timeseries {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long timeseriesId;
    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("data")
    @Expose
    @Embedded
    private Data data;


}
