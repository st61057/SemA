package org.example.dto.updates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserDto {

    @Schema(description = "Logged username", example = "test")
    @NotBlank(message = "Name is required")
    private String loggedUsername;

    @Schema(description = "Users name", example = "testuser1")
    @NotBlank(message = "Name is required.")
    private String username;

    @Schema(description = "Users email", example = "nnpdasem@gmail.com")
    @NotBlank(message = "Name is required.")
    @Email(message = "Email must be valid")
    private String email;

}
