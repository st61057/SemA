package org.example.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorData> measuredValues = new ArrayList<>();

//    @Column
//    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Sensor(String name/*, SensorType sensorType*/) {
        this.name = name;
//        this.sensorType = sensorType;
    }
}
