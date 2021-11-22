package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DataDTO {

    private String time;
    private double avgTempMorning;

    @Embedded
    private InstantDTO instant;
}
