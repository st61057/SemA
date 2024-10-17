package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceDto {

    @Schema(description = "Device name", example = "Main device")
    @NotBlank(message = "Name is required.")
    private String name;

    @Schema(description = "Location name", example = "Prague")
    private String location;

    @Schema(description = "Latitude of device", example = "65.458946")
    private Long lat;

    @Schema(description = "Longitude of device", example = "5.458946")
    private Long lon;

    private List<String> sensorsName = new ArrayList<>();

}
