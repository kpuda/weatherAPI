
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;

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
public class Instant {


    @SerializedName("details")
    @Expose
    @Embedded
    private Details details;


}
