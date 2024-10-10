package org.example.dto.updates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDto {

    @Schema(description = "New password", example = "newPassword1")
    @NotBlank(message = "Password is required.")
    private String newPassword;

    @Schema(description = "Reset code need for authentication request", example = "f7b9104a-af6d-4858-a8c6-98fb3e0a5ea4")
    @NotBlank(message = "Reset code is required.")
    private String resetCode;

}
