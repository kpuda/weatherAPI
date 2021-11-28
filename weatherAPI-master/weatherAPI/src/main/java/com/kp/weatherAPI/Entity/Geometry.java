
package com.kp.weatherAPI.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geometry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long geometryId;

    @NotNull
    private String type;

    @ElementCollection
    @CollectionTable(name = "geometryCoordinates")
    private List<Double> coordinates;


}
