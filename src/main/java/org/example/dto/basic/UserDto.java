package org.example.dto.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.entity.Device;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @Schema(description = "Users username", example = "Xyzui")
    private String username;

    @Schema(description = "Users email", example = "xyzui@mail.to")
    private String email;

    @Schema(description = "Users password", example = "passwrod")
    private String password;

    @Schema(description = "Users devices", example = "Xyzui")
    private List<Device> devices;

}
