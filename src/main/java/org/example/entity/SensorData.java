package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private Instant dataMeasuredTime;

    @Column
    private BigDecimal temperature;

    @Column
    private BigDecimal usageEnergy;

    @ManyToOne
    private Sensor sensor;

    public SensorData(Instant dataMeasuredTime, BigDecimal temperature, BigDecimal usageEnergy, Sensor sensor) {
        this.dataMeasuredTime = dataMeasuredTime;
        this.temperature = temperature;
        this.usageEnergy = usageEnergy;
        this.sensor = sensor;
    }
}
