package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.Device;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private String username;

    private String email;

    @Nullable
    private String password;

    @Nullable
    private List<Device> devices;

}
