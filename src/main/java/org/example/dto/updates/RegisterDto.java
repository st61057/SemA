package org.example.dto.updates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {

    @Schema(description = "Users username", example = "pepa")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Users email", example = "pepa@gmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Users password", example = "password1234")
    @NotBlank(message = "Password is required.")
    private String password;
}
