
package com.kp.weatherAPI.Entity;

import java.util.List;

import javax.persistence.*;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Double> coordinates = null;

}
