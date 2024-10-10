package org.example.dto.updates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserDto {

    @Schema(description = "Users name", example = "testuser1")
    @NotBlank(message = "Name is required.")
    private String username;

    @Schema(description = "Users email", example = "nnpdasem@gmail.com")
    private String email;

    @Schema(description = "Users assigned devices", example = "my_device_1")
    private List<String> devicesNames;


}
