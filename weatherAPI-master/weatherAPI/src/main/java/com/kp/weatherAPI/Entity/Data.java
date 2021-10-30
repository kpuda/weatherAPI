
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//@Entity
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

}
