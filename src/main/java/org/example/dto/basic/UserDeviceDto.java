package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDeviceDto {

    @Schema(description = "Users username", example = "Xyzui")
    private String username;

    @Schema(description = "Users devices", example = "Xyzui")
    private String deviceName;
}
