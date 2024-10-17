package org.example.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column
    private BigDecimal sensorTemperature;

    @Column
    private BigDecimal sensorUsageEnergy;

    @Column
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Sensor(String name, BigDecimal sensorTemperature, BigDecimal sensorUsageEnergy, SensorType sensorType) {
        this.name = name;
        this.sensorTemperature = sensorTemperature;
        this.sensorUsageEnergy = sensorUsageEnergy;
        this.sensorType = sensorType;
    }
}
