
package com.kp.weatherAPI.Entity;

import java.util.List;
import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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

    private String type;

    @ElementCollection
    @CollectionTable(name="geometryCoordinates")
    private List<Double> coordinates = null;


}
