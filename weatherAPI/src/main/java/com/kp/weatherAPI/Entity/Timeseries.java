
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Generated("jsonschema2pojo")
public class Timeseries {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long timeseriesId;

    @SerializedName("time")
    @Expose
    private String time;

    @ManyToOne(cascade = CascadeType.ALL)
    @SerializedName("data")
    @Expose
    @Embedded
    private Data data;


}
