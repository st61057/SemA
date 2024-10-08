package org.example.dto;

import lombok.*;
import org.example.entity.Device;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String username;

    private String email;

    private String password;

    @Nullable
    private List<Device> devices;

}
