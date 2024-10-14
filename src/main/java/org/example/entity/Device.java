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

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Sensor> sensorList = Collections.EMPTY_LIST;

    public Device(String name, List<Sensor> sensorList) {
        this.name = name;
        this.sensorList = sensorList;
    }

}
