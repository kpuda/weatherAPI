
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Details__2 {

    @SerializedName("precipitation_amount")
    @Expose
    private Double precipitationAmount;

    public Double getPrecipitationAmount() {
        return precipitationAmount;
    }

    public void setPrecipitationAmount(Double precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

}
