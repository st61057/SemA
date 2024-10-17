package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceSensorDto {

    @Schema(description = "Device name", example = "Main device")
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Sensor name is required.")
    private String sensorName;
}
