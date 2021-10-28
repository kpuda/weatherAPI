
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Generated("jsonschema2pojo")
public class Data {

    @SerializedName("instant")
    @Expose
    @Embedded
    private Instant instant;
   /* @SerializedName("next_12_hours")
    @Expose
    private Next12Hours next12Hours;
    @SerializedName("next_1_hours")
    @Expose
    private Next1Hours next1Hours;
    @SerializedName("next_6_hours")
    @Expose
    private Next6Hours next6Hours;*/


}