
package com.kp.weatherAPI.Entity;

import javax.persistence.*;

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
