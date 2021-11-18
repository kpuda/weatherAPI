
package com.kp.weatherAPI.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geometry_id", referencedColumnName = "geometryId")
    private Geometry geometry;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id", referencedColumnName = "propertiesId")
    private Properties properties;

}
