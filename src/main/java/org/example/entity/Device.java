package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "devices")
    @JsonIgnore
    private Set<User> user;

    @Column
    private String location;

    @Column
    private Long lat;

    @Column
    private Long lon;

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Sensor> sensorList = new ArrayList<>();

    public Device(String name, String location, Long lat, Long lon, List<Sensor> sensorList) {
        this.name = name;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
        this.sensorList = sensorList;
    }

}
