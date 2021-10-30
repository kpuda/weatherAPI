
package com.kp.weatherAPI.Entity;

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
@Generated("jsonschema2pojo")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("geometry")
    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geometry_id",referencedColumnName = "geometryId")
    private Geometry geometry;

    @SerializedName("properties")
    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id",referencedColumnName = "propertiesId")
    private Properties properties;

}
