package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeseriesDTO {

    private String date;

    @Transient
    private Double avgTempMorning;

    @Transient
    private Double avgTempNoon;

    @Transient
    private Double avgTempAfterNoon;

    @Transient
    private Double avgTempNight;

    @Embedded
    private DataDTO data;

    public int compareTo(TimeseriesDTO o) {
        return this.getDate().substring(8).compareTo(o.getDate().substring(8));
    }

}
