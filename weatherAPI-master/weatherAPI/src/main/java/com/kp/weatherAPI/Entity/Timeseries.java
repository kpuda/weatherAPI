
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor

public class Timeseries implements Comparable<Timeseries> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeseriesId;

    @NotNull
    private String time;

    @Embedded
    private Data data;

    @Override
    public int compareTo(Timeseries o) {
        return this.getTime().compareTo(o.getTime());
    }
}
