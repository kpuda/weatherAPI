package com.kp.weatherAPI.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {

    private String time;

    private DetailsDTO details;


}
