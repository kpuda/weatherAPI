package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeseriesDTO {

    private String date;

    private Double avgTemperatureMorning;

    private Double avgTemperatureNoon;

    private Double avgTemperatureAfterNoon;

    private Double avgTemperatureEvening;

    private List<DataDTO> data;

    public TimeseriesDTO(String date, List<DataDTO> data) {
        this.date = date;
        this.data = data;
    }

    public int compareTo(TimeseriesDTO o) {
        return this.getDate().substring(8).compareTo(o.getDate().substring(8));
    }

}
