package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDeviceDto {

    @Schema(description = "Users username", example = "username")
    @NotBlank(message = "Username is required.")
    private String username;

    @Schema(description = "Users devices", example = "device_name")
    @NotBlank(message = "Name is required.")
    private String deviceName;
}
