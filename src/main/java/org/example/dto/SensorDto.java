package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorDto {

    @Schema(description = "Sensor name", example = "humidity sensor xyz")
    @NotBlank(message = "Name is required.")
    private String name;

}
