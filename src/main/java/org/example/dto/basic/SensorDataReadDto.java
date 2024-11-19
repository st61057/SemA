package org.example.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorDataReadDto {

    private Instant dataMeasuredTime;

    private BigDecimal temperature;

    private BigDecimal usageEnergy;
}
