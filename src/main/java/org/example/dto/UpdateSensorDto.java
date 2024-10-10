package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSensorDto {

    @Schema(description = "Current sensor name", example = "my_sensor_1")
    @NotBlank(message = "Name is required.")
    private String oldName;

    @Schema(description = "Updated sensor name", example = "my_sensor_new_1")
    @NotBlank(message = "Name is required.")
    private String newName;

}
