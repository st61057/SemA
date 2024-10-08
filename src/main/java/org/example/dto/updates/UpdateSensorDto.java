package org.example.dto.updates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSensorDto {

    @Schema(description = "Current sensor name", example = "testsenzor_1")
    @NotBlank(message = "Name is required.")
    private String oldName;

    @Schema(description = "Updated sensor name", example = "testsenzor_1.0")
    @NotBlank(message = "Name is required.")
    private String newName;

}
