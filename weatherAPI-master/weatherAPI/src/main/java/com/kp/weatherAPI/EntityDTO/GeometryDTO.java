package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeometryDTO {

    private Double lat;

    private Double lon;

    private Double height;

}
