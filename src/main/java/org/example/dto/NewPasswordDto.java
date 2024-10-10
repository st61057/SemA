package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPasswordDto {

    @Schema(description = "Users username", example = "testuser")
    @NotBlank(message = "Username is required.")
    private String username;

    @Schema(description = "Current password", example = "currentpassword")
    @NotBlank(message = "Current password is required.")
    private String oldPassword;

    @Schema(description = "New password", example = "newPassword1")
    @NotBlank(message = "New password is required.")
    private String newPassword;
}
