package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.SensorType;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorDto {

    @Schema(description = "Sensor name", example = "humidity sensor xyz")
    @NotBlank(message = "Name is required.")
    private String name;

    private List<SensorDataDto> sensorDataDto = new ArrayList<>();

}
