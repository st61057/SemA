package org.example.dto.updates;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDeviceDto {

    @Schema(description = "Updated device name", example = "my_device_1")
    @NotBlank(message = "Name is required.")
    private String name;

    @Schema(description = "Sensors names, which are assigned to device", example = "my_sensor_1")
    private List<String> sensorsNames;
}
