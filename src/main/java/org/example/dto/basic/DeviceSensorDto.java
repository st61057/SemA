package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceSensorDto {

    @Schema(description = "Device name", example = "Main device")
    private String name;

    private String sensorName;
}
