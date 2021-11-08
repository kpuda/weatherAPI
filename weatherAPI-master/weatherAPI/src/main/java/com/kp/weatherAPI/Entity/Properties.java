
package com.kp.weatherAPI.Entity;

import java.sql.Time;
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
@Generated("jsonschema2pojo")
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertiesId;

    @SerializedName("meta")
    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meta_id", referencedColumnName = "metaId")
    private Meta meta;

    @SerializedName("timeseries")
    @Expose
    @OneToMany(targetEntity = Timeseries.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id", referencedColumnName = "propertiesId")
    private List<Timeseries> timeseries = null;


}
