
package com.kp.weatherAPI.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertiesId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meta_id", referencedColumnName = "metaId")
    private Meta meta;

    @OneToMany(targetEntity = Timeseries.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id", referencedColumnName = "propertiesId")
    private List<Timeseries> timeseries;

}
