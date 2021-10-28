
package com.kp.weatherAPI.EntityAPI;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@lombok.Data
@Generated("jsonschema2pojo")
public class Data {

    @SerializedName("instant")
    @Expose
    private Instant instant;
    /*@SerializedName("next_12_hours")
    @Expose
    private Next12Hours next12Hours;
    @SerializedName("next_1_hours")
    @Expose
    private Next1Hours next1Hours;
    @SerializedName("next_6_hours")
    @Expose
    private Next6Hours next6Hours;*/



}
