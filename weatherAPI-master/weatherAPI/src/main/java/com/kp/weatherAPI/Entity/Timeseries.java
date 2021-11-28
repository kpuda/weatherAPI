
package com.kp.weatherAPI.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.OptionalDouble;

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

    @Transient
    private OptionalDouble avgMorningTemp;

    @Override
    public int compareTo(Timeseries o) {
        return this.getTime().compareTo(o.getTime());
    }
}
