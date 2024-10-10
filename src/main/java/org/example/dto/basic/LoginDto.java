package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    @Schema(description = "Users username", example = "testuser")
    @NotBlank(message = "Username is required.")
    private String username;

    @Schema(description = "Users password", example = "password1234")
    @NotBlank(message = "Password is required.")
    private String password;

}
