package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.SensorType;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorDto {

    @Schema(description = "Sensor name", example = "humidity sensor xyz")
    @NotBlank(message = "Name is required.")
    private String name;

    @Schema(description = "Sensor type", example = "O")
    @NotBlank(message = "Sensor type is required.")
    private SensorType sensorType;

    @Schema(description = "Sensor temperature", example = "38")
    private BigDecimal sensorTemperature;

    @Schema(description = "Sensor usage energy", example = "O")
    private BigDecimal sensorUsageEnergy;

}
