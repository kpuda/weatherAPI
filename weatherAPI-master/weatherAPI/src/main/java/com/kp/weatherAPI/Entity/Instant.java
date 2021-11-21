
package com.kp.weatherAPI.Entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Instant {

    @Embedded
    private Details details;
}
