
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Next6Hours {

    @SerializedName("summary")
    @Expose
    private Summary__2 summary;
    @SerializedName("details")
    @Expose
    private Details__2 details;

    public Summary__2 getSummary() {
        return summary;
    }

    public void setSummary(Summary__2 summary) {
        this.summary = summary;
    }

    public Details__2 getDetails() {
        return details;
    }

    public void setDetails(Details__2 details) {
        this.details = details;
    }

}
